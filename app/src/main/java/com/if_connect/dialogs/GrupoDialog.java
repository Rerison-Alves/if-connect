package com.if_connect.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;
import com.if_connect.models.Grupo;
import com.if_connect.recycleviews.RecyclerViewEncontros;

import java.util.ArrayList;
import java.util.List;

public class GrupoDialog extends DialogFragment {
    
    Grupo grupo;
    Context context;
    FragmentManager fragmentManager;
    List<Encontro> encontrosList = new ArrayList<>();

    public GrupoDialog(Grupo grupo, Context context, FragmentManager fragmentManager) {
        this.grupo=grupo;
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    TextView nomeGrupo, areaestudo,descricao, adm;
    RecyclerView listaEncontros;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_grupo, container, false);
        
        nomeGrupo = view.findViewById(R.id.nomeGrupo);
        descricao = view.findViewById(R.id.descricao);
        areaestudo = view.findViewById(R.id.areaestudo);
        adm = view.findViewById(R.id.adm);

        nomeGrupo.setText(grupo.getNome());
        descricao.setText(grupo.getDescricao());
        areaestudo.setText(grupo.getAreadeEstudo());
        adm.setText(grupo.getAdmin().getNome());

        searchEncontros();
        return view;
    }

    private void listarEncontros() {
        listaEncontros.setLayoutManager(new LinearLayoutManager(context));
        listaEncontros.setAdapter(new RecyclerViewEncontros(context, fragmentManager, encontrosList));
        listaEncontros.setNestedScrollingEnabled( false );
    }

    private void searchEncontros() {

    }

}