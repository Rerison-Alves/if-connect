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
import com.if_connect.utils.ImageManager;
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
    TurmaService turmaService;
    UsuarioService usuarioService;
    Context context;

    Button buttonCriarAgrupamento;
    List<Turma> turmasList = new ArrayList<>();
    TextView nomeUsuario, siapeUsuario;
    CardView cardImagem;
    public ImageView imagemUsuario;
    ShimmerFrameLayout shimmerImagem;
    FloatingActionButton config;
    RecyclerView recycleTurmasDoProfessor;

    Usuario usuarioLogado;
    String fotoPerfilBase64;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_professor, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        token = TokenManager.getInstance(context).getAccessToken();
        turmaService = Generator.getRetrofitInstance().create(TurmaService.class);
        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);

        nomeUsuario = view.findViewById(R.id.nome_usuario);
        siapeUsuario = view.findViewById(R.id.siape);
        cardImagem = view.findViewById(R.id.card_imagem);
        imagemUsuario = view.findViewById(R.id.imagem_usuario);
        shimmerImagem = view.findViewById(R.id.shimmer_imagem);
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
        getFotoPerfil();
        getTurmas();
        return view;
    }

    private void setProfessor() {
        nomeUsuario.setText(usuarioLogado.getNome());
        String siape = Optional.ofNullable(usuarioLogado.getProfessor())
                .map(Professor::getSiape)
                .orElse("N/a");
        siapeUsuario.setText(siape);
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

    public void getTurmas(){
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
