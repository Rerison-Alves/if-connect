package com.if_connect.recycleviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.dialogs.DialogConvidaUsuario;
import com.if_connect.models.Usuario;

import java.util.List;

public class RecyclerViewUsuariosConvidados extends RecyclerView.Adapter<RecyclerViewUsuariosConvidados.ViewHolder> {
    Context context;
    List<Usuario> usuariosConvidados;
    List<Usuario> usuariosTodos;
    DialogConvidaUsuario dialog;
    View view;
    ViewHolder viewHolder;

    public RecyclerViewUsuariosConvidados(Context context, List<Usuario> usuariosConvidados, List<Usuario> usuariosTodos, DialogConvidaUsuario dialog) {
        this.context = context;
        this.usuariosConvidados = usuariosConvidados;
        this.usuariosTodos = usuariosTodos;
        this.dialog = dialog;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imagemUsuario;
        public TextView nomeUsuario;
        public FrameLayout card;
        public TextView status;
        public ViewHolder(View v) {
            super(v);
            imagemUsuario = v.findViewById(R.id.imagem_usuario);
            nomeUsuario = v.findViewById(R.id.nome);
            card = v.findViewById(R.id.card);
            status = v.findViewById(R.id.status);
        }
    }

    public RecyclerViewUsuariosConvidados.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(context).inflate(R.layout.recycle_card_usuarios, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.status.setText("✓");
        holder.nomeUsuario.setText(usuariosConvidados.get(position).getNome());
        holder.card.setOnClickListener(view -> {
            Usuario usuarioSelecionado = usuariosConvidados.remove(position);
            usuariosTodos.add(usuarioSelecionado);
            dialog.adaptarListas();
        });

    }

    public int getItemCount() {
        return usuariosConvidados.size();
    }
}