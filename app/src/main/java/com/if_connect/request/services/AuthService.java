package com.if_connect.request.services;

import com.if_connect.request.requestbody.AuthenticationRequest;
import com.if_connect.request.requestbody.AuthenticationResponse;
import com.if_connect.request.requestbody.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("auth/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @POST("auth/authenticate")
    Call<AuthenticationResponse> authenticate(@Body AuthenticationRequest authenticationRequest);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Platform: android"})
    @GET("auth/refresh-token")
    Call<AuthenticationResponse> refreshtoken(@Header("authorization") String auth);
}
