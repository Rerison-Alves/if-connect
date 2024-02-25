package com.if_connect.bottomsheets;

import static com.if_connect.utils.VerificaDados.validarEmail;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.if_connect.models.Usuario;

public class FragmentLoginAluno extends Fragment {

    EditText email, senha;
    FrameLayout btn_concluir, btn_cadastro, btn_esqueciasenha;

    Context context;
    BottomSheetTelaInicial bottomSheetTelaInicial;

    public FragmentLoginAluno(Context context, BottomSheetTelaInicial bottomSheetTelaInicial) {
        this.context = context;
        this.bottomSheetTelaInicial = bottomSheetTelaInicial;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_aluno, container, false);

        email = view.findViewById(R.id.email);
        senha = view.findViewById(R.id.senha);

        btn_concluir = view.findViewById(R.id.btn_concluir);
        btn_concluir.setOnClickListener(view1 -> {
            if(verificadados()){
                login(null);
            }
        });

        btn_cadastro = view.findViewById(R.id.cadastro);
        btn_cadastro.setOnClickListener(view1 -> {
            //cadastro
            bottomSheetTelaInicial.replaceFragment(new FragmentCadastroAluno(context, bottomSheetTelaInicial));
        });

        btn_esqueciasenha = view.findViewById(R.id.esqueciasenha);
        btn_esqueciasenha.setOnClickListener(view1 -> {
            //esqueci senha
            bottomSheetTelaInicial.replaceFragment(new FragmentAlterarSenha(context, bottomSheetTelaInicial));
        });
        return view;
    }

    private void login(Usuario usuario) {

    }

    private boolean verificadados() {
        boolean result = true;

        String emailString = email.getText().toString().trim();
        String senhaString = senha.getText().toString().trim();

        if (TextUtils.isEmpty(emailString)) {
            email.setError("Campo obrigatório");
            email.requestFocus();
            result = false;
        }

        if (TextUtils.isEmpty(senhaString)) {
            senha.setError("Campo obrigatório");
            senha.requestFocus();
            result = false;
        }

        // Verificar se o email é válido
        if (!validarEmail(emailString)) {
            email.setError("Email inválido ou não pertence ao domínio IFCE");
            email.requestFocus();
            result = false;
        }

        return result;
    }
}
