package com.if_connect.recycleviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.dialogs.DialogPesquisar;
import com.if_connect.models.Curso;
import com.if_connect.models.Local;
import com.if_connect.utils.ImageManager;

import java.util.List;

public class RecyclerViewCursos extends RecyclerView.Adapter<RecyclerViewCursos.ViewHolder> {
    List<Curso> cursos;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;

    public RecyclerViewCursos(Context context, FragmentManager fragmentManager, List<Curso> cursos){
        this.cursos = cursos;
        this.fragmentManager=fragmentManager;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeCurso;
        public ImageView iconeCurso;
        public FrameLayout consulta;
        public ViewHolder(View v){
            super(v);
            nomeCurso = v.findViewById(R.id.nome_curso);
            iconeCurso = v.findViewById(R.id.icone_curso);
            consulta=v.findViewById(R.id.consulta);
        }
    }

    public RecyclerViewCursos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    view = LayoutInflater.from(context).inflate(R.layout.recycle_cursos, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position){
        holder.nomeCurso.setText(cursos.get(position).getDescricao());
        holder.iconeCurso.setImageBitmap(ImageManager.base64StringToBitmap(cursos.get(position).getIconeBase64()));
        holder.consulta.setOnClickListener(view ->
            new DialogPesquisar(context, fragmentManager, "", cursos.get(position).getId()).show(fragmentManager, "tag")
        );
    }

    public int getItemCount(){
        return cursos.size();
    }
}