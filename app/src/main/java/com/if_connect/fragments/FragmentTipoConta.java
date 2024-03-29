package com.if_connect.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetShape;

public class FragmentTipoConta extends Fragment {

    Context context;
    BottomSheetShape bottomSheetTelaInicial;
    FragmentManager fragmentManager;

    FrameLayout btn_cadastroAluno,
                btn_loginAluno,
                btn_cadastroProfessor,
                btn_loginProfessor;

    public FragmentTipoConta(Context context, BottomSheetShape bottomSheetTelaInicial, FragmentManager fragmentManager) {
        this.context = context;
        this.bottomSheetTelaInicial = bottomSheetTelaInicial;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tipo_conta, container, false);

        btn_cadastroAluno = view.findViewById(R.id.btn_cadastro_aluno);
        btn_cadastroAluno.setOnClickListener(view12 ->
                bottomSheetTelaInicial.replaceFragment(new FragmentCadastroAluno(context, bottomSheetTelaInicial, fragmentManager)));

        btn_loginAluno = view.findViewById(R.id.btn_login_aluno);
        btn_loginAluno.setOnClickListener(view1 ->
                bottomSheetTelaInicial.replaceFragment(new FragmentLoginAluno(context, bottomSheetTelaInicial, fragmentManager)));

        btn_cadastroProfessor = view.findViewById(R.id.btn_cadastro_professor);
        btn_cadastroProfessor.setOnClickListener(view12 ->
                bottomSheetTelaInicial.replaceFragment(new FragmentCadastroProfessor(context, bottomSheetTelaInicial, fragmentManager)));

        btn_loginProfessor = view.findViewById(R.id.btn_login_professor);
        btn_loginProfessor.setOnClickListener(view1 ->
                bottomSheetTelaInicial.replaceFragment(new FragmentLoginProfessor(context, bottomSheetTelaInicial, fragmentManager)));
        return view;
    }
}
