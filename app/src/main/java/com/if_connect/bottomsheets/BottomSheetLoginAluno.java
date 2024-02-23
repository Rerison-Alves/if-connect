package com.if_connect.bottomsheets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.if_connect.models.Usuario;

public class BottomSheetLoginAluno extends BottomSheetDialogFragment {

    EditText email, senha;
    FrameLayout btn_concluir, btn_cadastro, btn_esqueciasenha;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_aluno, container, false);

        email = view.findViewById(R.id.email);
        senha = view.findViewById(R.id.senha);

        btn_concluir = view.findViewById(R.id.btn_concluir);
        btn_concluir.setOnClickListener(view13 -> {
            if(verificadados()){
                login(null);
            }
        });

        btn_cadastro = view.findViewById(R.id.cadastro);
        btn_cadastro.setOnClickListener(view1 -> {
            dismiss();
            //cadastro
        });

        btn_esqueciasenha = view.findViewById(R.id.esqueciasenha);
        btn_esqueciasenha.setOnClickListener(view12 -> {
            dismiss();
            //esqueci senha
        });
        return view;
    }

    private void login(Usuario usuario) {

    }

    private boolean verificadados() {
        boolean result = true;
        if(!(email.getText().toString().equals("")&&senha.getText().toString().equals(""))) {
//            try {
//                InternetAddress emailAddr = new InternetAddress(email.getText().toString());
//                emailAddr.validate();
//            } catch (AddressException ex) {
//                result = false;
//            }
        }else {
            Toast.makeText(getContext(), "Preencha os campos!", Toast.LENGTH_LONG).show();
            result = false;
        }
        return result;
    }
}
