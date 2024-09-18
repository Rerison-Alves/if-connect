package com.if_connect.dialogs;

import static com.if_connect.utils.ErrorManager.showError;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetCriarEncontro;
import com.if_connect.models.Local;
import com.if_connect.recycleviews.RecyclerViewLocais;
import com.if_connect.request.Generator;
import com.if_connect.request.services.LocalService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogEscolherLocal extends DialogFragment {

    Context context;
    List<Local> locaisList = new ArrayList<>();
    FragmentManager fragmentManager;
    String startTime, endTime;
    RecyclerView locais;
    BottomSheetCriarEncontro bottomSheetCriarEncontro;

    LocalService localService;
    String token;

    public DialogEscolherLocal(Context context, FragmentManager fragmentManager, BottomSheetCriarEncontro bottomSheetCriarEncontro, String startTime, String endTime) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.bottomSheetCriarEncontro = bottomSheetCriarEncontro;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_escolher_local,null);
        token = TokenManager.getInstance(context).getAccessToken();
        localService = Generator.getRetrofitInstance().create(LocalService.class);

        locais = view.findViewById(R.id.locais);

        builder.setView(view).setTitle("Escolha um Local (Online por padrão):");

        builder.setPositiveButton("Cancelar", (dialog, which) -> dismiss());

        consultalocais();
        return builder.create();
    }

    private void adaptarLocais() {
        locais.setLayoutManager(new LinearLayoutManager(context));
        locais.setAdapter(new RecyclerViewLocais(context, fragmentManager, locaisList, this, bottomSheetCriarEncontro));
    }

    private void consultalocais() {
        localService.getLocaisAvailable(startTime, endTime, token).enqueue(new Callback<List<Local>>() {
            @Override
            public void onResponse(Call<List<Local>> call, Response<List<Local>> response) {
                if(response.isSuccessful()){
                    locaisList = response.body();
                    if(locaisList!=null && locaisList.size()>0){
                        adaptarLocais();
                    }
                }else {
                    showError("Não foi possível buscar locais: ", response, context);
                }
            }

            @Override
            public void onFailure(Call<List<Local>> call, Throwable t) {

            }
        });

    }

}