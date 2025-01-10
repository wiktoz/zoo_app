package com.example.blog_app_new.CModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.PostDetailActivity; // Upewnij się, że nazwa i ścieżka się zgadzają
import com.example.blog_app_new.R;

import java.util.List;
import java.util.Map;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> posts;
    private Map<String, Double> postRatings; // Mapa ocen

    public PostsAdapter(List<Post> posts, Map<String, Double> postRatings) {
        this.posts = posts;
        this.postRatings = postRatings;
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

        // Tytuł
        holder.postTitle.setText(post.title);

        // Autor + data
        String userDate = "Autor: " + post.user;
        if (post.created_at != null) {
            String[] dateParts = post.created_at.split("T");
            userDate += " • " + (dateParts.length > 1 ? dateParts[0] : post.created_at);
        }
        holder.postUserDate.setText(userDate);

        // Treść
        holder.postContent.setText(post.content);

        // Średnia ocena
        Double rating = postRatings.get(post.post_id);
        if (rating != null) {
            holder.postAverageRating.setText("Ocena: " + String.format("%.1f", rating));
            holder.postRatingBar.setRating(rating.floatValue());
        } else {
            holder.postAverageRating.setText("Brak ocen");
            holder.postRatingBar.setRating(0);
        }

        // Kliknięcie w cały item -> otwarcie PostDetailActivity z przekazaniem post_id
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("post_id", post.post_id);
            context.startActivity(intent);
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
        RatingBar postRatingBar;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postUserDate = itemView.findViewById(R.id.postUserDate);
            postContent = itemView.findViewById(R.id.postContent);
            postAverageRating = itemView.findViewById(R.id.postAverageRating);
            postRatingBar = itemView.findViewById(R.id.postRatingBar);
        }
    }
}
