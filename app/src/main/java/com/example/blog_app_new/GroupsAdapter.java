package com.example.blog_app_new;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> implements android.widget.Filterable {

    private List<Group> groups;
    private List<Group> filteredGroups; // Lista do przechowywania wyników filtrowania

    public GroupsAdapter(List<Group> groups) {
        this.groups = groups;
        this.filteredGroups = new ArrayList<>(groups); // Na początku wszystkie grupy są widoczne
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = filteredGroups.get(position); // Wyświetlaj tylko przefiltrowane grupy
        holder.groupName.setText(group.getName());
        holder.groupDescription.setText(group.getDescription());
    }

    @Override
    public int getItemCount() {
        return filteredGroups.size(); // Zwracaj liczbę przefiltrowanych grup
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        TextView groupDescription;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupDescription = itemView.findViewById(R.id.groupDescription);
        }
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                List<Group> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = groups; // Jeśli nie ma zapytania, zwracamy wszystkie grupy
                } else {
                    for (Group group : groups) {
                        if (group.getName().toLowerCase().contains(query)) {
                            filtered.add(group); // Dodajemy grupy, które pasują do zapytania
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredGroups = (List<Group>) results.values; // Aktualizujemy przefiltrowaną listę
                notifyDataSetChanged(); // Odświeżamy RecyclerView
            }
        };
    }
}
