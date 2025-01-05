
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

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private List<Group> groups;
    private OnGroupClickListener onGroupClickListener; // interfejs do przechwycenia kliknięć

    public GroupsAdapter(List<Group> groups, OnGroupClickListener onGroupClickListener) {
        this.groups = groups;
        this.onGroupClickListener = onGroupClickListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view, onGroupClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupName.setText(group.name);
        holder.groupDescription.setText(group.description);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void updateData(List<Group> newGroups) {
        this.groups = newGroups;
        notifyDataSetChanged();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName, groupDescription;

        public GroupViewHolder(@NonNull View itemView, OnGroupClickListener onGroupClickListener) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupDescription = itemView.findViewById(R.id.groupDescription);

            // Obsługa kliknięcia
            itemView.setOnClickListener(v -> {
                if (onGroupClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onGroupClickListener.onGroupClick(position);
                    }
                }
            });
        }
    }

    public interface OnGroupClickListener {
        void onGroupClick(int position);
    }
}

