package com.if_connect.request.services;

import com.if_connect.models.Turma;
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

public interface TurmaService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("turmas")
    Call<Turma> createTurma(@Body Turma turma,
                            @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @PUT("turmas/{id}")
    Call<ResponseBody> updateTurma(@Path("id") Integer id,
                                   @Body Turma turma,
                                   @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("turmas/{id}")
    Call<Turma> getTurma(@Path("id") Integer id,
                         @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("turmas/get-all")
    Call<List<Turma>> getAllTurmas(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("turmas/get-by-admin/{id}")
    Call<List<Turma>> getTurmasByAdmin(@Path("id") Integer idAdmin,
                                       @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("turmas/search?")
    Call<Page<Turma>> getTurmasPageable(@Query("searchTerm") String searchTerm,
                                        @Query("userId") String userId,
                                        @Query("cursoId") String cursoId,
                                        @Query("order") String order,
                                        @Query("page") Integer page,
                                        @Query("size") Integer size,
                                        @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @DELETE("turmas/{id}")
    Call<ResponseBody> deleteTurma(@Path("id") Integer id,
                                   @Header("authorization") String auth);
}
