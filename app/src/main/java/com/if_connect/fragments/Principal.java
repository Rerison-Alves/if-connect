package com.if_connect.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;
import com.if_connect.models.Grupo;

public class Principal extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam2;

    public Principal() {
        // Required empty public constructor
    }

    public static Principal newInstance(String param1, String param2) {
        Principal fragment = new Principal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
//    String[] testeRecentes= new String[] {"POO em aplicativos", "POO 2022", "Prog. Linear", "Nome de grupo", "Tô sem ideia"};
//    ArrayList<Grupo> listaTodos = new ArrayList<>();
//    ArrayList<Encontro> ListadeEncontros= new ArrayList<>();
    Context context;
//    RecyclerViewAdapterRecentes recyclerViewAdapterRecentes;
//    RecyclerViewAdapterEncontrosPrincipal recyclerViewAdapterEncontrosPrincipal;
//    RecyclerViewAdapterExtended recyclerViewAdapterExtended;
    RecyclerView todosView, encontrosView, recentesView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal, container, false);
        context= getContext();
        //Todos Grupos
        todosView = view.findViewById(R.id.gpstodosgrupos);
        todosView.setNestedScrollingEnabled( false );
        //Grupos Recentes
        recentesView = view.findViewById(R.id.gpsrecentes);

        //Próximos Encontros
        encontrosView = view.findViewById(R.id.gpsencontros);
        return view;
    }

    private void listarRecentes() {
//        recentesView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        recentesView.setAdapter(RecyclerViewAdapterRecentes(context, requireActivity().getSupportFragmentManager(), listaTodos));
    }

    private void listarTodos() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(new RecyclerViewAdapterExtended(context, requireActivity().getSupportFragmentManager(), listaTodos));
    }

    private void listarEncontros() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(new RecyclerViewAdapterEncontrosPrincipal(context, requireActivity().getSupportFragmentManager(), ListadeEncontros));
    }

}