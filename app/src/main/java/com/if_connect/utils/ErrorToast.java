package com.if_connect.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Response;

public class ErrorToast {
    public static void toastError(String text, Response response, Context context) {
        if (response.errorBody() != null) {
            try {
                List<ErrorResponse> list = new Gson().fromJson(
                        response.errorBody().charStream(),
                        new TypeToken<List<ErrorResponse>>() {
                        }.getType());
                if(list==null) throw new Exception();
                list.forEach(i -> Toast.makeText(context,
                        text + i.getMensagem(),
                        Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                e.printStackTrace();
                String codeErro = "code " + response.raw().code();
                Toast.makeText(context,
                        text + codeErro,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
