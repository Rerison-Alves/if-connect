package com.if_connect.fragments;

import static com.if_connect.utils.VerificaDados.validarEmail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.MainActivity;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.AuthenticationRequest;
import com.if_connect.request.requestbody.AuthenticationResponse;
import com.if_connect.request.services.AuthService;
import com.if_connect.utils.ErrorManager;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLoginProfessor extends Fragment {

    AuthService authUsuarioService;

    EditText email, senha;
    FrameLayout btnCadastro, btnEsqueciSenha;
    Button btnConcluir;
    ProgressBar progressBar;

    Context context;
    BottomSheetShape bottomSheetShape;
    FragmentManager fragmentManager;

    public FragmentLoginProfessor(Context context, BottomSheetShape bottomSheetShape, FragmentManager fragmentManager) {
        this.context = context;
        this.bottomSheetShape = bottomSheetShape;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_professor, container, false);

        authUsuarioService = Generator.getRetrofitInstance().create(AuthService.class);

        email = view.findViewById(R.id.email);
        senha = view.findViewById(R.id.senha);
        btnCadastro = view.findViewById(R.id.cadastro);
        btnEsqueciSenha = view.findViewById(R.id.esqueciasenha);
        btnConcluir = view.findViewById(R.id.btn_concluir);
        progressBar = view.findViewById(R.id.progress_bar);
        btnConcluir.setOnClickListener(view1 -> login());

        btnCadastro.setOnClickListener(view1 -> {
            //cadastro
            bottomSheetShape.replaceFragment(new FragmentCadastroProfessor(context, bottomSheetShape, fragmentManager));
        });

        btnEsqueciSenha.setOnClickListener(view1 -> {
            //esqueci senha
            bottomSheetShape.replaceFragment(new FragmentCPEmail(context, bottomSheetShape, fragmentManager));
        });
        return view;
    }

    private void login() {
        if (verificadados()) {
            isLoading(true);
            authUsuarioService.authenticate(getAuthRequest()).enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    isLoading(false);
                    if (response.isSuccessful()) {
                        AuthenticationResponse authenticationResponse = response.body();
                        if (authenticationResponse!=null) {
                            Toast.makeText(context, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                            startMainActivity(authenticationResponse.accessToken, authenticationResponse.refreshToken);
                        }
                    }else {
                        ErrorManager.showError("Erro ao fazer login: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    isLoading(false);
                    Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void isLoading(boolean isLoading) {
        btnConcluir.setEnabled(!isLoading);
        btnConcluir.setAlpha(isLoading?0.5f:1.0f);
        progressBar.setVisibility(isLoading?View.VISIBLE:View.INVISIBLE);
    }

    private AuthenticationRequest getAuthRequest() {
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
        UsuarioManager.requestUsuario(token, context);
        startActivity(new Intent(context, MainActivity.class));
        Activity activity = bottomSheetShape.getActivity();
        if (activity!=null) {
            activity.finish();
        }
    }
}
