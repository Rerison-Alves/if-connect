package com.if_connect.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.LoginActivity;
import com.if_connect.MainActivity;
import com.if_connect.bottomsheets.BottomSheetShape;
import com.if_connect.fragments.FragmentCPEmail;
import com.if_connect.models.Agrupamento;
import com.if_connect.recycleviews.RecyclerViewTodos;
import com.if_connect.request.Generator;
import com.if_connect.request.requestbody.Page;
import com.if_connect.request.services.AgrupamentoService;
import com.if_connect.utils.BounceEditText;
import com.if_connect.utils.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogPesquisar extends DialogFragment {

    Context context;
    FragmentManager fragmentManager;
    String filtroPesquisa;
    String token;
    AgrupamentoService agrupamentoService;

    ImageView voltar;
    AutoCompleteTextView pesquisar;
    RecyclerView recycleAgrupamentos;

    List<Agrupamento> agrupamentoList;

    public DialogPesquisar(Context context, FragmentManager fragmentManager, String filtroPesquisa) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.filtroPesquisa = filtroPesquisa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pesquisar, container, false);
        token = TokenManager.getInstance(context).getAccessToken();
        agrupamentoService = Generator.getRetrofitInstance().create(AgrupamentoService.class);
        voltar = view.findViewById(R.id.voltar);
        pesquisar = view.findViewById(R.id.pesquisar);
        recycleAgrupamentos = view.findViewById(R.id.agrupamentos);

        voltar.setOnClickListener(v -> dismiss());

        pesquisar.setText(filtroPesquisa);

        new BounceEditText(pesquisar, 500, text -> {
            filtroPesquisa = text.toString();
            searcherAgrupamento(filtroPesquisa);
        });

        return view;
    }

    private void searcherAgrupamento(String filtro) {
        agrupamentoService.getAgrupamentosPageable(filtro, "", "", "nome", 0, Integer.MAX_VALUE, token).enqueue(new Callback<Page<Agrupamento>>() {
            @Override
            public void onResponse(Call<Page<Agrupamento>> call, Response<Page<Agrupamento>> response) {
                if(response.isSuccessful() && response.body() != null){
                    agrupamentoList = response.body().getContent();
                    listarAgrupamentos();
                }
            }

            @Override
            public void onFailure(Call<Page<Agrupamento>> call, Throwable t) {

            }
        });
    }

    private void listarAgrupamentos() {
        recycleAgrupamentos.setLayoutManager(new LinearLayoutManager(context));
        recycleAgrupamentos.setAdapter(new RecyclerViewTodos(context, fragmentManager, agrupamentoList));
    }


}