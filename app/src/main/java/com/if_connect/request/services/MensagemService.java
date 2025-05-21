package com.if_connect.request.services;

import com.if_connect.models.Mensagem;
import com.if_connect.models.Mensagem;
import com.if_connect.models.Mensagem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MensagemService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("mensagens/{id}")
    Call<Mensagem> getMensagem(@Path("id") Integer id,
                               @Header("authorization") String auth);
    
    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("mensagens/get-all")
    Call<List<Mensagem>> getAllMensagens(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("mensagens/search?")
    Call<List<Mensagem>> getMensagensPageable(@Query("encontroId") Integer encontroId,
                                              @Query("order") String order,
                                              @Query("page") Integer page,
                                              @Query("size") Integer size,
                                              @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @PUT("mensagens")
    Call<ResponseBody> updateMensagem(@Body Mensagem mensagem,
                                      @Header("authorization") String auth);
    
    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @DELETE("mensagens/{id}")
    Call<ResponseBody> deleteMensagem(@Path("id") Integer id,
                                      @Header("authorization") String auth);
}
