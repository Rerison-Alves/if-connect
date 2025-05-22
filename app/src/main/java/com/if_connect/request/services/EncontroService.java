package com.if_connect.request.services;

import com.if_connect.models.Encontro;
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

public interface EncontroService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("encontros")
    Call<Encontro> createEncontro(@Body Encontro encontro,
                                  @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @PUT("encontros")
    Call<ResponseBody> updateEncontro(@Body Encontro encontro,
                                      @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/{id}")
    Call<Encontro> getEncontro(@Path("id") Integer id,
                               @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/get-all")
    Call<List<Encontro>> getAllEncontros(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/user-upcoming/{id}")
    Call<List<Encontro>> getUpcomingEncontrosByUser(@Path("id")Integer idUser,
                                                    @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/grupo/{id}")
    Call<List<Encontro>> getEncontrosByGrupo(@Path("id")Integer idGrupo,
                                             @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/grupo-upcoming/{id}")
    Call<List<Encontro>> getUpcomingEncontrosByGrupo(@Path("id")Integer idGrupo,
                                                     @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/turma/{id}")
    Call<List<Encontro>> getEncontrosByTurma(@Path("id")Integer idTurma,
                                             @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/turma-upcoming/{id}")
    Call<List<Encontro>> getUpcomingEncontrosByTurma(@Path("id")Integer idTurma,
                                                     @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("encontros/search?")
    Call<Page<Encontro>> getEncontrosPageable(@Query("searchTerm") String searchTerm,
                                              @Query("order") String order,
                                              @Query("page") Integer page,
                                              @Query("size") Integer size,
                                              @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @DELETE("encontros/{id}")
    Call<ResponseBody> deleteEncontro(@Path("id") Integer id,
                                      @Header("authorization") String auth);
}
