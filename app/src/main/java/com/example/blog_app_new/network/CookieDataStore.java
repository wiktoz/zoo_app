package com.example.blog_app_new.network;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.Cookie;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CookieDataStore {

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    /**
     * Stores the cookies in the internal storage.
     *
     * @param cookies List of cookies to store.
     */
    public void storeCookies(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            String domain = cookie.domain();

            // Retrieve existing cookies for the domain
            List<Cookie> existingCookies = cookieStore.getOrDefault(domain, new ArrayList<>());

            // Remove cookies with the same name to avoid duplicates
            Iterator<Cookie> iterator = existingCookies.iterator();
            while (iterator.hasNext()) {
                Cookie existingCookie = iterator.next();
                if (existingCookie.name().equals(cookie.name())) {
                    iterator.remove();
                }
            }
            Log.d("TokenManagement","Adding cookie " + cookie.name() + ": " + cookie.value());

            // Add the new cookie
            existingCookies.add(cookie);
            cookieStore.put(domain, existingCookies);
        }
    }

    /**
     * Retrieves cookies for requests.
     *
     * @return List of cookies to be added to the request.
     */
    public List<Cookie> getCookies() {
        List<Cookie> allCookies = new ArrayList<>();

        for (List<Cookie> cookies : cookieStore.values()) {
            // Remove expired cookies
            Iterator<Cookie> iterator = cookies.iterator();
            while (iterator.hasNext()) {
                Cookie cookie = iterator.next();
                if (cookie.expiresAt() < System.currentTimeMillis()) {
                    iterator.remove();
                }
            }
            allCookies.addAll(cookies);
        }

        return allCookies;
    }

    public boolean isLogged() {
        for (List<Cookie> cookies : cookieStore.values()) {
            for (Cookie cookie : cookies) {
                if ("access_token_cookie".equals(cookie.name())) {
                    return isJwtValid(cookie.value());
                }
            }
        }
        return false; // No valid access token cookie found
    }

    private boolean isJwtValid(String jwt) {
        try {
            // Split the JWT into its parts: header, payload, and signature
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                return false; // Invalid JWT format
            }

            // Decode the payload (second part of the JWT)
            String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);

            // Parse the JSON payload
            JSONObject payload = new JSONObject(payloadJson);

            // Extract the "exp" field and compare it to the current UTC time
            long exp = payload.getLong("exp");
            long currentUtcTime = System.currentTimeMillis() / 1000; // Convert milliseconds to seconds

            return exp > currentUtcTime; // Return true if the JWT has not expired
        } catch (Exception e) {
            e.printStackTrace();
            return false; // If any error occurs, consider the JWT invalid
        }
    }
}
