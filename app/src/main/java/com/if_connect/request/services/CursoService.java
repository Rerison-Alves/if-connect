package com.if_connect.request.services;

import com.if_connect.models.Curso;
import com.if_connect.request.requestbody.Page;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CursoService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("cursos/{id}")
    Call<Curso> getCurso(@Path("id") Integer id);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("cursos/get-all")
    Call<List<Curso>> getAllCursos();

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("cursos/search?")
    Call<Page<Curso>> getCursosPageable(@Query("searchTerm") String searchTerm,
                                        @Query("order") String order,
                                        @Query("page") Integer page,
                                        @Query("size") Integer size);
}
