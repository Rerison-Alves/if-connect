package com.if_connect.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.dialogs.DialogPesquisar;
import com.if_connect.models.Curso;
import com.if_connect.recycleviews.RecyclerViewCursos;
import com.if_connect.request.Generator;
import com.if_connect.request.services.CursoService;
import com.if_connect.utils.SpeechToText;

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

    AutoCompleteTextView pesquisar;
    ImageButton speechButton;
    RecyclerView recycleCursos;

    SpeechToText speechToText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        cursoService = Generator.getRetrofitInstance().create(CursoService.class);
        pesquisar = view.findViewById(R.id.pesquisar);
        speechButton = view.findViewById(R.id.btnVoice);
        recycleCursos = view.findViewById(R.id.recycle_cursos);

        speechToText =  new SpeechToText(this, s -> {
            pesquisar.setText(s);
            new DialogPesquisar(context, fragmentManager, pesquisar.getText().toString()).show(fragmentManager, "tag");
        });
        speechButton.setOnClickListener(v -> speechToText.speak());

        pesquisar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                new DialogPesquisar(context, fragmentManager, pesquisar.getText().toString()).show(fragmentManager, "tag");
                return true;
            }
            return false;
        });

        getCursos();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        speechToText.onActivityResult(requestCode, resultCode, data);
    }

    private void getCursos() {
        cursoService.getAllCursos().enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if (response.isSuccessful()) {
                    cursosList = response.body();
                    if (cursosList!=null && !cursosList.isEmpty()) {
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