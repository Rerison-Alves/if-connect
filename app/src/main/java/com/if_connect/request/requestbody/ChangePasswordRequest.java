package com.if_connect.request.requestbody;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest {
    @SerializedName("code")
    public Integer code;
    @SerializedName("userEmail")
    public String userEmail;
    @SerializedName("newPassword")
    public String newPassword;
    @SerializedName("confirmationPassword")
    public String confirmationPassword;

    public ChangePasswordRequest(Integer code, String userEmail, String newPassword, String confirmationPassword) {
        this.code = code;
        this.userEmail = userEmail;
        this.newPassword = newPassword;
        this.confirmationPassword = confirmationPassword;
    }
}
