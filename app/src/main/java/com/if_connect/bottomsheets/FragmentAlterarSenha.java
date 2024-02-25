package com.if_connect.bottomsheets;

import static com.if_connect.utils.VerificaDados.validarEmail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.if_connect.R;

public class FragmentAlterarSenha extends Fragment {

    EditText email;
    FrameLayout btn_concluir;

    Context context;
    BottomSheetTelaInicial bottomSheetTelaInicial;

    public FragmentAlterarSenha(Context context, BottomSheetTelaInicial bottomSheetTelaInicial) {
        this.context = context;
        this.bottomSheetTelaInicial = bottomSheetTelaInicial;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alterar_senha, container, false);
        email = view.findViewById(R.id.email);
        btn_concluir = view.findViewById(R.id.btn_concluir);
        btn_concluir.setOnClickListener(view1 -> concluir());

        return view;
    }

    private void concluir() {
        if(validaCampo()){
            Toast.makeText(context, "Email de alteração da senha enviado", Toast.LENGTH_SHORT).show();
            bottomSheetTelaInicial.dismiss();
            //mandar email de alteração
        }
    }

    private boolean validaCampo(){
        String emailString = email.getText().toString();
        if(!validarEmail(emailString)){
            email.setError("Email inválido!");
            email.requestFocus();
            return false;
        }
        return true;
    }

}

