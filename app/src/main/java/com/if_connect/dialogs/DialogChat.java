package com.if_connect.dialogs;

import static com.if_connect.utils.ErrorManager.showError;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.if_connect.models.Usuario;
import com.if_connect.request.ChatWebSocket;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.Page;
import com.if_connect.request.services.MensagemService;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;
import com.if_connect.models.Mensagem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class DialogChat extends DialogFragment {

    Encontro encontro;
    Context context;
    FragmentManager fragmentManager;
    Usuario usuarioLogado;
    GroupAdapter groupAdapterMensagens;
    ChatWebSocket socket;
    MensagemService service;
    String token;

    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLoadingSocket = false;
    private boolean allMessagesLoaded = false;
    private static final int PAGE_SIZE = 20;

    public DialogChat(Encontro encontro, Context context, FragmentManager fragmentManager) {
        this.encontro = encontro;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    TextView tema;
    ShimmerFrameLayout shimmerLayout;
    ConstraintLayout loadingWavy;
    ProgressBar loadingMore;
    RecyclerView recyclerViewChat;
    LinearLayoutManager layoutChat;
    ImageView send, voltar;
    EditText texto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat, container, false);
        service = Generator.getRetrofitInstance().create(MensagemService.class);
        token = TokenManager.getInstance(context).getAccessToken();

        tema = view.findViewById(R.id.tema);
        shimmerLayout = view.findViewById(R.id.shimmer_layout);
        loadingWavy = view.findViewById(R.id.loading_wavy_layout);
        loadingMore = view.findViewById(R.id.loading_more);
        recyclerViewChat = view.findViewById(R.id.chat);
        send = view.findViewById(R.id.send);
        texto = view.findViewById(R.id.texto);
        voltar = view.findViewById(R.id.voltar);

        usuarioLogado = UsuarioManager.getUsuarioLogado();

        groupAdapterMensagens = new GroupAdapter();
        layoutChat = new LinearLayoutManager(context);
        recyclerViewChat.setLayoutManager(layoutChat);
        recyclerViewChat.setAdapter(groupAdapterMensagens);

        recyclerViewChat.addOnScrollListener(getChatListener());

        tema.setText(encontro.getTema());
        voltar.setOnClickListener(view2 -> dismiss());
        send.setOnClickListener(view1 -> {
            String conteudo = texto.getText().toString().trim();
            if (!conteudo.isEmpty()) {
                Mensagem mensagem = new Mensagem(encontro.toLightweight(), usuarioLogado.toLightweight(), conteudo, new Date());
                socket.sendMessage(String.valueOf(encontro.getId()), new Gson().toJson(mensagem));
                texto.setText("");
                addMessageToChat(mensagem);
            }
        });

        loadInitialMessages();
        initWebSocket();

        return view;
    }

    private void initWebSocket() {
        isLoadingSocket = true;

        socket = ChatWebSocket.getInstance();

        socket.setOnConnected(() -> {
            isLoadingSocket = false;
            socket.subscribeToTopic(String.valueOf(encontro.getId()));
            if (!isLoading) {
                stopLoadingAnimations();
            }
        });

        socket.setOnMessageReceived(messageJson -> {
            ((Activity)context).runOnUiThread(() -> {
                String jsonPart = messageJson.substring(messageJson.indexOf("\n\n") + 2).trim();
                Mensagem recebida = new Gson().fromJson(jsonPart, Mensagem.class);
                if (recebida != null && !recebida.getUsuario().getId().equals(usuarioLogado.getId())) {
                    addMessageToChat(recebida);
                }
            });
        });
    }

    private RecyclerView.OnScrollListener getChatListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) DialogChat.this.recyclerViewChat.getLayoutManager();
                if (!isLoading && !allMessagesLoaded && layoutManager != null && !recyclerView.canScrollVertically(-1)) {
                    loadMoreMessages();
                }
            }
        };
    }

    private void stopLoadingAnimations() {
        ((Activity)context).runOnUiThread(() -> {
            loadingWavy.setVisibility(View.GONE);
            recyclerViewChat.setVisibility(View.VISIBLE);
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
        });
    }

    private void loadInitialMessages() {
        currentPage = 0;
        groupAdapterMensagens.clear();
        shimmerLayout.startShimmerAnimation();
        loadMoreMessages();
    }

    private void loadMoreMessages() {
        isLoading = true;
        if (currentPage != 0) loadingMore.setVisibility(View.VISIBLE);
        service.getMensagensPageable(encontro.getId(), "-data", currentPage, PAGE_SIZE, token).enqueue(new Callback<Page<Mensagem>>() {
            @Override
            public void onResponse(Call<Page<Mensagem>> call, retrofit2.Response<Page<Mensagem>> response) {
                isLoading = false;
                loadingMore.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Mensagem> mensagens = response.body().getContent();

                        if (mensagens.size() < PAGE_SIZE) {
                            allMessagesLoaded = true;
                        }
                        for (Mensagem mensagem:mensagens) {
                            groupAdapterMensagens.add(0, new MessageItem(mensagem));
                        }
                        if (currentPage == 0) {
                            recyclerViewChat.scrollToPosition(groupAdapterMensagens.getItemCount() - 1);
                            if (!isLoadingSocket) {
                                stopLoadingAnimations();
                            }
                        }
                        currentPage++;
                    }
                } else {
                    showError("Não possível carregar mensagens: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<Page<Mensagem>> call, Throwable t) {
                isLoading = false;
                loadingMore.setVisibility(View.VISIBLE);
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessageToChat(Mensagem mensagem) {
        groupAdapterMensagens.add(new MessageItem(mensagem));
        recyclerViewChat.scrollToPosition(groupAdapterMensagens.getItemCount() - 1);
    }

    private class MessageItem extends Item<GroupieViewHolder> {

        private final Mensagem mensagem;
        private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        private MessageItem(Mensagem message) {
            this.mensagem = message;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView nome = viewHolder.itemView.findViewById(R.id.nomeusuario);
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.texto);
            TextView data = viewHolder.itemView.findViewById(R.id.data);

            nome.setText(mensagem.getUsuario().getNome());
            txtMsg.setText(mensagem.getTexto());
            data.setText(timeFormat.format(mensagem.getData()));
        }

        @Override
        public int getLayout() {
            return mensagem.getUsuario().getId().equals(usuarioLogado.getId())
                    ? R.layout.recycle_mensagem_enviada
                    : R.layout.recycle_mensagem_recebida;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.close();
    }
}