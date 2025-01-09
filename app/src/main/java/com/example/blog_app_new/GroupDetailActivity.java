package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.Group;
import com.example.blog_app_new.CModels.Post;
import com.example.blog_app_new.CModels.PostsAdapter;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.CModels.AverageRatingResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity {
    private static final String TAG = "GroupDetailActivity";

    private TextView groupNameTextView, groupDescriptionTextView;
    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;

    private String groupId; // ID grupy
    private List<Post> groupPosts;
    private Map<String, Double> postRatings = new HashMap<>(); // Przechowuje oceny dla postów

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        findViewById(R.id.newPostTemplate).setOnClickListener(v -> {
            Intent intent = new Intent(GroupDetailActivity.this, AddPostActivity.class);
            intent.putExtra("group_id", groupId);
            startActivity(intent);
            finish();
        });

        groupId = getIntent().getStringExtra("group_id");
        if (groupId == null || groupId.isEmpty()) {
            finish();
            return;
        }

        // Inicjalizacja widoków
        groupNameTextView = findViewById(R.id.groupNameTextView);
        groupDescriptionTextView = findViewById(R.id.groupDescriptionTextView);
        postsRecyclerView = findViewById(R.id.postsRecyclerView);

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsAdapter = new PostsAdapter(new ArrayList<>(), postRatings);
        postsRecyclerView.setAdapter(postsAdapter);

        // Pobieranie szczegółów grupy i postów
        fetchGroupDetails();
        fetchGroupPosts();
    }

    private void fetchGroupDetails() {
        ApiService.getInstance().getApiEndpoint().getGroupDetails(groupId)
                .enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Group group = response.body();
                            groupNameTextView.setText(group.name);
                            groupDescriptionTextView.setText(group.description);
                            Log.d(TAG, "Group details loaded: " + group.name);
                        } else {
                            showError("Failed to load group details: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    private void fetchGroupPosts() {
        ApiService.getInstance().getApiEndpoint().getGroupPosts(groupId)
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            groupPosts = response.body();
                            postsAdapter.updateData(groupPosts);
                            fetchPostRatings();
                            Log.d(TAG, "Posts loaded for group: " + groupId);
                        } else {
                            showError("Failed to load group posts: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    private void fetchPostRatings() {
        for (Post post : groupPosts) {
            ApiService.getInstance().getApiEndpoint().getPostAverageRating(post.post_id)
                    .enqueue(new Callback<AverageRatingResponse>() {
                        @Override
                        public void onResponse(Call<AverageRatingResponse> call, Response<AverageRatingResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                postRatings.put(post.post_id, response.body().getAverage());
                                postsAdapter.notifyDataSetChanged(); // Aktualizujemy adapter po pobraniu oceny
                            }
                        }

                        @Override
                        public void onFailure(Call<AverageRatingResponse> call, Throwable t) {
                            Log.e(TAG, "Failed to fetch rating for post: " + post.post_id);
                        }
                    });
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, message);
    }
    @Override
    protected void onResume() {
        super.onResume();

        fetchGroupDetails();
        fetchGroupPosts();
    }
}
