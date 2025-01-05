package com.example.blog_app_new.CModels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;

import java.util.List;

/**
 * Adapter do wyświetlania kafelków grup w RecyclerView.
 * Umożliwia pokazanie/ukrycie przycisku "Dołącz" (showJoinButton)
 * oraz obsługę kliknięcia w item i kliknięcia w przycisk dołączenia.
 */
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private List<Group> groups;
    private OnGroupClickListener onGroupClickListener;
    private boolean showJoinButton; // decyduje, czy przycisk "Dołącz" ma być widoczny

    /**
     * Konstruktor adaptera
     * @param groups lista grup do wyświetlenia
     * @param onGroupClickListener interfejs obsługujący kliknięcia
     * @param showJoinButton czy pokazywać przycisk "Dołącz do grupy"
     */
    public GroupsAdapter(List<Group> groups,
                         OnGroupClickListener onGroupClickListener,
                         boolean showJoinButton) {
        this.groups = groups;
        this.onGroupClickListener = onGroupClickListener;
        this.showJoinButton = showJoinButton;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view, onGroupClickListener, showJoinButton);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupName.setText(group.name);
        holder.groupDescription.setText(group.description);
        // Tutaj ewentualnie można ustawiać inne pola, np. obrazek
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    /**
     * Metoda do aktualizacji danych (np. po pobraniu z API)
     */
    public void updateData(List<Group> newGroups) {
        this.groups = newGroups;
        notifyDataSetChanged();
    }

    /**
     * Interfejs do obsługi kliknięć: w cały item i w przycisk dołączenia.
     */
    public interface OnGroupClickListener {
        void onGroupClick(int position);
        void onJoinGroupClick(int position);
    }

    /**
     * ViewHolder – przechowuje odniesienia do widoków w layoucie group_item
     */
    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName, groupDescription;
        TextView joinGroupBtn; // Przycisk "Dołącz"

        public GroupViewHolder(@NonNull View itemView,
                               OnGroupClickListener onGroupClickListener,
                               boolean showJoinButton) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupDescription = itemView.findViewById(R.id.groupDescription);
            joinGroupBtn = itemView.findViewById(R.id.joinGroupBtn);

            // Obsługa widoczności przycisku
            if (!showJoinButton) {
                // W danym widoku nie chcemy przycisku
                joinGroupBtn.setVisibility(View.GONE);
            } else {
                // W tym widoku przycisk ma być widoczny
                joinGroupBtn.setVisibility(View.VISIBLE);

                // Kliknięcie w przycisk "Dołącz"
                joinGroupBtn.setOnClickListener(v -> {
                    if (onGroupClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onGroupClickListener.onJoinGroupClick(position);
                        }
                    }
                });
            }

            // Kliknięcie w cały kafelek (przejście do szczegółów)
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
}
