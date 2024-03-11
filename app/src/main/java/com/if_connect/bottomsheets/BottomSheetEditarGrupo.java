package com.if_connect.bottomsheets;

import static com.if_connect.utils.ErrorToast.toastError;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.if_connect.dialogs.DialogConvidaUsuario;
import com.if_connect.models.Curso;
import com.if_connect.models.Grupo;
import com.if_connect.models.Usuario;
import com.if_connect.request.Generator;
import com.if_connect.request.services.GrupoService;
import com.if_connect.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetEditarGrupo extends BottomSheetCriarGrupo {

    Grupo grupoOld;

    public BottomSheetEditarGrupo(Context context, Usuario admin, FragmentManager fragmentManager, Grupo grupoOld) {
        super(context, admin, fragmentManager);
        this.grupoOld = grupoOld;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        nomeDoGrupo.setText(grupoOld.getNome());
        areaDeEstudo.setText(grupoOld.getAreadeEstudo());
        descricao.setText(grupoOld.getDescricao());
        spinnerCursos.setSelection(1);

        convidadosList = grupoOld.getUsuarios();
        changeCounter(convidadosList.size());
        return view;
    }

    @Override
    public void salvarGrupo(){
        if(validarCampos()){
            Grupo newGrupo = getGrupo();
            newGrupo.setId(grupoOld.getId());
            grupoService.updateGrupo(newGrupo, token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, "Grupo editado com sucesso!", Toast.LENGTH_SHORT).show();
                    }else {
                        toastError("Erro ao editar grupo: ", response, context);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}