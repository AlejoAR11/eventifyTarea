package com.prev.eventify;
import com.prev.eventify.UserHomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegisterUser;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btnRegisterUser.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            // Guardamos el nombre y correo
                            User user = new User(name, email);
                            database.getReference("usuarios").child(uid).setValue(user)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                                        new android.os.Handler().postDelayed(() -> {
                                            Toast.makeText(this, "Redirigiendo...", Toast.LENGTH_SHORT).show(); // <-- Añade esto
                                            Intent i = new Intent(RegisterUserActivity.this, UserHomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }, 1000);
                                    })



                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Clase modelo del usuario
    public static class User {
        public String nombre, correo;

        public User() {} // Constructor vacío requerido por Firebase

        public User(String nombre, String correo) {
            this.nombre = nombre;
            this.correo = correo;
        }
    }
}
