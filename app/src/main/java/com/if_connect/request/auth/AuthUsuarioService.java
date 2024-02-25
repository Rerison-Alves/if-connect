package com.if_connect.request.auth;

import com.if_connect.request.requestbody.AuthenticationRequest;
import com.if_connect.request.requestbody.AuthenticationResponse;
import com.if_connect.request.requestbody.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthUsuarioService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("auth/register")
    Call<AuthenticationResponse> registerUsuario(@Body RegisterRequest registerRequest);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("auth/authenticate")
    Call<AuthenticationResponse> authenticateUsuario(@Body AuthenticationRequest authenticationRequest);
}
