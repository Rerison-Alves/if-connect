package com.if_connect.request.services;

import com.if_connect.models.Usuario;
import com.if_connect.request.requestbody.ChangePasswordRequest;
import com.if_connect.request.requestbody.Page;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<Page<Usuario>> getUsuariosPageable(@Query("searchTerm") String searchTerm,
                                            @Query("inicio") Integer inicio,
                                            @Query("limite") Integer limite,
                                            @Query("ordem") String ordem,
                                            @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("usuarios/change-password")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @PATCH("usuarios/change-password-code")
    Call<ResponseBody> changePasswordCode(@Query("email") String email);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("usuarios/profile-image")
    Call<String> getProfileImage(@Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @PUT("usuarios/profile-image")
    Call<ResponseBody> changeProfileImage(@Body String fotoPerfilBase64,
                                          @Header("authorization") String auth);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @DELETE("usuarios/profile-image")
    Call<ResponseBody> deleteProfileImage(@Header("authorization") String auth);
}
