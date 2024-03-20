package com.if_connect.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {
    private static final String TOKEN_PREF = "token_pref";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";

    private final SharedPreferences sharedPreferences;
    private static TokenManager instance;

    private TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(TOKEN_PREF, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public void saveTokens(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    public String getAccessToken() {
        String token = sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
        return token!=null?bearer(token):null;
    }

    public String getRefreshToken() {
        String token = sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
        return token!=null?bearer(token):null;
    }

    public void deleteTokens() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.apply();
    }

    private String bearer(String token){
        return "Bearer " + token;
    }
}