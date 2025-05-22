package com.if_connect.fragments;

import static java.lang.String.valueOf;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.models.Agrupamento;
import com.if_connect.models.Encontro;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewEncontrosPrincipal;
import com.if_connect.recycleviews.RecyclerViewTodos;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.Page;
import com.if_connect.request.services.AgrupamentoService;
import com.if_connect.request.services.EncontroService;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Principal extends Fragment {

    Context context;
    FragmentManager fragmentManager;

    Usuario usuarioLogado;
    String token;

    EncontroService encontroService;
    AgrupamentoService agrupamentoService;

    List<Agrupamento> agrupamentosList = new ArrayList<>();
    List<Encontro> proximosEncontrosList = new ArrayList<>();

    RecyclerView recycleRecentes,
            recycleEncontros,
            recycleTodos;
    LinearLayout emptyRecentes,
            emptyEncontros,
            emptyTodos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        usuarioLogado = UsuarioManager.getUsuarioLogado();
        token = TokenManager.getInstance(context).getAccessToken();
        encontroService = Generator.getRetrofitInstance().create(EncontroService.class);
        agrupamentoService = Generator.getRetrofitInstance().create(AgrupamentoService.class);

        recycleRecentes = view.findViewById(R.id.recycle_recentes);
        recycleEncontros = view.findViewById(R.id.recycle_encontros);
        recycleTodos = view.findViewById(R.id.recycle_todos);

        emptyRecentes = view.findViewById(R.id.empty_recente);
        emptyEncontros = view.findViewById(R.id.empty_encontro);
        emptyTodos = view.findViewById(R.id.empty_todos);

        recycleTodos.setNestedScrollingEnabled( false );

        getProximosEncontros();
//        getRecentes();
        getTodos();
        return view;
    }

    private void getTodos() {
        agrupamentoService.getAgrupamentosPageable("", valueOf(usuarioLogado.getId()), "","nome", 0, Integer.MAX_VALUE, token).enqueue(new Callback<Page<Agrupamento>>() {
            @Override
            public void onResponse(Call<Page<Agrupamento>> call, Response<Page<Agrupamento>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        agrupamentosList = response.body().getContent();
                        if (!agrupamentosList.isEmpty()) {
                            listarTodos();
                            recycleTodos.setVisibility(View.VISIBLE);
                            emptyTodos.setVisibility(View.INVISIBLE);
                        }else {
                            recycleTodos.setVisibility(View.INVISIBLE);
                            emptyTodos.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Page<Agrupamento>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarTodos() {
        recycleTodos.setLayoutManager(new LinearLayoutManager(context));
        recycleTodos.setAdapter(new RecyclerViewTodos(context, fragmentManager, agrupamentosList));
    }


    private void getRecentes() {
        //
    }

    private void listarRecentes() {
//        recentesView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        recentesView.setAdapter(RecyclerViewAdapterRecentes(context, requireActivity().getSupportFragmentManager(), listaTodos));
    }

    private void getProximosEncontros() {
        encontroService.getUpcomingEncontrosByUser(usuarioLogado.getId(), token).enqueue(new Callback<List<Encontro>>() {
            @Override
            public void onResponse(Call<List<Encontro>> call, Response<List<Encontro>> response) {
                if (response.isSuccessful()) {
                    proximosEncontrosList = response.body();
                    if (proximosEncontrosList!=null) {
                        if (!proximosEncontrosList.isEmpty()) {
                            listarProximosEncontros();
                            recycleEncontros.setVisibility(View.VISIBLE);
                            emptyEncontros.setVisibility(View.INVISIBLE);
                        }else {
                            recycleEncontros.setVisibility(View.INVISIBLE);
                            emptyEncontros.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Encontro>> call, Throwable throwable) {
            }
        });
    }

    private void listarProximosEncontros() {
        recycleEncontros.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycleEncontros.setAdapter(new RecyclerViewEncontrosPrincipal(context, fragmentManager, proximosEncontrosList));
    }

}