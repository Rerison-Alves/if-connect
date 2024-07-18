package com.if_connect.bottomsheets;

import static com.if_connect.utils.ErrorManager.showError;
import static com.if_connect.utils.typeadapters.SpinnerAdapter.getAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.if_connect.dialogs.DialogConvidaUsuario;
import com.if_connect.fragments.PerfilProfessor;
import com.if_connect.models.Curso;
import com.if_connect.models.Turma;
import com.if_connect.models.Usuario;
import com.if_connect.models.enums.TipoAgrupamento;
import com.if_connect.models.enums.Turno;
import com.if_connect.request.Generator;
import com.if_connect.request.services.CursoService;
import com.if_connect.request.services.TurmaService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetCriarTurma extends BottomSheetDialogFragment {

    Context context;
    public Usuario admin;
    FragmentManager fragmentManager;
    PerfilProfessor perfilProfessor;
    TurmaService turmaService;
    CursoService cursoService;
    String token;

    public BottomSheetCriarTurma(Context context, Usuario admin, FragmentManager fragmentManager, PerfilProfessor perfilProfessor) {
        this.context = context;
        this.admin = admin;
        this.fragmentManager = fragmentManager;
        this.perfilProfessor = perfilProfessor;
    }

    EditText nomeTurma, disciplina, descricao;
    TextView counterConvidados;

    Spinner spinnerCursos;
    Spinner spinnerTurno;
    List<Curso> cursosList;
    String[] nomeCursos = new String[]{};

    List<Usuario> convidadosList=new ArrayList<>();
    FrameLayout btnConvidaAlunos, btnConcluir;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_criar_turma, container, false);
        token = TokenManager.getInstance(context).getAccessToken();
        turmaService = Generator.getRetrofitInstance().create(TurmaService.class);
        cursoService = Generator.getRetrofitInstance().create(CursoService.class);

        nomeTurma = view.findViewById(R.id.nome_turma);
        disciplina = view.findViewById(R.id.disciplina);
        descricao = view.findViewById(R.id.descricao);
        btnConvidaAlunos = view.findViewById(R.id.btn_convidaalunos);
        counterConvidados = view.findViewById(R.id.nconvidados);
        spinnerCursos = view.findViewById(R.id.curso);
        spinnerTurno = view.findViewById(R.id.turno);
        btnConcluir = view.findViewById(R.id.btn_concluir);

        spinnerTurno.setAdapter(getAdapter(Turno.getDescricoes(), context));

        btnConvidaAlunos.setOnClickListener(view1 ->
                new DialogConvidaUsuario(context, convidadosList, this)
                        .show(fragmentManager, "tag"));

        requestCursos();

        btnConcluir.setOnClickListener(v -> salvarTurma());
        return view;
    }

    public void salvarTurma(){
        if(validarCampos()){
            turmaService.createTurma(getTurma(), token).enqueue(new Callback<Turma>() {
                @Override
                public void onResponse(Call<Turma> call, Response<Turma> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, "Turma criada com sucesso!", Toast.LENGTH_SHORT).show();
                        //perfil.listarGrupos();
                        dismiss();
                    }else {
                        showError("Erro ao criar turma: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<Turma> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public Turma getTurma(){
        return new Turma(admin,
                nomeTurma.getText().toString(),
                descricao.getText().toString(),
                getCurso(),
                TipoAgrupamento.TURMA,
                disciplina.getText().toString(),
                getTurno(),
                convidadosList);
    }

    @SuppressLint("DefaultLocale")
    public void changeCounter(int size){
        counterConvidados.setText(String.format("%d/50", size));
    }

    private void requestCursos() {
        cursoService.getAllCursos().enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if(response.isSuccessful()){
                    cursosList = response.body();
                    if(cursosList!=null && !cursosList.isEmpty()){
                        nomeCursos = Stream.concat(
                                        Stream.of("Indefinido"),
                                        cursosList.stream().map(Curso::getDescricao))
                                .toArray(String[]::new);
                        spinnerCursos.setAdapter(getAdapter(nomeCursos, context));
                        setCurso();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {

            }
        });
    }

    private Curso getCurso() {
        return cursosList.get((int)spinnerCursos.getSelectedItemId()-1);
    }

    private Turno getTurno(){
        return Turno.valueOf(spinnerTurno.getSelectedItem().toString());
    }

    void setCurso(){
        if(this instanceof BottomSheetEditarTurma){
            spinnerCursos.setSelection(cursosList.indexOf(
                    ((BottomSheetEditarTurma)this).turmaOld.getCurso())+1);
        }
    }

    public boolean validarCampos(){
        boolean valida = true;
        String nomeTurmaString = nomeTurma.getText().toString();
        String disciplinaString = disciplina.getText().toString();

        if (TextUtils.isEmpty(nomeTurmaString)) {
            disciplina.setError("Campo obrigatório");
            disciplina.requestFocus();
            valida = false;
        }
        if (TextUtils.isEmpty(disciplinaString)) {
            disciplina.setError("Campo obrigatório");
            disciplina.requestFocus();
            valida = false;
        }
        if(spinnerCursos.getSelectedItemId()==0){
            Toast.makeText(context, "Selecione um curso!", Toast.LENGTH_LONG).show();
            spinnerCursos.requestFocus();
            valida = false;
        }
        if(spinnerTurno.getSelectedItemId()==0){
            Toast.makeText(context, "Selecione um turno!", Toast.LENGTH_LONG).show();
            spinnerTurno.requestFocus();
            valida = false;
        }
        if(convidadosList.isEmpty()){
            Toast.makeText(context, "Convide ao menos um usuário!", Toast.LENGTH_LONG).show();
            btnConvidaAlunos.requestFocus();
            valida = false;
        }
        return valida;
    }
}
