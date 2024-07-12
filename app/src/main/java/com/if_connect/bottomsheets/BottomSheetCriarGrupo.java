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
import com.if_connect.fragments.PerfilAluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Grupo;
import com.if_connect.models.Usuario;
import com.if_connect.request.Generator;
import com.if_connect.request.services.CursoService;
import com.if_connect.request.services.GrupoService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetCriarGrupo extends BottomSheetDialogFragment {

    Context context;
    public Usuario admin;
    FragmentManager fragmentManager;
    PerfilAluno perfilAluno;
    GrupoService grupoService;
    CursoService cursoService;
    String token;

    public BottomSheetCriarGrupo(Context context, Usuario admin, FragmentManager fragmentManager, PerfilAluno perfilAluno) {
        this.context = context;
        this.admin = admin;
        this.fragmentManager = fragmentManager;
        this.perfilAluno = perfilAluno;
    }

    EditText nomeDoGrupo, areaDeEstudo, descricao;
    TextView counterConvidados;

    Spinner spinnerCursos;
    List<Curso> cursosList;
    String[] nomeCursos = new String[]{};

    List<Usuario> convidadosList=new ArrayList<>();
    FrameLayout btnConvidaAlunos, btnConcluir;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_criar_grupo, container, false);
        token = TokenManager.getInstance(context).getAccessToken();
        grupoService = Generator.getRetrofitInstance().create(GrupoService.class);
        cursoService = Generator.getRetrofitInstance().create(CursoService.class);

        nomeDoGrupo = view.findViewById(R.id.nomegrupo);
        areaDeEstudo = view.findViewById(R.id.areadeestudo);
        descricao = view.findViewById(R.id.descricao);
        btnConvidaAlunos = view.findViewById(R.id.btn_convidaalunos);
        counterConvidados = view.findViewById(R.id.nconvidados);
        spinnerCursos = view.findViewById(R.id.spinner);
        btnConcluir = view.findViewById(R.id.btn_concluir);

        btnConvidaAlunos.setOnClickListener(view1 ->
                new DialogConvidaUsuario(context, convidadosList, this)
                        .show(fragmentManager, "tag"));

        requestCursos();

        btnConcluir.setOnClickListener(v -> salvarGrupo());
        return view;
    }

    public void salvarGrupo(){
        if(validarCampos()){
            grupoService.createGrupo(getGrupo(), token).enqueue(new Callback<Grupo>() {
                @Override
                public void onResponse(Call<Grupo> call, Response<Grupo> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, "Grupo criado com sucesso!", Toast.LENGTH_SHORT).show();
                        perfilAluno.listarGrupos();
                        dismiss();
                    }else {
                        showError("Erro ao criar grupo: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<Grupo> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    public Grupo getGrupo() {
        return new Grupo(
                admin,
                nomeDoGrupo.getText().toString(),
                descricao.getText().toString(),
                getCurso(),
                areaDeEstudo.getText().toString(),
                convidadosList);
    }

    private Curso getCurso() {
        return cursosList.get((int)spinnerCursos.getSelectedItemId()-1);
    }

    void setCurso(){
        if(this instanceof BottomSheetEditarGrupo){
            spinnerCursos.setSelection(cursosList.indexOf(
                    ((BottomSheetEditarGrupo)this).grupoOld.getCurso())+1);
        }
    }

    public boolean validarCampos(){
        boolean valida = true;
        String nomeDoGrupoString = nomeDoGrupo.getText().toString();
        String areadeestudoString = areaDeEstudo.getText().toString();

        if (TextUtils.isEmpty(nomeDoGrupoString)) {
            nomeDoGrupo.setError("Campo obrigatório");
            nomeDoGrupo.requestFocus();
            valida = false;
        }
        if (TextUtils.isEmpty(areadeestudoString)) {
            areaDeEstudo.setError("Campo obrigatório");
            areaDeEstudo.requestFocus();
            valida = false;
        }
        if(spinnerCursos.getSelectedItemId()==0){
            Toast.makeText(context, "Selecione um curso!", Toast.LENGTH_LONG).show();
            spinnerCursos.requestFocus();
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