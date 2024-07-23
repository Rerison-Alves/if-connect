package com.if_connect.dialogs;

import static com.if_connect.utils.ErrorManager.showError;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.models.Encontro;
import com.if_connect.models.Grupo;
import com.if_connect.recycleviews.RecyclerViewEncontros;
import com.if_connect.request.Generator;
import com.if_connect.request.services.EncontroService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogGrupo extends DialogFragment {
    
    Grupo grupo;
    Context context;
    FragmentManager fragmentManager;
    List<Encontro> encontrosList = new ArrayList<>();
    EncontroService encontroService;
    String token;

    public DialogGrupo(Grupo grupo, Context context, FragmentManager fragmentManager) {
        this.grupo=grupo;
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    TextView nomeGrupo, areaestudo,descricao, adm;
    ImageView voltar;
    RecyclerView listaEncontros;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Ifconnect);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_grupo, container, false);
        token = TokenManager.getInstance(context).getAccessToken();
        encontroService = Generator.getRetrofitInstance().create(EncontroService.class);
        
        nomeGrupo = view.findViewById(R.id.nome_grupo);
        voltar = view.findViewById(R.id.voltar);
        descricao = view.findViewById(R.id.descricao);
        areaestudo = view.findViewById(R.id.areaestudo);
        adm = view.findViewById(R.id.adm);
        listaEncontros = view.findViewById(R.id.lista_encontros);

        voltar.setOnClickListener(v -> dismiss());
        nomeGrupo.setText(grupo.getNome());
        descricao.setText(grupo.getDescricao());
        areaestudo.setText(grupo.getAreadeEstudo());
        adm.setText(grupo.getAdmin().getNome());

        searchEncontros();
        return view;
    }

    private void listarEncontros() {
        listaEncontros.setLayoutManager(new LinearLayoutManager(context));
        listaEncontros.setAdapter(new RecyclerViewEncontros(context, fragmentManager, encontrosList));
        listaEncontros.setNestedScrollingEnabled( false );
    }

    public void searchEncontros() {
        encontroService.getEncontrosByGrupo(grupo.getId(), token).enqueue(new Callback<List<Encontro>>() {
            @Override
            public void onResponse(Call<List<Encontro>> call, Response<List<Encontro>> response) {
                if(response.isSuccessful()){
                    encontrosList = response.body();
                    listarEncontros();
                }else {
                    showError("Não possível carregar encontros: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<List<Encontro>> call, Throwable t) {

            }
        });
    }

}