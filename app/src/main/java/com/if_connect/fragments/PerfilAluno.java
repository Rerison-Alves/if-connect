package com.if_connect.fragments;

import static com.if_connect.utils.ErrorManager.showError;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if_connect.MainActivity;
import com.if_connect.bottomsheets.BottomSheetCriarGrupo;
import com.if_connect.dialogs.DialogAlterarImagemPerfil;
import com.if_connect.dialogs.DialogConfig;
import com.if_connect.models.Aluno;
import com.if_connect.models.Curso;
import com.if_connect.models.Grupo;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewPerfilAluno;
import com.if_connect.request.Generator;
import com.if_connect.request.services.GrupoService;
import com.if_connect.request.services.UsuarioService;
import com.if_connect.utils.ImageManager;
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
    UsuarioService usuarioService;
    Context context;

    Button buttonCriarAgrupamento;
    List<Grupo> gruposList = new ArrayList<>();

    FloatingActionButton config;
    TextView nomeUsuario, cursoUsuario;
    CardView cardImagem;
    public ImageView imagemUsuario;
    ShimmerFrameLayout shimmerImagem;
    RecyclerView recycleGruposDoUsuario;

    Usuario usuarioLogado;
    String fotoPerfilBase64;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_aluno, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        token = TokenManager.getInstance(context).getAccessToken();
        grupoService = Generator.getRetrofitInstance().create(GrupoService.class);
        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);

        nomeUsuario = view.findViewById(R.id.nomeusuariologado);
        cursoUsuario = view.findViewById(R.id.cursousuariologado);
        cardImagem = view.findViewById(R.id.card_imagem);
        imagemUsuario = view.findViewById(R.id.imagem_usuario);
        shimmerImagem = view.findViewById(R.id.shimmer_imagem);
        config= view.findViewById(R.id.config);
        buttonCriarAgrupamento = view.findViewById(R.id.btn_novoagrupamento);
        recycleGruposDoUsuario = view.findViewById(R.id.gpsusuario);

        cardImagem.setOnClickListener(v -> {
            new DialogAlterarImagemPerfil(context, imagemUsuario, fotoPerfilBase64).show(fragmentManager, "");
        });
        config.setOnClickListener(v -> {
            new DialogConfig(context, (MainActivity) getActivity(), fragmentManager).show(fragmentManager,"tag");
        });
        buttonCriarAgrupamento.setOnClickListener(v -> {
            new BottomSheetCriarGrupo(context, usuarioLogado, fragmentManager, this).show(fragmentManager,"TAG");
        });

        usuarioLogado = UsuarioManager.getUsuarioLogado();
        setAluno();
        getFotoPerfil();
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

    private void getFotoPerfil() {
        startImageShimmerAnimation();
        usuarioService.getProfileImage(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    fotoPerfilBase64 = response.body();
                    if(fotoPerfilBase64!=null){
                        imagemUsuario.setImageBitmap(
                                ImageManager.base64StringToBitmap(fotoPerfilBase64)
                        );
                    }
                }
                stopImageShimmerAnimation();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                stopImageShimmerAnimation();
            }
        });
    }

    private void startImageShimmerAnimation(){
        cardImagem.setVisibility(View.INVISIBLE);
        shimmerImagem.setVisibility(View.VISIBLE);
        shimmerImagem.startShimmerAnimation();
    }

    private void stopImageShimmerAnimation(){
        cardImagem.setVisibility(View.VISIBLE);
        shimmerImagem.setVisibility(View.INVISIBLE);
        shimmerImagem.stopShimmerAnimation();
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
