package com.if_connect.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Generator {
    public static String apiUrl = "https://ifconnect-36203a119f8b.herokuapp.com/api/v1/";
//    public static String apiUrl = "https://1a81-186-249-189-220.ngrok-free.app/api/v1/";
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