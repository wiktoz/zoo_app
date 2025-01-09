package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.networksModels.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private TextView userNameTextView, userSurnameTextView, userEmailTextView;
    private TextView userPostsCountTextView, userGroupsCountTextView, userCommentsCountTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        userNameTextView = findViewById(R.id.userNameTextView);
        userSurnameTextView = findViewById(R.id.userSurnameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userPostsCountTextView = findViewById(R.id.userPostsCountTextView);
        userGroupsCountTextView = findViewById(R.id.userGroupsCountTextView);
        userCommentsCountTextView = findViewById(R.id.userCommentsCountTextView);
        logoutButton = findViewById(R.id.logoutButton);

        // Fetch user data
        fetchUserDetails();

        // Handle logout
        logoutButton.setOnClickListener(v -> logout());
    }

    /**
     * Fetches user details from the backend and populates the UI.
     */
    private void fetchUserDetails() {
        ApiService.getInstance().getApiEndpoint().getUserDetails()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            populateUserData(response.body());
                        } else {
                            showError("Failed to fetch user details.");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    /**
     * Populates the UI with user data.
     *
     * @param user The user data object.
     */
    private void populateUserData(User user) {
        // Set user details
        userNameTextView.setText(user.getName());
        userSurnameTextView.setText(user.getSurname());
        userEmailTextView.setText(user.getEmail());

        // Set user statistics
        userPostsCountTextView.setText(String.format("Liczba postów: %d", user.getPosts().size()));
        userGroupsCountTextView.setText(String.format("Liczba grup: %d", user.getGroups().size()));
        userCommentsCountTextView.setText(String.format("Liczba komentarzy: %d", user.getComments().size()));
    }

    /**
     * Logs the user out and redirects to the LoginActivity.
     */
    private void logout() {
        ApiService.getInstance().getApiEndpoint().logout()
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Logout successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showError("Failed to log out.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, message);
    }


}
