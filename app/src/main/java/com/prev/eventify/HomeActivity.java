package com.prev.eventify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(v -> {
            // Aquí se llamará a la pantalla de selección de tipo de registro (usuario/organizador)
            // Por ahora, puedes dejarlo como un Toast o vacío
        });

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, RegisterSelectionActivity.class));
        });



    }
}
