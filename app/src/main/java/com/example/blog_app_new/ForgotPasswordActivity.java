package com.example.blog_app_new;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Wprowadź e-mail", Toast.LENGTH_SHORT).show();
            } else {
                // Symulacja wysłania e-maila resetującego hasło
                Toast.makeText(ForgotPasswordActivity.this, "Link resetujący wysłany na: " + email, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
