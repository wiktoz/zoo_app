package com.example.blog_app_new.network;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.List;

public class MyCookieJar implements CookieJar {

    // Static instance for Singleton pattern
    private static MyCookieJar instance;

    private final CookieDataStore cookieStorage;

    // Private constructor to prevent instantiation from outside
    private MyCookieJar() {
        this.cookieStorage = new CookieDataStore();
    }

    // Public method to provide access to the single instance
    public static synchronized MyCookieJar getInstance() {
        if (instance == null) {
            instance = new MyCookieJar();
        }
        return instance;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStorage.storeCookies(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStorage.getCookies();
    }

    public boolean isLogged() {
        return cookieStorage.isLogged();
    }

    public String getCSRFToken() {
        return cookieStorage.getCSRFToken();
    }
}
