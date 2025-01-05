
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

    private List<com.example.blog_app_new.CModels.Group> groups;

    public GroupsAdapter(List<com.example.blog_app_new.CModels.Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        Log.d("GroupsAdapter", "onBindViewHolder: position=" + position
                + ", groupName=" + group.getName());
        holder.groupName.setText(group.getName());
        holder.groupDescription.setText(group.getDescription());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void updateData(List<Group> newGroups) {
        this.groups = newGroups; // Aktualizacja danych
        notifyDataSetChanged();  // Odświeżenie widoku RecyclerView
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName, groupDescription;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupDescription = itemView.findViewById(R.id.groupDescription);
        }
    }
}