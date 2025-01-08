package com.example.blog_app_new;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.ImagesAdapter;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.networksModels.ApiResponse;
import com.example.blog_app_new.networksModels.ErrorResponse;
import com.example.blog_app_new.networksModels.LoginRequest;
import com.example.blog_app_new.networksModels.LoginResponse;
import com.example.blog_app_new.networksModels.PostRequest;
import com.example.blog_app_new.utils.ImageConverter;
import com.example.blog_app_new.utils.ToolbarUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {
    private EditText titleEditText, contentEditText;
    private Button addImageButton, submitPostButton;
    private RecyclerView imagesRecyclerView;

    private ImagesAdapter imagesAdapter;
    private String groupId;

    private String groupName;
    private final List<Uri> selectedImages = new ArrayList<>();

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
                if (uris != null && !uris.isEmpty()) {
                    selectedImages.addAll(uris);
                    imagesAdapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        addImageButton = findViewById(R.id.addImageButton);
        submitPostButton = findViewById(R.id.submitPostButton);
        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);

        groupId = getIntent().getStringExtra("group_id");
        groupName = getIntent().getStringExtra("group_name");


        if (groupId == null || groupId.isEmpty()) {
            Toast.makeText(this, "Group ID is missing!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the group ID is not available
            return;
        }

        ToolbarUtils.setupToolbar(AddPostActivity.this, R.id.custom_toolbar, "Nowy post w " + groupName);


        // Ustawienie adaptera dla RecyclerView
        imagesAdapter = new ImagesAdapter(selectedImages);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 kolumny
        imagesRecyclerView.setAdapter(imagesAdapter);

        // Obsługa przycisku dodawania zdjęć
        addImageButton.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // Obsługa przycisku dodawania posta
        submitPostButton.setOnClickListener(v -> submitPost());
    }

    private void submitPost() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> base64Images = ImageConverter.convertUrisToBase64(selectedImages, getContentResolver());

        ApiService.getInstance().getApiEndpoint().createPost(groupId, new PostRequest(title, content, base64Images))
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddPostActivity.this, "Post created!", Toast.LENGTH_SHORT).show();
                        } else {
                            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                            if (errorResponse.message != null) {
                                Toast.makeText(AddPostActivity.this, "Post creation failed! " + errorResponse.message, Toast.LENGTH_SHORT).show();
                            }
                            if (errorResponse.msg != null) {
                                Toast.makeText(AddPostActivity.this, "Post creation failed! " + errorResponse.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(AddPostActivity.this, "Post creation failed due to a network error", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(AddPostActivity.this, GroupDetailActivity.class);
        intent.putExtra("group_id", groupId);
        startActivity(intent);
        finish();
    }
}
