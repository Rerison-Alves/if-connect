package com.if_connect.fragments;

import static com.if_connect.utils.ErrorToast.toastError;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.if_connect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if_connect.bottomsheets.BottomSheetCriarGrupo;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Grupo;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewPerfil;
import com.if_connect.request.Generator;
import com.if_connect.request.services.GrupoService;
import com.if_connect.request.services.UsuarioService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Perfil extends Fragment {

    FragmentManager fragmentManager;
    String token;
    UsuarioService usuarioService;
    GrupoService grupoService;

    Context context;
    Button buttonCriarAgrupamento;
    List<Grupo> listadeGrupos = new ArrayList<>();
    FloatingActionButton config;
    TextView nomeusuariologado, cursousuariologado;
    RecyclerView usuariogpsView;
    Usuario usuarioLogado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        context= getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        token = TokenManager.getInstance(context).getAccessToken();
        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);
        grupoService = Generator.getRetrofitInstance().create(GrupoService.class);

        nomeusuariologado= view.findViewById(R.id.nomeusuariologado);
        cursousuariologado= view.findViewById(R.id.cursousuariologado);
        config= view.findViewById(R.id.config);
        buttonCriarAgrupamento = view.findViewById(R.id.btn_novoagrupamento);
        usuariogpsView = view.findViewById(R.id.gpsusuario);

        config.setOnClickListener(view1 -> {
//            Intent startConfig = new Intent(getActivity(), Config.class);
//            startActivity(startConfig);
        });
        buttonCriarAgrupamento.setOnClickListener(view1 ->
                new BottomSheetCriarGrupo(context, usuarioLogado, fragmentManager).show(fragmentManager,"TAG"));
        getUsuario();
        return view;
    }

    private void getUsuario() {
        usuarioService.getUsuarioLogado(token).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    usuarioLogado = response.body();
                    setUsuario();
                    getGrupos();
                }else {
                    toastError("Não foi possível carregar usuário: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUsuario() {
        nomeusuariologado.setText(usuarioLogado.getNome());
        String nomeCurso = Optional.ofNullable(usuarioLogado.getAluno())
                                                .map(Aluno::getCurso)
                                                .map(Curso::getDescricao)
                                                .orElse("N/a");
        cursousuariologado.setText(nomeCurso);
    }

    private void getGrupos() {
        grupoService.getGruposByAdmin(usuarioLogado.getId(), token)
                .enqueue(new Callback<List<Grupo>>() {
                    @Override
                    public void onResponse(Call<List<Grupo>> call, Response<List<Grupo>> response) {
                        if(response.isSuccessful()){
                            listadeGrupos = response.body();
                            if(listadeGrupos !=null){
                                listarGrupos();
                            } else {
                                toastError("Não foi possível carregar grupos do usuário: ", response, context);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Grupo>> call, Throwable t) {

                    }
                });
    }

    private void listarGrupos() {
        usuariogpsView.setLayoutManager(new LinearLayoutManager(context));
        usuariogpsView.setAdapter(new RecyclerViewPerfil(listadeGrupos, context, fragmentManager, grupoService, token, usuarioLogado));
        usuariogpsView.setNestedScrollingEnabled( false );
    }
    
}