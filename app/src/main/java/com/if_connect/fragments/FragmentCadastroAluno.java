package com.if_connect.fragments;

import static com.if_connect.utils.CustomDatePicker.openDatePicker;
import static com.if_connect.utils.ErrorManager.showError;
import static com.if_connect.utils.VerificaDados.validarDataNascimento;
import static com.if_connect.utils.VerificaDados.validarEmail;
import static com.if_connect.utils.VerificaDados.verificasenha;
import static com.if_connect.utils.typeadapters.SpinnerAdapter.getAdapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.dialogs.AlertDialogManager;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.enums.Role;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.RegisterRequest;
import com.if_connect.request.services.AuthService;
import com.if_connect.request.services.CursoService;
import com.if_connect.utils.DateEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCadastroAluno extends Fragment {

    AuthService authUsuarioService;
    CursoService cursoService;

    EditText nome, email, matricula, senha, repetesenha;
    DateEditText datanasc;
    CardView buttonDt;
    Button btnConcluir;
    ProgressBar progressBar;
    Spinner curso;

    Calendar calendar = Calendar.getInstance();

    List<Curso> cursosList;
    String[] nomeCursos = new String[]{};

    Context context;
    BottomSheetShape bottomSheetTelaInicial;
    FragmentManager fragmentManager;

    public FragmentCadastroAluno(Context context, BottomSheetShape bottomSheetTelaInicial, FragmentManager fragmentManager) {
        this.context = context;
        this.bottomSheetTelaInicial = bottomSheetTelaInicial;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_aluno, container, false);

        authUsuarioService = Generator.getRetrofitInstance().create(AuthService.class);
        cursoService = Generator.getRetrofitInstance().create(CursoService.class);

        nome = view.findViewById(R.id.nomealuno);
        email = view.findViewById(R.id.email);
        matricula = view.findViewById(R.id.matricula);
        curso = view.findViewById(R.id.curso);
        datanasc = view.findViewById(R.id.datanasc);
        buttonDt = view.findViewById(R.id.button_dt);
        senha = view.findViewById(R.id.senha);
        repetesenha = view.findViewById(R.id.repetesenha);
        btnConcluir = view.findViewById(R.id.btn_concluir);
        progressBar = view.findViewById(R.id.progress_bar);

        buttonDt.setOnClickListener(view1 -> openDatePicker(calendar, datanasc, context, new Date(), null));

        requestCursos();

        btnConcluir.setOnClickListener(view1 -> {
            concluirCadastro();
        });
        return view;
    }

    private void requestCursos() {
        cursoService.getAllCursos().enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if (response.isSuccessful()) {
                    cursosList = response.body();
                    if (cursosList!=null && !cursosList.isEmpty()) {
                        nomeCursos = Stream.concat(
                                        Stream.of("Indefinido"),
                                        cursosList.stream().map(Curso::getDescricao))
                                        .toArray(String[]::new);
                        curso.setAdapter(getAdapter(nomeCursos, context));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {

            }
        });
    }

    private void concluirCadastro() {
        if (validarCampos()) {
            isLoading(true);
            authUsuarioService.register(getUsuario()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        isLoading(false);
                        ResponseBody authenticationResponse = response.body();
                        if (authenticationResponse!=null) {
                            new AlertDialogManager(
                                    context,
                                    "Sucesso!",
                                    "Conta criada com sucesso! Enviamos um e-mail para "+
                                            email.getText().toString()+ " com o link de ativação. " +
                                            "Após isso você poderá fazer login!")
                                    .show();
                            bottomSheetTelaInicial.replaceFragment(new FragmentLoginAluno(context, bottomSheetTelaInicial, fragmentManager));
                        }
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

    private void isLoading(boolean isLoading) {
        btnConcluir.setEnabled(!isLoading);
        btnConcluir.setAlpha(isLoading?0.5f:1.0f);
        progressBar.setVisibility(isLoading?View.VISIBLE:View.INVISIBLE);
    }

    private RegisterRequest getUsuario() {
        return new RegisterRequest(
                nome.getText().toString(),
                email.getText().toString(),
                senha.getText().toString(),
                datanasc.getDate(),
                new Aluno(getCurso(), matricula.getText().toString()),
                null,
                Role.USER) {};
    }

    private Curso getCurso() {
        if (cursosList==null || cursosList.isEmpty()) {
            return null;
        }else {
            return cursosList.get((int)curso.getSelectedItemId()-1);
        }
    }


    public boolean validarCampos() {

        boolean valida = true;

        // Obter os valores dos campos
        String nomeString = nome.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String matriculaString = matricula.getText().toString().trim();
        String senhaString = senha.getText().toString().trim();
        String repetesenhaString = repetesenha.getText().toString().trim();
        String datanascString = datanasc.getText();

        // Verificar se algum campo está vazio
        if (TextUtils.isEmpty(nomeString)) {
            nome.setError("Campo obrigatório");
            nome.requestFocus();
            valida = false;
        }
        if (TextUtils.isEmpty(emailString)) {
            email.setError("Campo obrigatório");
            email.requestFocus();
            valida = false;
        }
        if (TextUtils.isEmpty(matriculaString)) {
            matricula.setError("Campo obrigatório");
            matricula.requestFocus();
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

        // Verificar se o email é válido
        if (!validarEmail(emailString)) {
            email.setError("Email inválido ou não pertence ao domínio IFCE");
            email.requestFocus();
            valida = false;
        }

        if (!verificasenha(senhaString)) {
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

        // Verificar se a data de nascimento é válida
        if (!validarDataNascimento(datanascString)) {
            Toast.makeText(context, "Data de nascimento inválida", Toast.LENGTH_SHORT).show();
            valida = false;
        }

        if (curso.getSelectedItemId()==0) {
            Toast.makeText(context, "Curso é obrigatório!", Toast.LENGTH_SHORT).show();
            curso.requestFocus();
        }

        // Se tudo estiver válido, retorna true
        return valida;
    }

}
