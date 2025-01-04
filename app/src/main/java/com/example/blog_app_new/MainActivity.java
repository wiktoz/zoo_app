package com.example.blog_app_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.blog_app_new.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Tag for logging
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        // Clear SharedPreferences for testing purposes
        clearLoginStatus();

        // Log SharedPreferences data
        logSharedPreferences();

        // Check login status and redirect if needed
        checkLoginStatus();
    }

    /**
     * Clears the login status for testing purposes.
     */
    private void clearLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Remove all data from SharedPreferences
        editor.apply();
        Log.d(TAG, "Login status has been cleared.");
    }

    /**
     * Logs SharedPreferences data for debugging purposes.
     */
    private void logSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        Log.d(TAG, "SharedPreferences -> isLoggedIn: " + isLoggedIn);
    }

    /**
     * Checks whether the user is logged in and redirects to LoginActivity if not.
     */
    private void checkLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Ensure the user is not logged in by default
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        Log.d(TAG, "Login status (default false): " + isLoggedIn);

        if (!isLoggedIn) {
            Log.d(TAG, "User is not logged in. Redirecting to LoginActivity...");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity
        } else {
            Log.d(TAG, "User is logged in.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
