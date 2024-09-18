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
import com.if_connect.models.Grupo;

import java.util.Objects;

public class DialogGrupoAdm extends DialogGrupo {

    public DialogGrupoAdm(Grupo grupo, Context context, FragmentManager fragmentManager) {
        super(grupo, context, fragmentManager);
    }

    Button btnNovoEncontro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = Objects.requireNonNull(super.onCreateView(inflater, container, savedInstanceState));

        btnNovoEncontro = view.findViewById(R.id.btn_novoencontro);
        btnNovoEncontro.setVisibility(View.VISIBLE);

        btnNovoEncontro.setOnClickListener(v -> {
            new BottomSheetCriarEncontro(context, grupo, this, fragmentManager).show(fragmentManager,"tag");
        });
        return view;
    }
}
