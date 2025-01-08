package com.example.blog_app_new.CModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.blog_app_new.CModels.Comment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;


import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> comments; // klasa Comment z polami comment_id, user_id, content, created_at...

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment c = comments.get(position);
        holder.commentUserId.setText("Użytkownik: " + c.username);
        holder.commentCreatedAt.setText("Data: " + c.created_at);
        holder.commentContent.setText(c.content);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void updateData(List<Comment> newComments) {
        this.comments = newComments;
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUserId, commentCreatedAt, commentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserId = itemView.findViewById(R.id.commentUserId);
            commentCreatedAt = itemView.findViewById(R.id.commentCreatedAt);
            commentContent = itemView.findViewById(R.id.commentContent);
        }
    }
}
