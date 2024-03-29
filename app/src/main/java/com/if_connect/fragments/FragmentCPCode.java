package com.if_connect.fragments;

import static com.if_connect.utils.CustomDatePicker.openDatePicker;
import static com.if_connect.utils.ErrorManager.showError;
import static com.if_connect.utils.VerificaDados.verificasenha;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.dialogs.AlertDialogManager;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.ChangePasswordRequest;
import com.if_connect.request.services.UsuarioService;
import com.jakode.verifycodeedittext.VerifyCodeEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCPCode extends Fragment {

    UsuarioService usuarioService;

    Context context;
    BottomSheetShape bottomSheetShape;
    FragmentManager fragmentManager;
    String email;

    public FragmentCPCode(Context context, BottomSheetShape bottomSheetShape, FragmentManager fragmentManager, String email) {
        this.context = context;
        this.bottomSheetShape = bottomSheetShape;
        this.fragmentManager = fragmentManager;
        this.email = email;
    }

    ImageView voltar;
    EditText senha, repetesenha;
    TextView emailText, counter;
    VerifyCodeEditText code;
    FrameLayout reenvio;
    Button btnConcluir;
    ProgressBar progressBar;

    private CountDownTimer countDownTimer;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cp_code, container, false);

        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);

        voltar = view.findViewById(R.id.voltar);
        senha = view.findViewById(R.id.senha);
        emailText = view.findViewById(R.id.email);
        counter = view.findViewById(R.id.counter);
        code = view.findViewById(R.id.code);
        reenvio = view.findViewById(R.id.reenvio);
        repetesenha = view.findViewById(R.id.repetesenha);
        btnConcluir = view.findViewById(R.id.btn_concluir);
        progressBar = view.findViewById(R.id.progress_bar);

        emailText.setText(email);
        startCountDownTimer();

        voltar.setOnClickListener(v -> {
            bottomSheetShape.replaceFragment(new FragmentCPEmail(context, bottomSheetShape, fragmentManager));
        });

        reenvio.setOnClickListener(v -> {
            reenviarEmail();
        });

        btnConcluir.setOnClickListener(view1 -> {
            alterarSenha();
        });
        return view;
    }

    private void alterarSenha() {
        if(validarCampos()){
            isLoading(true);
            usuarioService.changePassword(getChangePasswordRequest()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        isLoading(false);
                        new AlertDialogManager(
                                context,
                                "Sucesso!",
                                "Sua senha foi alterada.")
                                .show();
                        bottomSheetShape.dismiss();
                    }else {
                        showError("Não foi possível efetuar cadastro: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isLoading(false);
                    Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void isLoading(boolean isLoading){
        btnConcluir.setEnabled(!isLoading);
        btnConcluir.setAlpha(isLoading?0.5f:1.0f);
        progressBar.setVisibility(isLoading?View.VISIBLE:View.INVISIBLE);
    }

    private ChangePasswordRequest getChangePasswordRequest() {
        return new ChangePasswordRequest(
                Integer.valueOf(code.getText()),
                email,
                senha.getText().toString(),
                repetesenha.getText().toString());
    }

    public boolean validarCampos() {

        boolean valida = true;

        // Obter os valores dos campos
        String codeString = code.getText();
        String senhaString = senha.getText().toString().trim();
        String repetesenhaString = repetesenha.getText().toString().trim();

        // Verificar se algum campo está vazio
        if (TextUtils.isEmpty(codeString)) {
            Toast.makeText(context, "Código é obrigatório!", Toast.LENGTH_SHORT).show();
            code.setCodeItemErrorLineDrawable();
            code.requestFocus();
            valida = false;
        }
        if (TextUtils.isEmpty(senhaString)) {
            senha.setError("Campo obrigatório");
            senha.requestFocus();
            valida = false;
        }
        if (TextUtils.isEmpty(repetesenhaString)) {
            repetesenha.setError("Campo obrigatório");
            repetesenha.requestFocus();
            valida = false;
        }

        // Verificar se código possue tamanho certo
        if (codeString.length()!=5) {
            Toast.makeText(context, "Código está inválido!", Toast.LENGTH_SHORT).show();
            code.setCodeItemErrorLineDrawable();
            emailText.requestFocus();
            valida = false;
        }

        if(!verificasenha(senhaString)){
            senha.setError("Senha inválida, deve conter 8 caracteres, 1 letra maiúscula, 1 letra ");
            senha.requestFocus();
            valida = false;
        }
        // Verificar se as senhas coincidem
        if (!repetesenhaString.equals(senhaString)) {
            repetesenha.setError("Senhas não coincidem");
            repetesenha.requestFocus();
            valida = false;
        }

        // Se tudo estiver válido, retorna true
        return valida;
    }

    private void reenviarEmail(){
        startCountDownTimer();
        usuarioService.changePasswordCode(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    showError("Não foi possível reenviar email: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountDownTimer() {
        reenvio.setClickable(false);
        reenvio.setAlpha(0.5f);
        // Definir 30 segundos em milissegundos
        final long milliseconds = 30 * 1000;

        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Atualizar o EditText com o tempo restante formatado como mm:ss
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                counter.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                // Quando o contador termina, desbloquear o FrameLayout
                reenvio.setClickable(true);
                reenvio.setAlpha(1.0f);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Certifique-se de parar o contador quando a atividade é destruída para evitar vazamento de memória
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
