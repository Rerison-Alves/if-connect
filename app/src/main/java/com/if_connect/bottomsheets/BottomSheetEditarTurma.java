package com.if_connect.bottomsheets;

import static com.if_connect.utils.ErrorManager.showError;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.if_connect.dialogs.AlertDialogManager;
import com.if_connect.fragments.PerfilProfessor;
import com.if_connect.models.Turma;
import com.if_connect.models.Usuario;
import com.if_connect.models.enums.Turno;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetEditarTurma extends BottomSheetCriarTurma {

    Turma turmaOld;

    public BottomSheetEditarTurma(Context context, Usuario admin, FragmentManager fragmentManager, PerfilProfessor perfilProfessor, Turma turmaOld) {
        super(context, admin, fragmentManager, perfilProfessor);
        this.turmaOld = turmaOld;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        nomeTurma.setText(turmaOld.getNome());
        disciplina.setText(turmaOld.getDisciplina());
        descricao.setText(turmaOld.getDescricao());
        spinnerTurno.setSelection(
                Arrays.asList(Turno.getDescricoes()).
                        indexOf(turmaOld.getTurno().name()));

        convidadosList = turmaOld.getUsuarios();
        changeCounter(convidadosList.size());
        return view;
    }

    @Override
    public void salvarTurma(){
        if(validarCampos()){
            Turma newTurma = getTurma();
            newTurma.setId(turmaOld.getId());
            turmaService.updateTurma(newTurma.getId(), newTurma, token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        new AlertDialogManager(context, "Informações atualizadas!", "As informações da turma foram alteradas com sucesso!").show();
                        perfilProfessor.getTurmas();
                        dismiss();
                    }else {
                        showError("Erro ao editar turma: ", response, context);
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