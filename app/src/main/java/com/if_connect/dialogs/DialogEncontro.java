package com.if_connect.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.models.Agendamento;
import com.if_connect.models.Encontro;
import com.if_connect.models.Local;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DialogEncontro extends DialogFragment {

    Encontro encontro;
    Context context;
    FragmentManager fragmentManager;

    public DialogEncontro(Encontro encontro, Context context, FragmentManager fragmentManager) {
        this.encontro = encontro;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    TextView tema, dataEncontro, horarioEncontro, localEncontro, descricaoEncontro;
    ImageView voltar;
    LinearLayout buttonChat;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat hourformat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_encontro, container, false);

        tema = view.findViewById(R.id.tema);
        descricaoEncontro = view.findViewById(R.id.descricao);
        voltar = view.findViewById(R.id.voltar);
        dataEncontro = view.findViewById(R.id.data);
        horarioEncontro = view.findViewById(R.id.horario);
        localEncontro = view.findViewById(R.id.local);
        buttonChat = view.findViewById(R.id.btn_chat);

        tema.setText(encontro.getTema());
        dataEncontro.setText(dateFormat.format(encontro.getAgendamento().getStartTime()));
        horarioEncontro.setText(getHorario(encontro.getAgendamento()));
        Local local = encontro.getAgendamento().getLocal();
        localEncontro.setText(local!=null?local.getNome():"Online");
        descricaoEncontro.setText(encontro.getDescricao());

        voltar.setOnClickListener(view1 -> dismiss());
        buttonChat.setOnClickListener(view12 -> {
            // Abrir chat
        });

        return view;
    }

    String getHorario(Agendamento agendamento){
        return String.format("%s - %s",
                hourformat.format(agendamento.getStartTime()),
                hourformat.format(agendamento.getEndTime()));
    }
}
