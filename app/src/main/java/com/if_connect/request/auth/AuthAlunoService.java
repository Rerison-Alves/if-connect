package com.if_connect.request.auth;

import com.if_connect.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthAlunoService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("auth/register")
    Call<AuthenticationResponse> registerUsuario(@Body Usuario usuario);
}
