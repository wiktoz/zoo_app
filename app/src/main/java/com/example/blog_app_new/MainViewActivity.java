package com.example.blog_app_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

public class MainViewActivity extends AppCompatActivity {

    private static final String TAG = "MainViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view); // Nowy XML dla głównego widoku

        // Setup toolbar or any other initialization logic here
        checkIfLoggedIn();
    }

    private void checkIfLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Redirect to MainActivity (Login)
            Log.d(TAG, "User is not logged in. Redirecting to MainActivity...");
            Intent intent = new Intent(MainViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
