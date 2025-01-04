package com.example.blog_app_new.network;

import com.example.blog_app_new.models.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiEndpoint {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/token/refresh")
    Call<TokenResponse> refreshToken();

    @POST("auth/token/revoke/atoken")
    Call<ApiResponse> revokeAccessToken();

    @POST("auth/token/revoke/rtoken")
    Call<ApiResponse> revokeRefreshToken();
}
