package com.if_connect.recycleviews;

import static java.lang.String.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.dialogs.DialogGrupo;
import com.if_connect.dialogs.DialogTurma;
import com.if_connect.models.Agrupamento;
import com.if_connect.models.Grupo;
import com.if_connect.models.Turma;

import java.util.List;
import java.util.Locale;

public class RecyclerViewTodos extends RecyclerView.Adapter<RecyclerViewTodos.ViewHolder> {
    List<Agrupamento> agrupamentoList;
    Context context;
    View view;
    ViewHolder viewHolder;
    FragmentManager fragmentManager;

    public RecyclerViewTodos(Context context, FragmentManager fragmentManager, List<Agrupamento> agrupamentoList){
        this.agrupamentoList = agrupamentoList;
        this.context=context;
        this.fragmentManager=fragmentManager;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nome, tipoArupamento, participantes;
        public FrameLayout consulta;
        public ViewHolder(View v){
            super(v);
            nome = v.findViewById(R.id.textviewPrincipal);
            consulta = v.findViewById(R.id.consulta);
            tipoArupamento = v.findViewById(R.id.tipo_agrupamento);
            participantes = v.findViewById(R.id.participantes);
        }
    }

    public RecyclerViewTodos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        view = LayoutInflater.from(context).inflate(R.layout.recycle_todos, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        Agrupamento agrupamento = agrupamentoList.get(position);

        holder.nome.setText(agrupamento.getNome());
        holder.tipoArupamento.setText(agrupamento.getTipoAgrupamento().getTipo());
        holder.participantes.setText(getQuantParticipantes(agrupamento));
        holder.consulta.setOnClickListener(view -> {
            if(agrupamento instanceof Grupo){
                new DialogGrupo((Grupo) agrupamento, context, fragmentManager).show(fragmentManager, "tag");
            }else if (agrupamento instanceof Turma) {
                new DialogTurma((Turma) agrupamento, context, fragmentManager).show(fragmentManager, "tag");
            }
        });

    }

    private String getQuantParticipantes(Agrupamento agrupamento){
        int quant = 0;
        if(agrupamento instanceof Grupo){
            quant = ((Grupo) agrupamento).getUsuarios().size();
        }else if (agrupamento instanceof Turma) {
            quant = ((Turma) agrupamento).getUsuarios().size();
        }
        return format(Locale.getDefault(), "%d participante%s", quant, quant > 1 ? "s" : "");
    }

    public int getItemCount(){
        return agrupamentoList.size();
    }
}
