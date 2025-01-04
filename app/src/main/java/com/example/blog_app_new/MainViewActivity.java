package com.example.blog_app_new;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainViewActivity extends AppCompatActivity {

    private RecyclerView groupsRecyclerView;
    private GroupsAdapter adapter;
    private List<Group> allGroups; // Pełna lista grup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        // Inicjalizacja RecyclerView
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ładowanie grup
        allGroups = loadGroups();
        adapter = new GroupsAdapter(new ArrayList<>(allGroups)); // Początkowa lista to pełna lista
        groupsRecyclerView.setAdapter(adapter);

        // Pasek wyszukiwania
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nie robimy nic przed zmianą tekstu
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGroups(s.toString()); // Filtruj listę grup na podstawie tekstu
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nie robimy nic po zmianie tekstu
            }
        });
    }

    // Filtruje grupy na podstawie nazwy
    private void filterGroups(String query) {
        List<Group> filteredGroups = new ArrayList<>();
        for (Group group : allGroups) {
            if (group.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredGroups.add(group); // Dodaj grupy, które pasują do zapytania
            }
        }
        adapter.updateData(filteredGroups); // Zaktualizuj dane w adapterze
    }

    // Ładowanie grup (na razie statyczne)
    private List<Group> loadGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("Grupa 1", "Opis grupy 1"));
        groups.add(new Group("Grupa 2", "Opis grupy 2"));
        groups.add(new Group("Grupa 3", "Opis grupy 3"));
        groups.add(new Group("Grupa testowa", "Opis testowy"));
        return groups;
    }
}
