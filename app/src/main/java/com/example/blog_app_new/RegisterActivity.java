package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);



        TextView textViewLogin = findViewById(R.id.textViewLogin);

        // Navigate to RegisterActivity
        textViewLogin.setOnClickListener(v -> {
            finish();
        });


        buttonRegister.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Hasła nie pasują", Toast.LENGTH_SHORT).show();
            } else {
                // Symulacja rejestracji użytkownika
                Toast.makeText(RegisterActivity.this, "Rejestracja zakończona sukcesem", Toast.LENGTH_SHORT).show();
                finish(); // Zakończ i wróć do ekranu logowania
            }
        });
    }
}
