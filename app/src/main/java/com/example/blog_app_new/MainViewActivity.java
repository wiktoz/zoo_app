package com.example.blog_app_new;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainViewActivity extends AppCompatActivity {

    private RecyclerView groupsRecyclerView;
    private List<Group> groups = new ArrayList<>();
    private GroupsAdapter groupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        // Initialize RecyclerView
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load groups from JSON
        loadGroupsFromJson();

        // Set Adapter
        groupsAdapter = new GroupsAdapter(groups);
        groupsRecyclerView.setAdapter(groupsAdapter);
    }

    private void loadGroupsFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.groups);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            reader.close();
            inputStream.close();

            // Parse JSON
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                groups.add(new Group(name, description));
            }

            // Log loaded groups
            for (Group group : groups) {
                Log.d("MainViewActivity", "Group: " + group.getName() + ", Description: " + group.getDescription());
            }

        } catch (Exception e) {
            Log.e("MainViewActivity", "Error loading groups", e);
        }
    }
}
