package com.if_connect.bottomsheets;

import static com.if_connect.utils.CustomDatePicker.openDatePicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Usuario;
import com.if_connect.utils.MaskWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BottomSheetCadastroAluno extends BottomSheetDialogFragment {

    EditText nome, email, matricula, senha, repetesenha, datanasc;
    FloatingActionButton buttonDt;
    FrameLayout btn_concluir;
    Spinner curso;

    Calendar calendar = Calendar.getInstance();

    Context context;

    public BottomSheetCadastroAluno(Context context) {
        this.context = context;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_cadastro_aluno, container, false);
        nome = view.findViewById(R.id.nomealuno);
        email = view.findViewById(R.id.email);
        matricula = view.findViewById(R.id.matricula);
        curso = view.findViewById(R.id.curso);
        datanasc = view.findViewById(R.id.datanasc);
        buttonDt = view.findViewById(R.id.button_dt);
        senha = view.findViewById(R.id.senha);
        repetesenha = view.findViewById(R.id.repetesenha);
        btn_concluir = view.findViewById(R.id.btn_concluir);

        datanasc.addTextChangedListener(new MaskWatcher(datanasc, "##/##/####"));
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
        if(verificadados()){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
            Date data=null;
            try {
                data=formata.parse(String.valueOf(datanasc.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Usuario usuario = new Usuario(
                    nome.getText().toString(),
                    email.getText().toString(),
                    data,
                    new Aluno(getCurso(), matricula.getText().toString()),
                    null) {
            };
            cadastrarAluno(usuario);
        }
    }

    private Curso getCurso() {
        return null;
    }

    private void cadastrarAluno(Usuario usuario){
        // cadastro
    }

    private boolean verificadados() {
        boolean verifica = true;
        if(nome.getText().toString().equals("")||email.getText().toString().equals("")||
                matricula.getText().toString().equals("")||curso.getSelectedItem().toString().equals("Indefinido")||
                datanasc.getText().toString().equals("")||senha.getText().toString().equals("")||
                repetesenha.getText().toString().equals("")){
            verifica = false;
            Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        }else {
            if(!isValidEmailAddress(email.getText().toString())){
                verifica = false;
                Toast.makeText(getContext(), "Email inválido!", Toast.LENGTH_SHORT).show();
            }
            if(matricula.getText().toString().length()<14){
                verifica = false;
                Toast.makeText(getContext(), "Matrícula inválida!", Toast.LENGTH_SHORT).show();
            }
            Date dataatual=new Date();
            Date datateste= null, datamin=null;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
            try {
                datateste=formata.parse(datanasc.getText().toString());
                datamin=formata.parse("01/01/1900");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dataatual.before(datateste)||dataatual.equals(datateste)||datamin.after(datateste)) {
                verifica=false;
                Toast.makeText(getContext(), "Data é inválida!", Toast.LENGTH_SHORT).show();
            }
            if (!verificasenha()){
                verifica=false;
                Toast.makeText(getContext(), "Senha é insegura!\nDeve conter: números, letras maiúsculas\ne minusculas e ao menos 8 caracteres",
                        Toast.LENGTH_LONG).show();
            }
            String tsenha = senha.getText().toString();
            String trepsenha = repetesenha.getText().toString();
            if(!(tsenha.equals(trepsenha))){
                verifica=false;
                Toast.makeText(getContext(), "Senhas não coincidem!", Toast.LENGTH_SHORT).show();
            }
        }
        return verifica;
    }

    private boolean verificasenha() {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(senha.getText().toString());
        return m.matches();
    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
//        try {
//            InternetAddress emailAddr = new InternetAddress(email);
//            emailAddr.validate();
//        } catch (AddressException ex) {
//            result = false;
//        }
        return result;
    }

}
