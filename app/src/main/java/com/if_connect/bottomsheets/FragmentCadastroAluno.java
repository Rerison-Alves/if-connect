package com.if_connect.bottomsheets;

import static com.if_connect.utils.CustomDatePicker.openDatePicker;
import static com.if_connect.utils.ErrorToast.toastError;
import static com.if_connect.utils.VerificaDados.validarEmail;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.if_connect.R;
import com.if_connect.enums.Role;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Usuario;
import com.if_connect.request.Generator;
import com.if_connect.request.auth.AuthAlunoService;
import com.if_connect.request.auth.AuthenticationResponse;
import com.if_connect.utils.DateEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCadastroAluno extends Fragment {

    AuthAlunoService authAlunoService;

    EditText nome, email, matricula, senha, repetesenha;
    DateEditText datanasc;
    CardView buttonDt;
    FrameLayout btn_concluir;
    Spinner curso;

    Calendar calendar = Calendar.getInstance();

    Context context;
    BottomSheetTelaInicial bottomSheetTelaInicial;

    public FragmentCadastroAluno(Context context, BottomSheetTelaInicial bottomSheetTelaInicial) {
        this.context = context;
        this.bottomSheetTelaInicial = bottomSheetTelaInicial;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_aluno, container, false);

        authAlunoService = Generator.getRetrofitInstance().create(AuthAlunoService.class);

        nome = view.findViewById(R.id.nomealuno);
        email = view.findViewById(R.id.email);
        matricula = view.findViewById(R.id.matricula);
        curso = view.findViewById(R.id.curso);
        datanasc = view.findViewById(R.id.datanasc);
        buttonDt = view.findViewById(R.id.button_dt);
        senha = view.findViewById(R.id.senha);
        repetesenha = view.findViewById(R.id.repetesenha);
        btn_concluir = view.findViewById(R.id.btn_concluir);

        buttonDt.setOnClickListener(view1 -> openDatePicker(calendar, datanasc, context, new Date(), null));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.cursos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        curso.setAdapter(adapter);

        btn_concluir.setOnClickListener(view1 -> {
            concluirCadastro();
        });
        return view;
    }

    private void concluirCadastro() {
        if(validarCampos()){

            Usuario usuario = new Usuario(
                    nome.getText().toString(),
                    email.getText().toString(),
                    datanasc.getDate(),
                    new Aluno(getCurso(), matricula.getText().toString()),
                    null,
                    Role.USER) {};
            usuario.password = senha.getText().toString();

            authAlunoService.registerUsuario(usuario).enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    if(response.isSuccessful()){
                        AuthenticationResponse authenticationResponse = response.body();
                        if (authenticationResponse!=null){
                            Toast.makeText(context, authenticationResponse.accessToken, Toast.LENGTH_SHORT).show();
                        }else {
                            toastError("erro: ", response, context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Curso getCurso() {
        return null;
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

        // Verificar se a data de nascimento é válida
        if(!validarDataNascimento(datanascString)){
            Toast.makeText(context, "Data de nascimento inválida", Toast.LENGTH_SHORT).show();
            valida = false;
        }

        // Se tudo estiver válido, retorna true
        return valida;
    }

    private boolean verificasenha(String senha) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(senha);
        return m.matches();
    }

    private boolean validarDataNascimento(String datanascString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false); // Não permite datas inválidas como 30/02/2023

        try {
            // Convertendo a string para um objeto Date
            Date dataNascimento = sdf.parse(datanascString);
            // Verificar se a data de nascimento é anterior à data atual
            Calendar calDataNascimento = Calendar.getInstance();
            if (dataNascimento != null) {
                calDataNascimento.setTime(dataNascimento);
            }
            Calendar calAtual = Calendar.getInstance();
            return calDataNascimento.before(calAtual);
        } catch (ParseException e) {
            // Se ocorrer uma exceção ao fazer o parsing, a data é inválida
            return false;
        }
    }

}
