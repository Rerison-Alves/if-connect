package com.if_connect.recycleviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;

import java.util.List;

public class RecyclerViewEncontros extends RecyclerView.Adapter<RecyclerViewEncontros.ViewHolder> {
    List<Encontro> encontros;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;

    public RecyclerViewEncontros(Context context, FragmentManager fragmentManager, List<Encontro> encontros){
        this.encontros = encontros;
        this.fragmentManager=fragmentManager;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeDoEncontro, data, local;
        public ImageButton edit;
        public FrameLayout consulta;
        public ViewHolder(View v){
            super(v);
            nomeDoEncontro = v.findViewById(R.id.nomeDoEncontro);
            data = v.findViewById(R.id.data);
            local = v.findViewById(R.id.local);
            edit = v.findViewById(R.id.edit);
            consulta=v.findViewById(R.id.consulta);
        }
    }

    public RecyclerViewEncontros.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    view = LayoutInflater.from(context).inflate(R.layout.card_grupos_encontros, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position){
        holder.nomeDoEncontro.setText(encontros.get(position).getTema());
//        holder.data.setText(encontros.get(position).getAreadeEstudo());
        holder.consulta.setOnClickListener(view -> {
//            new ConsultaEncontroDialog(encontros.get(position), context, fragmentManager);
        });

        //get local e data
    }


    public int getItemCount(){
        return encontros.size();
    }
}