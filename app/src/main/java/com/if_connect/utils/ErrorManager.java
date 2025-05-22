package com.if_connect.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.if_connect.request.requestbody.ErrorResponse;

import retrofit2.Response;

public class ErrorManager {
    public static void showError(String text, Response response, Context context) {
        if (response.errorBody() != null) {
            try {
                ErrorResponse erro = new Gson().fromJson(
                        response.errorBody().charStream(),
                        new TypeToken<ErrorResponse>() {
                        }.getType());
                if (erro==null) throw new Exception();
                showErrorResponseDialog(context, text, erro);
            } catch (Exception e) {
                e.printStackTrace();
                String codeErro = "code " + response.raw().code();
                Toast.makeText(context,
                        text + codeErro,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private static void showErrorResponseDialog(Context context, String title, ErrorResponse errorResponse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        // Construindo a mensagem com base nos atributos do ErrorResponse
        StringBuilder message = new StringBuilder();
        if (errorResponse != null) {
            if (errorResponse.getMessage() != null) {
                message.append(errorResponse.getMessage()).append("\n");
            }
            if (errorResponse.getDetails() != null) {
                message.append("Details: ").append(errorResponse.getDetails());
            }
        }

        builder.setMessage(message.toString());

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
