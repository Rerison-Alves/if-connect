package com.if_connect.request.services;

import com.if_connect.models.Local;
import com.if_connect.request.requestbody.Page;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LocalService {
    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("locais/{id}")
    Call<Local> getLocal(@Path("id") Integer id);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("locais/get-all")
    Call<List<Local>> getAllLocais();

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("locais/find-available")
    Call<List<Local>> getLocaisAvailable(@Query("startTime") String startTime,
                                         @Query("endTime") String endTime,
                                         @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("locais/search?")
    Call<Page<Local>> getLocaisPageable(@Query("searchTerm") String searchTerm,
                                        @Query("order") String order,
                                        @Query("page") Integer page,
                                        @Query("size") Integer size);
}
