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
import com.if_connect.dialogs.DialogEncontro;
import com.if_connect.models.Encontro;
import com.if_connect.models.Local;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewEncontros extends RecyclerView.Adapter<RecyclerViewEncontros.ViewHolder> {
    List<Encontro> encontros;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public RecyclerViewEncontros(Context context, FragmentManager fragmentManager, List<Encontro> encontros){
        this.encontros = encontros;
        this.fragmentManager=fragmentManager;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeDoEncontro, data, local;
        public ImageButton edit, excluir;
        public FrameLayout consulta;
        public ViewHolder(View v){
            super(v);
            nomeDoEncontro = v.findViewById(R.id.nomeDoEncontro);
            data = v.findViewById(R.id.data);
            local = v.findViewById(R.id.local);
            edit = v.findViewById(R.id.edit);
            excluir= v.findViewById(R.id.delete);
            consulta=v.findViewById(R.id.consulta);
        }
    }

    public RecyclerViewEncontros.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    view = LayoutInflater.from(context).inflate(R.layout.recycle_grupos_encontros, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position){
        holder.nomeDoEncontro.setText(encontros.get(position).getTema());
        holder.data.setText(
                getHorario(
                        encontros.get(position).getAgendamento().getStartTime(),
                        encontros.get(position).getAgendamento().getEndTime()
        ));
        Local localEncontro = encontros.get(position).getAgendamento().getLocal();
        holder.local.setText(localEncontro!=null?localEncontro.getNome():"Online");
        holder.consulta.setOnClickListener(view -> {
            new DialogEncontro(encontros.get(position), context, fragmentManager).show(fragmentManager, "tag");
        });
    }

    String getHorario(Date startTime, Date endTime){
        return String.format("%s %s-%s",
                dateFormat.format(startTime),
                hourFormat.format(startTime),
                hourFormat.format(endTime));
    }

    public int getItemCount(){
        return encontros.size();
    }
}