package com.if_connect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.example.if_connect.R;


public class Pesquisar extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Pesquisar() {
        // Required empty public constructor
    }
    public static Pesquisar newInstance(String param1, String param2) {
        Pesquisar fragment = new Pesquisar();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FrameLayout comp, quim, engM;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);
        comp = view.findViewById(R.id.comp);
        quim = view.findViewById(R.id.quim);
        engM = view.findViewById(R.id.engM);

//        comp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CursoDialog cursoDialog=new CursoDialog("Ciência da Computação", getContext(),
//                        getActivity().getSupportFragmentManager(), R.style.Theme_ProjetoPAGE);
//                cursoDialog.show();
//            }
//        });
//        quim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CursoDialog cursoDialog=new CursoDialog("Química", getContext(),
//                        getActivity().getSupportFragmentManager(), R.style.Theme_ProjetoPAGE);
//                cursoDialog.show();
//            }
//        });
//        engM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CursoDialog cursoDialog=new CursoDialog("Engenharia Mecânica", getContext(),
//                        getActivity().getSupportFragmentManager(), R.style.Theme_ProjetoPAGE);
//                cursoDialog.show();
//            }
//        });
        return view;
    }
}