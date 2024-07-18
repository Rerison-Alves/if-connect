package com.if_connect.request.services;

import com.if_connect.models.Agrupamento;
import com.if_connect.request.requestbody.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AgrupamentoService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("agrupamentos/search?")
    Call<Page<Agrupamento>> getAgrupamentosPageable(@Query("searchTerm") String searchTerm,
                                                    @Query("userId") String userId,
                                                    @Query("cursoId") String cursoId,
                                                    @Query("order") String order,
                                                    @Query("page") Integer page,
                                                    @Query("size") Integer size,
                                                    @Header("authorization") String auth);
}
