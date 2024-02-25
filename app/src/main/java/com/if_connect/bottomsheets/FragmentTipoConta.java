package com.if_connect.bottomsheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.if_connect.R;

public class FragmentTipoConta extends Fragment {

    Context context;
    BottomSheetTelaInicial bottomSheetTelaInicial;

    FrameLayout btn_cadastroaluno,
                btn_loginaluno;

    public FragmentTipoConta(Context context, BottomSheetTelaInicial bottomSheetTelaInicial) {
        this.context = context;
        this.bottomSheetTelaInicial = bottomSheetTelaInicial;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tipo_conta, container, false);

        btn_cadastroaluno = view.findViewById(R.id.btn_cadastroaluno);
        btn_cadastroaluno.setOnClickListener(view12 ->
                bottomSheetTelaInicial.replaceFragment(new FragmentCadastroAluno(context, bottomSheetTelaInicial)));

        btn_loginaluno = view.findViewById(R.id.btn_loginaluno);
        btn_loginaluno.setOnClickListener(view1 ->
                bottomSheetTelaInicial.replaceFragment(new FragmentLoginAluno(context, bottomSheetTelaInicial)));
        return view;
    }
}
