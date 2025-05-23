package com.if_connect.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.if_connect.models.Agrupamento;
import com.if_connect.utils.typeadapters.AgrupamentoAdapter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Generator {
//    public static String apiUrl = "http://localhost:8080/api/v1/";
    public static String apiUrl = "http://fair-vary.gl.at.ply.gg:51692/api/v1/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Agrupamento.class, new AgrupamentoAdapter())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}