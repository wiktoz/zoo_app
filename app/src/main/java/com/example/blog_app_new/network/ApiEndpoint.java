package com.example.blog_app_new.network;

import com.example.blog_app_new.CModels.Group;
import com.example.blog_app_new.CModels.Notification;
import com.example.blog_app_new.CModels.Post;
import com.example.blog_app_new.networksModels.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET("groups/my")
    Call<List<Group>> getGroups();
    @GET("groups")
    Call<List<Group>> getAllGroups();
    @GET("notifications/me")
    Call<List<Notification>> getNotifications();

    @GET("groups/{group_id}")
    Call<Group> getGroupDetails(@Path("group_id") String groupId);

    @GET("groups/{group_id}/posts")
    Call<List<Post>> getGroupPosts(@Path("group_id") String groupId);
    @GET("groups/{group_id}/join")
    Call<ApiResponse> joinGroup(@Path("group_id") String groupId);
}
