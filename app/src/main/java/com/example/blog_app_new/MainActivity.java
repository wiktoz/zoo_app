package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.Group;
import com.example.blog_app_new.CModels.GroupsAdapter;
import com.example.blog_app_new.CModels.Notification;
import com.example.blog_app_new.CModels.NotificationAdapter;
import com.example.blog_app_new.databinding.ActivityMainBinding;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.network.MyCookieJar;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//    "password": "strongpassword123",
//        "email": "x@test.pl"

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    // RecyclerView i adapter
    private RecyclerView groupsRecyclerView;
    private GroupsAdapter groupAdapter;
    private NotificationAdapter notificationAdapter;

    private List<Group> allGroups;
    private List<Notification> notifications;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sprawdzenie logowania
        if (!MyCookieJar.getInstance().isLogged()) {
            Log.d(TAG, "User is not logged in. Redirecting to LoginActivity...");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        Log.d(TAG, "User is logged in.");

        // Inicjalizacja listy
        initGroupList();
        setupNotificationButton();

        // Ewentualna obsługa przycisku do widoku "AllGroupsActivity"
        Button goToAllGroupsBtn = findViewById(R.id.goToAllGroupsBtn);
        goToAllGroupsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AllGroupsActivity.class));
        });

        // Ewentualna obsługa powiadomień – pomijamy dla uproszczenia
    }

    /**
     * Metoda łącząca logikę wyświetlania grup z layoutem (RecyclerView, EditText)
     */
    private void initGroupList() {
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ustawiamy adapter z pustą listą i showJoinButton = false
        groupAdapter = new GroupsAdapter(
                new ArrayList<>(),
                new GroupsAdapter.OnGroupClickListener() {
                    @Override
                    public void onGroupClick(Group clickedGroup) {
                        // Kliknięcie w grupę -> szczegóły
                        Log.d(TAG, "Clicked group: " + clickedGroup.name + " (id=" + clickedGroup.group_id + ")");

                        Intent intent = new Intent(MainActivity.this, GroupDetailActivity.class);
                        intent.putExtra("group_id", clickedGroup.group_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onJoinGroupClick(Group group) {
                        // W MainActivity nie potrzebujemy "Dołącz do grupy"
                        // Zostawiamy puste (lub cokolwiek chcesz)
                    }
                },
                false // W tym widoku nie pokazujemy przycisku "Dołącz"
        );
        groupsRecyclerView.setAdapter(groupAdapter);

        // Ładujemy grupy z API
        loadGroups();

        // Obsługa wyszukiwania
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGroups(s.toString());
            }
            @Override
            public void afterTextChanged(android.text.Editable s) { }
        });
    }

    /**
     * Pobiera listę grup, do których user należy
     */
    private void loadGroups() {
        allGroups = new ArrayList<>();

        // W Twoim ApiEndpoint pewnie jest getGroups(), które zwraca grupy usera
        ApiService.getInstance().getApiEndpoint().getGroups()
                .enqueue(new Callback<List<Group>>() {
                    @Override
                    public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allGroups = response.body();
                            Log.d(TAG, "Groups loaded successfully: " + allGroups.size());
                            groupAdapter.updateData(allGroups);
                        } else {
                            Log.e(TAG, "Failed to load groups: " + response.code());
                            Toast.makeText(MainActivity.this, "Failed to load groups", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Group>> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                        Toast.makeText(MainActivity.this, "Failed to load groups due to network error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Proste filtrowanie listy grup
     */
    private void filterGroups(String query) {
        if (allGroups == null) return;
        List<Group> filteredGroups = new ArrayList<>();
        for (Group group : allGroups) {
            if (group.name.toLowerCase().contains(query.toLowerCase())
                    || group.description.toLowerCase().contains(query.toLowerCase())) {
                filteredGroups.add(group);
            }
        }
        groupAdapter.updateData(filteredGroups);
    }

    private void setupNotificationButton() {
        FrameLayout notificationCircle = findViewById(R.id.notificationCircle);
        TextView notificationCount = findViewById(R.id.notificationCount);

        // Initialize empty list for notifications
        List<Notification> notifications = new ArrayList<>();

        // Fetch notifications from API
        ApiService.getInstance().getApiEndpoint().getNotifications()
                .enqueue(new Callback<List<Notification>>() {
                    @Override
                    public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Update notifications list
                            notifications.clear();
                            notifications.addAll(response.body());

                            // Update notification count
                            notificationCount.setText(String.valueOf(notifications.size()));

                            Log.d(TAG, "Notifications loaded successfully: " + notifications.size());
                        } else {
                            Log.e(TAG, "Failed to load notifications: " + response.code());
                            Toast.makeText(MainActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Notification>> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                        Toast.makeText(MainActivity.this, "Failed to load notifications due to network error", Toast.LENGTH_SHORT).show();
                    }
                });

        // Handle button click
        notificationCircle.setOnClickListener(v -> {
            if (!notifications.isEmpty()) {
                showNotificationModal(notifications);
            } else {
                Toast.makeText(this, "No notifications available", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showNotificationModal(List<Notification> notifications) {
        // Inflate the modal layout
        View modalView = getLayoutInflater().inflate(R.layout.notification_modal, null);

        // Initialize RecyclerView inside the modal
        RecyclerView notificationRecyclerView = modalView.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter with a click listener
        NotificationAdapter adapter = new NotificationAdapter(notifications, notification -> {
            // Handle notification click
            Intent intent = new Intent(MainActivity.this, PostDetailActivity.class);
            intent.putExtra("post_id", notification.post_id); // Assuming getPostId() provides the ID
            startActivity(intent);
        });
        notificationRecyclerView.setAdapter(adapter);

        // Show modal dialog
        new android.app.AlertDialog.Builder(this)
                .setView(modalView)
                .setCancelable(true)
                .show();
    }



    /**
     * Sprawdza, czy użytkownik jest zalogowany i ew. przekierowuje do LoginActivity
     */


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() - refreshing groups...");
        loadGroups(); // Twoja metoda do pobrania listy grup i update'u adaptera
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            // Otwórz ProfileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
