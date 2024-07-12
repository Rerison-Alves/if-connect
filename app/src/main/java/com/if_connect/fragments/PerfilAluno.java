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
import com.if_connect.bottomsheets.BottomSheetCriarGrupo;
import com.if_connect.dialogs.DialogConfig;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Grupo;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewPerfilAluno;
import com.if_connect.request.Generator;
import com.if_connect.request.services.GrupoService;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilAluno extends Fragment {

    FragmentManager fragmentManager;
    String token;
    GrupoService grupoService;
    Context context;

    Button buttonCriarAgrupamento;
    List<Grupo> gruposList = new ArrayList<>();
    FloatingActionButton config;
    TextView nomeUsuario, cursoUsuario;
    RecyclerView recycleGruposDoUsuario;
    Usuario usuarioLogado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_aluno, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        token = TokenManager.getInstance(context).getAccessToken();
        grupoService = Generator.getRetrofitInstance().create(GrupoService.class);

        nomeUsuario = view.findViewById(R.id.nomeusuariologado);
        cursoUsuario = view.findViewById(R.id.cursousuariologado);
        config= view.findViewById(R.id.config);
        buttonCriarAgrupamento = view.findViewById(R.id.btn_novoagrupamento);
        recycleGruposDoUsuario = view.findViewById(R.id.gpsusuario);

        config.setOnClickListener(v -> {
            new DialogConfig(context, (MainActivity) getActivity(), fragmentManager).show(fragmentManager,"tag");
        });
        buttonCriarAgrupamento.setOnClickListener(v -> {
            new BottomSheetCriarGrupo(context, usuarioLogado, fragmentManager, this).show(fragmentManager,"TAG");
        });

        usuarioLogado = UsuarioManager.getUsuarioLogado();
        setAluno();
        getGrupos();
        return view;
    }

    private void setAluno() {
        nomeUsuario.setText(usuarioLogado.getNome());
        String nomeCurso = Optional.ofNullable(usuarioLogado.getAluno())
                .map(Aluno::getCurso)
                .map(Curso::getDescricao)
                .orElse("N/a");
        cursoUsuario.setText(nomeCurso);
    }

    private void getGrupos() {
        grupoService.getGruposByAdmin(usuarioLogado.getId(), token)
                .enqueue(new Callback<List<Grupo>>() {
                    @Override
                    public void onResponse(Call<List<Grupo>> call, Response<List<Grupo>> response) {
                        if(response.isSuccessful()){
                            gruposList = response.body();
                            if(gruposList !=null){
                                listarGrupos();
                            } else {
                                showError("Não foi possível carregar grupos do usuário: ", response, context);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Grupo>> call, Throwable t) {

                    }
                });
    }

    public void listarGrupos() {
        recycleGruposDoUsuario.setLayoutManager(new LinearLayoutManager(context));
        recycleGruposDoUsuario.setAdapter(new RecyclerViewPerfilAluno(gruposList, this, context, fragmentManager, grupoService, token, usuarioLogado));
        recycleGruposDoUsuario.setNestedScrollingEnabled(false);
    }
}