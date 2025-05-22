package com.if_connect.recycleviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.dialogs.DialogEncontro;
import com.if_connect.models.Encontro;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewEncontrosPrincipal extends RecyclerView.Adapter<RecyclerViewEncontrosPrincipal.ViewHolder> {

    List<Encontro> Encontros;
    FragmentManager fragmentManager;
    Context context;
    View view;
    ViewHolder viewHolder;

    public RecyclerViewEncontrosPrincipal(Context context, FragmentManager fragmentManager, List<Encontro> Encontros) {
        this.context=context;
        this.fragmentManager=fragmentManager;
        this.Encontros=Encontros;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tema, desc;
        LinearLayout consulta;
        public ViewHolder(View v) {
            super(v);
            tema = v.findViewById(R.id.textviewPrincipal);
            desc = v.findViewById(R.id.desc);
            consulta = v.findViewById(R.id.consulta);
        }
    }

    public RecyclerViewEncontrosPrincipal.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(context).inflate(R.layout.recycle_principal, parent, false);
    viewHolder = new ViewHolder(view);
    return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tema.setText(Encontros.get(position).getTema());

        holder.desc.setText(getDescricao(Encontros.get(position)));

        holder.consulta.setOnClickListener(view -> {
            new DialogEncontro(Encontros.get(position), context, fragmentManager).show(fragmentManager, "tag");
        });


    }

    private String getDescricao(Encontro encontro) {
        Date horaInicio = encontro.getAgendamento().getStartTime();
        Date horaFim = encontro.getAgendamento().getEndTime();
        String local = encontro.getAgendamento().getLocal().getNome() + " - " + encontro.getAgendamento().getLocal().getLocalizacao();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String dataFormatada = dateFormat.format(horaInicio);

        // Comparar data com a data atual
        String dataHojeFormatada = dateFormat.format(new Date());
        if (dataFormatada.equals(dataHojeFormatada)) {
            dataFormatada = "hoje";
        }

        // Formatador para os horários
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String horaInicioFormatada = timeFormat.format(horaInicio);
        String horaFimFormatada = timeFormat.format(horaFim);

        // Montar o bloco de texto

        return dataFormatada + "\n" +
                horaInicioFormatada + "h - " + horaFimFormatada + "h\n" +
                local;
    }

    public int getItemCount() {
        return Encontros.size();
    }
}