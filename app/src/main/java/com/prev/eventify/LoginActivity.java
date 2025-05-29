package com.prev.eventify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();

                            // Primero revisamos si es usuario
                            database.getReference("usuarios").child(uid).get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            startActivity(new Intent(this, UserHomeActivity.class));
                                            finish();
                                        } else {
                                            // Si no es usuario, revisamos si es organizador
                                            database.getReference("organizadores").child(uid).get()
                                                    .addOnSuccessListener(snapshot2 -> {
                                                        if (snapshot2.exists()) {
                                                            startActivity(new Intent(this, OrganizerHomeActivity.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(this, "Rol no identificado", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Toast.makeText(this, "Error al buscar organizador", Toast.LENGTH_SHORT).show());
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error al buscar usuario", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
