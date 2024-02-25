package com.if_connect.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Generator {
    public static String apiUrl = "http://172.25.240.1:8080/api/v1/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}