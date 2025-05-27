package com.prev.eventify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterSelectionActivity extends AppCompatActivity {

    private Button btnUser, btnOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_selection);

        btnUser = findViewById(R.id.btnUser);
        btnOrganizer = findViewById(R.id.btnOrganizer);

        btnUser.setOnClickListener(v -> {
            // Aquí irá el intent hacia RegisterUserActivity
            // Por ahora mostramos un mensaje o dejas pendiente
        });

        btnOrganizer.setOnClickListener(v -> {
            // Aquí irá el intent hacia RegisterOrganizerActivity
            // Por ahora mostramos un mensaje o dejas pendiente
        });

        btnUser.setOnClickListener(v -> {
            startActivity(new Intent(RegisterSelectionActivity.this, RegisterUserActivity.class));
        });


    }
}
