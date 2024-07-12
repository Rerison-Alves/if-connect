package com.if_connect.request.services;

import com.if_connect.models.Grupo;
import com.if_connect.request.requestbody.Page;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GrupoService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("grupos")
    Call<Grupo> createGrupo(@Body Grupo grupo,
                            @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @PUT("grupos")
    Call<ResponseBody> updateGrupo(@Body Grupo grupo,
                                   @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("grupos/{id}")
    Call<Grupo> getGrupo(@Path("id") Integer id,
                         @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("grupos/get-all")
    Call<List<Grupo>> getAllGrupos(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("grupos/get-by-admin/{id}")
    Call<List<Grupo>> getGruposByAdmin(@Path("id") Integer idAdmin,
                                       @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("grupos/search?")
    Call<Page<Grupo>> getGruposPageable(@Query("searchTerm") String searchTerm,
                                        @Query("userId") Integer userId,
                                        @Query("order") String order,
                                        @Query("page") Integer page,
                                        @Query("size") Integer size,
                                        @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @DELETE("grupos/{id}")
    Call<ResponseBody> deleteGrupo(@Path("id") Integer id,
                                   @Header("authorization") String auth);
}
