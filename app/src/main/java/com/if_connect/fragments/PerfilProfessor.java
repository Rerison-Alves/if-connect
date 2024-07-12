package com.if_connect.fragments;

import static com.if_connect.utils.ErrorManager.showError;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if_connect.MainActivity;
import com.if_connect.bottomsheets.BottomSheetCriarTurma;
import com.if_connect.dialogs.DialogConfig;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Professor;
import com.if_connect.models.Turma;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewPerfilProfessor;
import com.if_connect.request.Generator;
import com.if_connect.request.services.TurmaService;
import com.if_connect.request.services.UsuarioService;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilProfessor extends Fragment {

    FragmentManager fragmentManager;
    String token;
    UsuarioService usuarioService;
    TurmaService turmaService;
    Context context;

    Button buttonCriarAgrupamento;
    List<Turma> turmasList = new ArrayList<>();
    TextView nomeUsuario, siapeUsuario;
    FloatingActionButton config;
    RecyclerView recycleTurmasDoProfessor;
    Usuario usuarioLogado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_professor, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        token = TokenManager.getInstance(context).getAccessToken();
        turmaService = Generator.getRetrofitInstance().create(TurmaService.class);

        nomeUsuario = view.findViewById(R.id.nome_usuario);
        siapeUsuario = view.findViewById(R.id.siape);
        config= view.findViewById(R.id.config);
        buttonCriarAgrupamento = view.findViewById(R.id.btn_novoagrupamento);
        recycleTurmasDoProfessor = view.findViewById(R.id.turmas_professor);

        config.setOnClickListener(v -> {
            new DialogConfig(context, (MainActivity) getActivity(), fragmentManager).show(fragmentManager,"tag");
        });
        buttonCriarAgrupamento.setOnClickListener(v -> {
            new BottomSheetCriarTurma(context, usuarioLogado, fragmentManager, this).show(fragmentManager, "tag");
        });

        usuarioLogado = UsuarioManager.getUsuarioLogado();
        setProfessor();
        return view;
    }

    private void setProfessor() {
        nomeUsuario.setText(usuarioLogado.getNome());
        String siape = Optional.ofNullable(usuarioLogado.getProfessor())
                .map(Professor::getSiape)
                .orElse("N/a");
        siapeUsuario.setText(siape);
    }

    void getTurmas(){
        turmaService.getTurmasByAdmin(usuarioLogado.getId(), token).enqueue(new Callback<List<Turma>>() {
            @Override
            public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
                if(response.isSuccessful()){
                    turmasList = response.body();
                    if(turmasList!=null){
                        listarTurmas();
                    }else {
                        showError("Não foi possível carregar grupos do usuário: ", response, context);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Turma>> call, Throwable t) {

            }
        });
    }

    private void listarTurmas() {
        recycleTurmasDoProfessor.setLayoutManager(new LinearLayoutManager(context));
        recycleTurmasDoProfessor.setAdapter(new RecyclerViewPerfilProfessor(turmasList, this, context, fragmentManager, turmaService, token, usuarioLogado));
        recycleTurmasDoProfessor.setNestedScrollingEnabled(false);
    }
}
