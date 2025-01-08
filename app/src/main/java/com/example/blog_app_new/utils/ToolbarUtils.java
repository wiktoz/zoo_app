package com.example.blog_app_new.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.blog_app_new.R;

public class ToolbarUtils {

    /**
     * Inicjalizuje Toolbar w Activity.
     *
     * @param activity  Kontekst Activity, w którym znajduje się Toolbar.
     * @param toolbarId Identyfikator Toolbara.
     * @param title     Tytuł do ustawienia w Toolbarze.
     */
    public static void setupToolbar(AppCompatActivity activity, int toolbarId, String title) {
        // Znajdź Toolbar
        Toolbar toolbar = activity.findViewById(toolbarId);
        activity.setSupportActionBar(toolbar);

        // Wyłącz domyślny tytuł ActionBar
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Ustaw tytuł Toolbara
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);

        // Ustaw akcję dla ikony użytkownika
        ImageView userIcon = toolbar.findViewById(R.id.toolbar_user_icon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przejście do widoku panelu użytkownika
                Context context = activity.getApplicationContext();
//                Intent intent = new Intent(context, UserPanelActivity.class);
//                activity.startActivity(intent);
            }
        });
    }
}
