package com.if_connect.dialogs;

import static com.if_connect.utils.ErrorManager.showError;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_connect.R;
import com.if_connect.bottomsheets.BottomSheetCriarGrupo;
import com.if_connect.models.Usuario;
import com.if_connect.recycleviews.RecyclerViewUsuariosConvidados;
import com.if_connect.recycleviews.RecyclerViewUsuariosTodos;
import com.if_connect.request.Generator;
import com.if_connect.request.services.UsuarioService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogConvidaUsuario extends DialogFragment {

    UsuarioService usuarioService;
    String token;

    RecyclerView recycleConvidados, recycleTodos;
    TextView counterConvidados;
    Context context;

    List<Usuario> convidadosList;
    List<Usuario> todosList = new ArrayList<>();
    BottomSheetCriarGrupo bottomSheetCriarGrupo;

    public DialogConvidaUsuario(Context context, List<Usuario> convidadosList, BottomSheetCriarGrupo bottomSheetCriarGrupo) {
        this.context = context;
        this.convidadosList = convidadosList;
        this.bottomSheetCriarGrupo = bottomSheetCriarGrupo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_convida_usuario,null);
        builder.setView(view).setTitle("").setPositiveButton("OK", (dialogInterface, i) -> {});

        usuarioService = Generator.getRetrofitInstance().create(UsuarioService.class);
        token = TokenManager.getInstance(context).getAccessToken();

        recycleConvidados = view.findViewById(R.id.ListaConvidados);
        recycleTodos = view.findViewById(R.id.ListaTodos);
        counterConvidados = view.findViewById(R.id.nconvidados);

        ConsultaUsuarios();
        return builder.create();
    }

    private void ConsultaUsuarios() {
        usuarioService.getAllUsuarios(token).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.isSuccessful()){
                    todosList = response.body();
                    if(todosList!=null){
                        todosList.removeAll(convidadosList);
                        todosList.remove(bottomSheetCriarGrupo.admin);
                        adaptarListas();
                    }else {
                        showError("Não foi possível carregar usuários: ", response, context);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void adaptarListas(){
        adaptarListTodos();
        adaptarListConvidados();
        counterConvidados.setText(String.format("%d/50", convidadosList.size()));
        bottomSheetCriarGrupo.changeCounter(convidadosList.size());
    }

    private void adaptarListTodos(){
        recycleTodos.setLayoutManager(new LinearLayoutManager(context));
        recycleTodos.setAdapter(new RecyclerViewUsuariosTodos(context, convidadosList, todosList, this));
    }

    private void adaptarListConvidados(){
        recycleConvidados.setLayoutManager(new LinearLayoutManager(context));
        recycleConvidados.setAdapter(new RecyclerViewUsuariosConvidados(context, convidadosList, todosList, this));
    }

}