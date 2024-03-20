package com.if_connect.dialogs;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogManager {
    Context context;
    String titulo;
    String mensagem;

    public AlertDialogManager(Context context, String titulo, String mensagem) {
        this.context = context;
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }
}