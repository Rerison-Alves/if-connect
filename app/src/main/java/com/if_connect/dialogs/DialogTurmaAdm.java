package com.if_connect.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetCriarEncontro;
import com.if_connect.models.Turma;

import java.util.Objects;

public class DialogTurmaAdm extends DialogTurma{

    public DialogTurmaAdm(Turma turma, Context context, FragmentManager fragmentManager) {
        super(turma, context, fragmentManager);
    }

    Button btnNovoEncontro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = Objects.requireNonNull(super.onCreateView(inflater, container, savedInstanceState));

        btnNovoEncontro = view.findViewById(R.id.btn_novoencontro);
        btnNovoEncontro.setVisibility(View.VISIBLE);

        btnNovoEncontro.setOnClickListener(v -> {
            new BottomSheetCriarEncontro(context, turma, this, fragmentManager).show(fragmentManager,"tag");
        });
        return view;
    }
}
