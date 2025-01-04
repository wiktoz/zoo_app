package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blog_app_new.models.RegisterRequest;
import com.example.blog_app_new.models.RegisterResponse;
import com.example.blog_app_new.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextSurname = findViewById(R.id.editTextSurname);

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
            String name = editTextName.getText().toString().trim();
            String surname = editTextSurname.getText().toString().trim();

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Hasła nie są takie same", Toast.LENGTH_SHORT).show();
            } else {
                ApiService.getInstance().getApiEndpoint().register(new RegisterRequest(name, surname, email, password))
                        .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        RegisterResponse registerResponse = response.body();

                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, registerResponse.message, Toast.LENGTH_SHORT).show();
                            Log.d("onRegisterSuccess", registerResponse.message);
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, registerResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("onRegisterFailure", call.toString());
                        Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
