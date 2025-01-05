package com.example.blog_app_new.CModels;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;

import java.util.List;
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
        holder.postUser.setText("Autor: " + post.user);
        holder.postContent.setText(post.content);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updateData(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postUser, postContent;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postUser = itemView.findViewById(R.id.postUser);
            postContent = itemView.findViewById(R.id.postContent);
        }
    }
}

