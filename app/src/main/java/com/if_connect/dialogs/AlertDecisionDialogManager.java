package com.if_connect.dialogs;

import android.app.AlertDialog;
import android.content.Context;

import com.example.if_connect.R;

public abstract class AlertDecisionDialogManager {
    Context context;
    String titulo;
    String mensagem;

    public AlertDecisionDialogManager(Context context, String titulo, String mensagem) {
        this.context = context;
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setNegativeButton(R.string.sim, (dialog, id) -> yes())
                .setPositiveButton(R.string.nao, (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public abstract void yes();
}