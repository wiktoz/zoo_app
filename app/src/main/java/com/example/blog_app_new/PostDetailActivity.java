package com.example.blog_app_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blog_app_new.CModels.Post;
import com.example.blog_app_new.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";

    private TextView postDetailTitle, postDetailUserDate, postDetailContent;
    // ewentualnie inne pola: rating, photos, comments...

    private String postId; // ID posta przekazane w intencji

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Odczytujemy post_id z Intent
        postId = getIntent().getStringExtra("post_id");
        if (postId == null || postId.isEmpty()) {
            finish();
            return;
        }

        initViews();
        fetchPostDetails(postId);
    }

    private void initViews() {
        postDetailTitle = findViewById(R.id.postDetailTitle);
        postDetailUserDate = findViewById(R.id.postDetailUserDate);
        postDetailContent = findViewById(R.id.postDetailContent);
        // ewentualnie inne findViewById
    }

    private void fetchPostDetails(String postId) {
        // Zakładam, że w ApiEndpoint masz np.:
        // @GET("posts/{post_uuid}")  Call<Post> getPostDetails(@Path("post_uuid") String postId);
        ApiService.getInstance().getApiEndpoint().getPostDetails(postId)
                .enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Post post = response.body();
                            Log.d(TAG, "Post details loaded: " + post.title);
                            showPostDetails(post);
                        } else {
                            showError("Failed to load post details. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    private void showPostDetails(Post post) {
        postDetailTitle.setText(post.title);

        // ewentualnie ładne formatowanie daty
        String userDateString = post.user + " | " + post.created_at;
        postDetailUserDate.setText(userDateString);

        postDetailContent.setText(post.content);

        // jeśli chcesz jeszcze oceny, zdjęcia, komentarze - wyświetl lub pobierz
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);
    }
}
