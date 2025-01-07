package com.example.blog_app_new.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static ApiService instance;
    private static final String BASE_URL = "http://10.0.2.2:5000/api/";
    private final ApiEndpoint apiEndpoint;

    private ApiService() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder()
                                .header("X-CSRF-TOKEN", MyCookieJar.getInstance().getCSRFToken())
                                .method(original.method(), original.body());

                        Response originalResponse = chain.proceed(requestBuilder.build());

                        return originalResponse;
                    }
                })
                .cookieJar(MyCookieJar.getInstance())
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiEndpoint = retrofit.create(ApiEndpoint.class);
    }

    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public ApiEndpoint getApiEndpoint() {
        return apiEndpoint;
    }
}
