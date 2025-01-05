package com.example.blog_app_new.CModels;

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
        // Inflatujemy layout item_post, tworząc "kafelek" dla pojedynczego posta
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Ustawiamy tytuł
        holder.postTitle.setText(post.title);

        // Łączymy autora i datę w jednym wierszu
        String userDate = "Autor: " + post.user;
        if (post.created_at != null) {
            // Proste wycięcie daty z formatu "YYYY-MM-DDT..."
            // (warto byłoby użyć parsera i formatowania w realnym projekcie)
            String[] dateParts = post.created_at.split("T");
            if (dateParts.length > 1) {
                userDate += " • " + dateParts[0] + " " + dateParts[1];
            } else {
                userDate += " • " + post.created_at;
            }
        }
        holder.postUserDate.setText(userDate);

        // Ustawiamy treść
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

    // Klasa ViewHolder: przechowuje referencje do widoków w item_post.xml
    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postUserDate, postContent;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postUserDate = itemView.findViewById(R.id.postUserDate);
            postContent = itemView.findViewById(R.id.postContent);
        }
    }
}
