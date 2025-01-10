package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.CModels.Group;
import com.example.blog_app_new.CModels.GroupsAdapter;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.networksModels.ApiResponse;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Aktywność wyświetlająca WSZYSTKIE grupy (niezależnie, czy user do nich należy czy nie).
 */
public class AllGroupsActivity extends AppCompatActivity {

    private static final String TAG = "AllGroupsActivity";

    private RecyclerView allGroupsRecyclerView;
    private GroupsAdapter allGroupsAdapter;
    private List<Group> allGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_groups);

        initAllGroupsList();
    }

    /**
     * Inicjalizacja RecyclerView i adaptera
     * plus obsługa wyszukiwania i pobranie danych z endpointu getAllGroups().
     */
    private void initAllGroupsList() {
        allGroupsRecyclerView = findViewById(R.id.allGroupsRecyclerView);
        allGroupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicjalizujemy adapter (używamy takiego samego co w MainActivity, bo wygląd itemu jest podobny)
        allGroupsAdapter = new GroupsAdapter(new ArrayList<>(), new GroupsAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(Group clickedGroup) {
                // przejście do szczegółów
                Intent intent = new Intent(AllGroupsActivity.this, GroupDetailActivity.class);
                intent.putExtra("group_id", clickedGroup.group_id);
                startActivity(intent);
            }

            @Override
            public void onJoinGroupClick(Group groupToJoin) {
                // Wywołanie endpointu joinGroup
                joinGroup(groupToJoin.group_id,0);
            }
        },
                true  // <-- showJoinButton = true (przycisk widoczny)
        );


        allGroupsRecyclerView.setAdapter(allGroupsAdapter);

        // Pobieramy WSZYSTKIE grupy z API
        loadAllGroups();

        // Obsługa wyszukiwania
        EditText searchAllGroups = findViewById(R.id.searchAllGroups);
        searchAllGroups.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAllGroups(s.toString());
            }
            @Override
            public void afterTextChanged(android.text.Editable s) { }
        });
    }

    /**
     * Wywołanie endpointu getAllGroups()
     */
    private void loadAllGroups() {
        allGroups = new ArrayList<>();
        ApiService.getInstance().getApiEndpoint().getAllGroups()
                .enqueue(new Callback<List<Group>>() {
                    @Override
                    public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allGroups = response.body();
                            Log.d(TAG, "All Groups loaded: " + allGroups.size());
                            allGroupsAdapter.updateData(allGroups);
                        } else {
                            Log.e(TAG, "Failed to load all groups: " + response.code());
                            Toast.makeText(AllGroupsActivity.this, "Failed to load all groups", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Group>> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                        Toast.makeText(AllGroupsActivity.this, "Network error loading all groups", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    /**
     * Filtr tekstowy
     */
    private void filterAllGroups(String query) {
        if (allGroups == null) return;
        List<Group> filtered = new ArrayList<>();
        for (Group g : allGroups) {
            if (g.name.toLowerCase().contains(query.toLowerCase() || g.description.toLowerCase().contains(query.toLowerCase() )
                    || g.description.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(g);
            }
        }
        allGroupsAdapter.updateData(filtered);
    }
    private void joinGroup(String groupId, int position) {
        // Załóżmy, że w ApiEndpoint masz:
        // @GET("groups/{group_id}/join")
        // Call<ApiResponse> joinGroup(@Path("group_id") String groupId);

        ApiService.getInstance().getApiEndpoint().joinGroup(groupId)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(AllGroupsActivity.this,
                                    "Dołączyłeś do grupy " + allGroups.get(position).name,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AllGroupsActivity.this,
                                    "Nie udało się dołączyć do grupy",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(AllGroupsActivity.this,
                                "Błąd sieci przy dołączaniu",
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
