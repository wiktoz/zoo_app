package com.example.blog_app_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.Group;
import com.example.blog_app_new.CModels.Post;
import com.example.blog_app_new.CModels.PostsAdapter;
import com.example.blog_app_new.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity {
    private static final String TAG = "GroupDetailActivity";

    private TextView groupNameTextView, groupDescriptionTextView;
    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;

    private String groupId; // ID wybranej grupy
    private Group currentGroup;
    private List<Post> groupPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        // 1. Odczytujemy ID grupy z intencji
        groupId = getIntent().getStringExtra("group_id");
        if (groupId.equals("")) {
            finish(); // Brak właściwego ID -> zamykamy aktywność
            return;
        }

        // 2. Inicjalizacja widoków
        groupNameTextView = findViewById(R.id.groupNameTextView);
        groupDescriptionTextView = findViewById(R.id.groupDescriptionTextView);
        postsRecyclerView = findViewById(R.id.postsRecyclerView);

        // Ustawienie RecyclerView
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsAdapter = new PostsAdapter(new ArrayList<>()); // Twój adapter do postów
        postsRecyclerView.setAdapter(postsAdapter);

        // 3. Pobieramy szczegóły grupy (nazwa, opis, lista ID postów)
        loadGroupDetails();

        // 4. Pobieramy pełne posty tej grupy
        loadGroupPosts();
    }

    private void loadGroupDetails() {
        ApiService.getInstance().getApiEndpoint().getGroupDetails(groupId)
                .enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            currentGroup = response.body();
                            // Ustaw w UI nazwę i opis
                            groupNameTextView.setText(currentGroup.name);
                            groupDescriptionTextView.setText(currentGroup.description);

                            Log.d(TAG, "Group details loaded: " + currentGroup.name
                                    + " (Posts IDs: " + currentGroup.posts + ")");
                        } else {
                            Log.e(TAG, "Failed to load group detail: " + response.code());
                            Toast.makeText(GroupDetailActivity.this, "Failed to load group detail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                        Toast.makeText(GroupDetailActivity.this, "Network error while loading group detail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadGroupPosts() {
        // Tu używasz endpointu: GET /api/groups/<group_id>/posts
        ApiService.getInstance().getApiEndpoint().getGroupPosts(groupId)
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            groupPosts = response.body();
                            postsAdapter.updateData(groupPosts);
                            Log.d(TAG, "Posts loaded for group=" + groupId + " size=" + groupPosts.size());
                        } else {
                            Log.e(TAG, "Failed to load group posts: " + response.code());
                            Toast.makeText(GroupDetailActivity.this, "Failed to load group posts", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                        Toast.makeText(GroupDetailActivity.this, "Failed to load posts due to network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
