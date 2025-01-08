package com.example.blog_app_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.Comment;
import com.example.blog_app_new.CModels.CommentsAdapter;
import com.example.blog_app_new.CModels.Post;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.utils.ToolbarUtils;
import com.example.blog_app_new.networksModels.ApiResponse;
import com.example.blog_app_new.networksModels.CommentRequest;
import com.example.blog_app_new.networksModels.RateRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";
    private String groupName;

    private TextView postDetailTitle, postDetailUserDate, postDetailContent;
    // ewentualnie inne pola: rating, photos, comments...

    private String postId; // ID posta przekazane w intencji
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList = new ArrayList<>();

    private EditText commentContentEditText;
    private RatingBar commentRatingBar;
    private Button addCommentButton;


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
        groupName = getIntent().getStringExtra("group_name");
        ToolbarUtils.setupToolbar(PostDetailActivity.this, R.id.custom_toolbar, "Post w " + groupName);

        initViews();
        fetchPostDetails(postId);
    }

    private void initViews() {
        postDetailTitle = findViewById(R.id.postDetailTitle);
        postDetailUserDate = findViewById(R.id.postDetailUserDate);
        postDetailContent = findViewById(R.id.postDetailContent);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsAdapter = new CommentsAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        commentContentEditText = findViewById(R.id.commentContentEditText);
        commentRatingBar = findViewById(R.id.commentRatingBar);
        addCommentButton = findViewById(R.id.addCommentButton);

        addCommentButton.setOnClickListener(v -> submitCommentAndRating());

        // ewentualnie inne findViewById
    }
    private void submitCommentAndRating() {
        String content = commentContentEditText.getText().toString().trim();
        float ratingValue = commentRatingBar.getRating(); // 0–5

        if (content.isEmpty()) {
            Toast.makeText(this, "Wpisz treść komentarza", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Dodaj komentarz (POST /api/posts/{post_uuid}/comments)
        addComment(content, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostDetailActivity.this, "Dodano komentarz", Toast.LENGTH_SHORT).show();
                    // Po udanym komentarzu, odśwież komentarze
                    fetchComments();

                    // 2. Jeśli ratingValue > 0, oceń post (POST /api/posts/{post_uuid}/rate)
                    if (ratingValue > 0) {
                        ratePost((int) ratingValue, new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response2) {
                                if (response2.isSuccessful()) {
                                    Toast.makeText(PostDetailActivity.this, "Dodano ocenę postu", Toast.LENGTH_SHORT).show();
                                } else {
                                    showError("Nie udało się dodać oceny. Code: " + response2.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                showError("Błąd sieci przy ocenie postu: " + t.getMessage());
                            }
                        });
                    }

                    // Wyczyść pole tekstowe i rating
                    commentContentEditText.setText("");
                    commentRatingBar.setRating(0);

                } else {
                    showError("Nie udało się dodać komentarza. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showError("Błąd sieci przy dodawaniu komentarza: " + t.getMessage());
            }
        });
    }
    private void addComment(String content, Callback<ApiResponse> callback) {
        // W backendzie: POST /api/posts/{post_uuid}/comments
        // Body: {"content": "..."}
        CommentRequest commentRequest = new CommentRequest(content);

        ApiService.getInstance().getApiEndpoint()
                .addCommentToPost(postId, commentRequest)
                .enqueue(callback);
    }

    private void ratePost(int value, Callback<ApiResponse> callback) {
        // W backendzie: POST /api/posts/{post_uuid}/rate
        // Body: {"value": ...}
        RateRequest rateRequest = new RateRequest(value);

        ApiService.getInstance().getApiEndpoint()
                .ratePost(postId, rateRequest)
                .enqueue(callback);
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
    private void fetchComments() {
        ApiService.getInstance().getApiEndpoint().getCommentsForPost(postId)
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            commentList = response.body();
                            commentsAdapter.updateData(commentList);
                            for (Comment comment: commentList) {
                                Log.d(TAG, "Post details: " + comment.toString());
                            }
                                                   } else {
                            showError("Failed to load comments. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        showError("Network error while loading comments: " + t.getMessage());
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
    @Override
    protected void onResume() {
        super.onResume();
        // Gdy aktywność znów jest widoczna – odśwież dane posta i komentarze
        fetchPostDetails(postId);
        fetchComments();
    }
}
