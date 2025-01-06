package com.example.blog_app_new.CModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.CModels.AverageRatingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.postTitle.setText(post.title);

        String userDate = "Autor: " + post.user;
        if (post.created_at != null) {
            String[] dateParts = post.created_at.split("T");
            userDate += " • " + (dateParts.length > 1 ? dateParts[0] : post.created_at);
        }
        holder.postUserDate.setText(userDate);
        holder.postContent.setText(post.content);

        // Pobieranie średniej oceny dla posta
        ApiService.getInstance().getApiEndpoint().getPostAverageRating(post.post_id)
                .enqueue(new Callback<AverageRatingResponse>() {
                    @Override
                    public void onResponse(Call<AverageRatingResponse> call, Response<AverageRatingResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            double average = response.body().getAverage();
                            holder.postAverageRating.setText("Średnia ocena: " + average);
                        } else {
                            holder.postAverageRating.setText("Średnia ocena: brak");
                        }
                    }

                    @Override
                    public void onFailure(Call<AverageRatingResponse> call, Throwable t) {
                        holder.postAverageRating.setText("Średnia ocena: błąd");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updateData(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postUserDate, postContent, postAverageRating;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postUserDate = itemView.findViewById(R.id.postUserDate);
            postContent = itemView.findViewById(R.id.postContent);
            postAverageRating = itemView.findViewById(R.id.postAverageRating);
        }
    }
}
