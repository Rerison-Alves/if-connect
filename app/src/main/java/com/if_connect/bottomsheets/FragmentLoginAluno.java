package com.if_connect.bottomsheets;

import static com.if_connect.utils.VerificaDados.validarEmail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.if_connect.MainActivity;
import com.if_connect.request.Generator;
import com.if_connect.request.services.AuthUsuarioService;
import com.if_connect.request.requestbody.AuthenticationRequest;
import com.if_connect.request.requestbody.AuthenticationResponse;
import com.if_connect.utils.ErrorToast;
import com.if_connect.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLoginAluno extends Fragment {

    AuthUsuarioService authUsuarioService;

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

        authUsuarioService = Generator.getRetrofitInstance().create(AuthUsuarioService.class);

        email = view.findViewById(R.id.email);
        senha = view.findViewById(R.id.senha);

        btn_concluir = view.findViewById(R.id.btn_concluir);
        btn_concluir.setOnClickListener(view1 -> login());

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

    private void login() {
        if(verificadados()){
            authUsuarioService.authenticateUsuario(getAuthRequest()).enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    if(response.isSuccessful()){
                        AuthenticationResponse authenticationResponse = response.body();
                        if(authenticationResponse!=null){
                            Toast.makeText(context, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                            startMainActivity(authenticationResponse.accessToken, authenticationResponse.refreshToken);
                        }
                    }else {
                        ErrorToast.toastError("Erro ao fazer login: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private AuthenticationRequest getAuthRequest(){
        return new AuthenticationRequest(
                email.getText().toString(),
                senha.getText().toString());
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

    //Inicia tela principal
    private void startMainActivity(String token, String refreshToken) {
        TokenManager.getInstance(context).saveTokens(token, refreshToken);
        startActivity(new Intent(context, MainActivity.class));
        Activity activity = bottomSheetTelaInicial.getActivity();
        if(activity!=null){
            activity.finish();
        }
    }
}
