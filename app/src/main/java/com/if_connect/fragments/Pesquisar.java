package com.if_connect.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.models.Curso;
import com.if_connect.recycleviews.RecyclerViewCursos;
import com.if_connect.request.Generator;
import com.if_connect.request.services.CursoService;
import com.if_connect.utils.TokenManager;
import com.if_connect.utils.UsuarioManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Pesquisar extends Fragment {

    Context context;
    FragmentManager fragmentManager;
    List<Curso> cursosList = new ArrayList<>();

    CursoService cursoService;

    RecyclerView recycleCursos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        cursoService = Generator.getRetrofitInstance().create(CursoService.class);
        recycleCursos = view.findViewById(R.id.recycle_cursos);

        getCursos();

        return view;
    }

    private void getCursos() {
        cursoService.getAllCursos().enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if(response.isSuccessful()){
                    cursosList = response.body();
                    if(cursosList!=null && !cursosList.isEmpty()){
                        listarCursos();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {

            }
        });
    }

    private void listarCursos() {
        recycleCursos.setLayoutManager(new LinearLayoutManager(context));
        recycleCursos.setAdapter(new RecyclerViewCursos(context, fragmentManager, cursosList));
    }
}