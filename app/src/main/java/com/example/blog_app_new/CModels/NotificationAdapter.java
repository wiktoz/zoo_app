package com.example.blog_app_new.CModels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.PostDetailActivity;
import com.example.blog_app_new.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        // Set content and timestamp
        holder.notificationContent.setText(notification.content);
        holder.notificationTimestamp.setText(notification.created_at);

        // Handle click on notification
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, PostDetailActivity.class);

            // Pass the post_id to PostDetailActivity
            intent.putExtra("post_id", notification.post_id);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView notificationContent;
        final TextView notificationTimestamp;

        ViewHolder(View itemView) {
            super(itemView);
            notificationContent = itemView.findViewById(R.id.notificationContent);
            notificationTimestamp = itemView.findViewById(R.id.notificationTimestamp);
        }
    }
}
