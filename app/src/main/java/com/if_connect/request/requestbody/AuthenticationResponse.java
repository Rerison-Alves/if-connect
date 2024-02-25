package com.if_connect.request.requestbody;

import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {

  @SerializedName("access_token")
  public String accessToken;
  @SerializedName("refresh_token")
  public String refreshToken;
}