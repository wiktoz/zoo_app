package com.example.blog_app_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blog_app_new.models.LoginResponse;
import com.example.blog_app_new.models.RegisterRequest;
import com.example.blog_app_new.models.RegisterResponse;
import com.example.blog_app_new.network.ApiService;
import com.example.blog_app_new.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                        .enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                            System.out.println(response.body());
                            finish();
                        }
                        else {
                            Log.d("onFailure", response.body().toString());

                            Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się" + response.body(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("onFailure", call.toString());
                        Log.d("onFailure", String.valueOf(t));
                        Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
