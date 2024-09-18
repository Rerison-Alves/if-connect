package com.if_connect.recycleviews;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.if_connect.R;
import com.if_connect.dialogs.AlertDecisionDialogManager;
import com.if_connect.models.Encontro;

import java.util.List;

public class RecyclerViewEncontrosAdm extends RecyclerViewEncontros{

    public RecyclerViewEncontrosAdm(Context context, FragmentManager fragmentManager, List<Encontro> encontros) {
        super(context, fragmentManager, encontros);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.edit.setOnClickListener(v -> {

        });
        holder.excluir.setOnClickListener(v -> {
            new AlertDecisionDialogManager(context,
                    String.format("Deseja excluir %s?", encontros.get(position).getTema()),
                    context.getString(R.string.encontro_delete_message)) {
                @Override public void yes() {excluirEncontro();}
            }.show();
        });
    }

    private void excluirEncontro() {
    }
}
