package com.if_connect.bottomsheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetTelaInicial extends BottomSheetDialogFragment {

    Context context;
    FragmentManager fragmentManager;

    public BottomSheetTelaInicial(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    FrameLayout btn_cadastroaluno, btn_loginaluno;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_tela_inicial, container, false);
        btn_cadastroaluno = view.findViewById(R.id.btn_cadastroaluno);
        btn_cadastroaluno.setOnClickListener(view12 -> {
            dismiss();
            new BottomSheetCadastroAluno(context).show(fragmentManager, "tag");
        });
        btn_loginaluno = view.findViewById(R.id.btn_loginaluno);
        btn_loginaluno.setOnClickListener(view1 -> {
            dismiss();
            //login aluno
        });
        return view;
    }

}
