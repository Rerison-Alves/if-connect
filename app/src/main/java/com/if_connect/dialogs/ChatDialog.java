package com.if_connect.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;
import com.if_connect.models.Mensagem;
import com.if_connect.models.Usuario;
import com.if_connect.request.WebSocketChatClient;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import java.util.Date;
import java.util.List;

public class ChatDialog extends DialogFragment {
    Encontro encontro;
    Context context;
    FragmentManager fragmentManager;
    private WebSocketChatClient webSocketChatClient;

    List<Mensagem> mensagemList;
    Usuario usuarioLogado;
    String token;

    public ChatDialog(Encontro encontro, Context context, FragmentManager fragmentManager) {
        this.encontro = encontro;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }
    TextView tema;
    RecyclerView chat;
    ImageView send, voltar;
    EditText texto;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_chat,null);

        usuarioLogado = UsuarioManager.getUsuarioLogado();
        token = TokenManager.getInstance(context).getAccessToken();

        tema = view.findViewById(R.id.Tema);
        chat = view.findViewById(R.id.chat);
        send = view.findViewById(R.id.send);
        texto = view.findViewById(R.id.texto);
        voltar = view.findViewById(R.id.voltar);

        tema.setText(encontro.getTema());

        voltar.setOnClickListener(v -> dismiss());

        send.setOnClickListener(view1 -> {
            if(!texto.getText().toString().isEmpty()){
                //new message
                Mensagem mensagem = new Mensagem();
                mensagem.setEncontro(encontro);
                mensagem.setUsuario(usuarioLogado);
                mensagem.setTexto(texto.getText().toString());
                mensagem.setData(new Date());
                webSocketChatClient.sendMessage(mensagem);
                texto.setText("");
            }
        });

        webSocketChatClient = new WebSocketChatClient(encontro.getId().toString(), mensagem -> {
            // Adicionar mensagem recebida à lista e atualizar RecyclerView
            mensagemList.add(mensagem);
            // Atualizar o RecyclerView com a nova mensagem
            // chatAdapter.notifyDataSetChanged();
        });

        try {
            webSocketChatClient.connect();
        } catch (Exception ignored) {}

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocketChatClient != null) {
            webSocketChatClient.disconnect();
        }
    }

//    private class MessageItem extends Item<ViewHolder> {
//
//        private final Mensagem mensagem;
//
//        private MessageItem(Mensagem message) {
//            this.mensagem = message;
//        }
//
//        @Override
//        public void bind(@NonNull ViewHolder viewHolder, int position) {
//            TextView nome = viewHolder.itemView.findViewById(R.id.nomeusuario);
//            TextView txtMsg = viewHolder.itemView.findViewById(R.id.texto);
//
//            nome.setText(mensagem.getNomeUsuario());
//            txtMsg.setText(mensagem.getTexto());
//        }
//
//        @Override
//        public int getLayout() {
//            return mensagem.getIdUsuario().equals(UsuarioAutenticado.UsuarioLogado().getUid())
//                    ? R.layout.mensagem_enviada
//                    : R.layout.mensagem_recebida;
//        }
//    }
}