package com.example.blog_app_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.Group;
import com.example.blog_app_new.CModels.GroupsAdapter;
import com.example.blog_app_new.databinding.ActivityMainBinding;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.network.MyCookieJar;
import com.example.blog_app_new.networksModels.GroupResponse;
import com.example.blog_app_new.networksModels.LoginRequest;
import com.example.blog_app_new.networksModels.LoginResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//    "password": "strongpassword123",
//        "email": "x@test.pl"

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    // Pola przeniesione z MainViewActivity
    private RecyclerView groupsRecyclerView;
    private GroupsAdapter adapter;
    private List<Group> allGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Sprawdzenie logowania
        if (!MyCookieJar.getInstance().isLogged()) {
            Log.d(TAG, "User is not logged in. Redirecting to LoginActivity...");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        Log.d(TAG, "User is logged in.");
        initGroupList();
    }


    /**
     * Metoda łącząca logikę wyświetlania grup z layoutem (RecyclerView, EditText)
     */
    private void initGroupList() {
        // RecyclerView
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicjalizujemy adapter z pustą listą
        adapter = new GroupsAdapter(new ArrayList<>(), position -> {
            // Tu obsługujemy kliknięcie w grupę
            Group clickedGroup = allGroups.get(position);
            Log.d(TAG, "Clicked group: " + clickedGroup.name + " (id=" + clickedGroup.group_id + ")");

            // Uruchamiamy GroupDetailActivity, przekazując group_id
            Intent intent = new Intent(MainActivity.this, GroupDetailActivity.class);
            intent.putExtra("group_id", clickedGroup.group_id);
            startActivity(intent);
        });
        groupsRecyclerView.setAdapter(adapter);

        // Ładujemy grupy z API
        loadGroups();

        // Obsługa wyszukiwania
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nie robimy nic
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGroups(s.toString()); // Filtruj listę na podstawie wpisanego tekstu
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                // Nie robimy nic
            }
        });
    }


    /**
     * Filtruje grupy na podstawie tekstu wyszukiwania
     */
    private void filterGroups(String query) {
        List<Group> filteredGroups = new ArrayList<>();
        for (Group group : allGroups) {
            if (group.name.toLowerCase().contains(query.toLowerCase())) {
                filteredGroups.add(group);
            }
        }
        adapter.updateData(filteredGroups);
    }

    /**
     * Wczytuje listę grup (na razie statycznie)
     */
    private void loadGroups() {
        // Inicjalizujemy pustą listę na wypadek braku odpowiedzi
        allGroups = new ArrayList<>();

        ApiService.getInstance().getApiEndpoint().getGroups()
                .enqueue(new Callback<List<Group>>() {
                    @Override
                    public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allGroups = response.body();
                            Log.d(TAG, "Groups loaded successfully: " + allGroups.size());
                            adapter.updateData(allGroups); // Aktualizujemy dane adaptera po wczytaniu
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
     * Sprawdza, czy użytkownik jest zalogowany i ew. przekierowuje do LoginActivity
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Dodanie menu do action bara
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Jeśli nie masz w layoutach nav_host_fragment_content_main, usuń poniższe linie
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
