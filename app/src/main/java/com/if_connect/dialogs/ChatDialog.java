package com.if_connect.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.if_connect.models.Usuario;
import com.if_connect.utils.UsuarioManager;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;
import com.if_connect.models.Mensagem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatDialog extends DialogFragment {

    Encontro encontro;
    Context context;
    FragmentManager fragmentManager;
    Usuario usuarioLogado;
    private GroupAdapter adapter;

    public ChatDialog(Encontro encontro, Context context, FragmentManager fragmentManager) {
        this.encontro=encontro;
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    TextView tema;
    RecyclerView chat;
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

        tema = view.findViewById(R.id.tema);
        chat = view.findViewById(R.id.chat);
        send = view.findViewById(R.id.send);
        texto = view.findViewById(R.id.texto);
        voltar = view.findViewById(R.id.voltar);

        usuarioLogado = UsuarioManager.getUsuarioLogado();

        tema.setText(encontro.getTema());
        voltar.setOnClickListener(view2 -> dismiss());
        adapter = new GroupAdapter();
        chat.setLayoutManager(new LinearLayoutManager(context));
        chat.setAdapter(adapter);
        send.setOnClickListener(view1 -> {
            if(!texto.getText().toString().isEmpty()){
                String textToSend = texto.getText().toString();
                Mensagem mensagem = new Mensagem(encontro, usuarioLogado, textToSend, new Date());
                texto.setText("");
            }
        });
        getMessages();
        return view;
    }

    private void getMessages() {
        List<Mensagem> lista = new ArrayList<>();

        /// TODO
        for (Mensagem mensagem:lista){
            adapter.add(new MessageItem(mensagem));
        }
        chat.scrollToPosition(lista.size()-1);
    }

    private class MessageItem extends Item<GroupieViewHolder> {

        private final Mensagem mensagem;

        private MessageItem(Mensagem message) {
            this.mensagem = message;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView nome = viewHolder.itemView.findViewById(R.id.nomeusuario);
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.texto);

            nome.setText(mensagem.getUsuario().getNome());
            txtMsg.setText(mensagem.getTexto());
        }

        @Override
        public int getLayout() {
            return mensagem.getUsuario().getId().equals(usuarioLogado.getId())
                    ? R.layout.recycle_mensagem_enviada
                    : R.layout.recycle_mensagem_recebida;
        }
    }
}