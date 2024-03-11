package com.if_connect.request.services;

import com.if_connect.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("usuarios/{id}")
    Call<Usuario> getUsuario(@Path("id") Integer id,
                             @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("usuarios/usuario-logado")
    Call<Usuario> getUsuarioLogado(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("usuarios/get-all")
    Call<List<Usuario>> getAllUsuarios(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("usuarios/search?")
    Call<List<Usuario>> getUsuariosPageable(@Query("searchTerm") String searchTerm,
                                            @Query("inicio") Integer inicio,
                                            @Query("limite") Integer limite,
                                            @Query("ordem") String ordem,
                                            @Header("authorization") String auth);
}
