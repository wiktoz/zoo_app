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

    private String groupId; // ID grupy
    private List<Post> groupPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

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
        postsAdapter = new PostsAdapter(new ArrayList<>());
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

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, message);
    }
}
