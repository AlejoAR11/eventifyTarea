package com.prev.eventify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterOrganizerActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etCompany, etPhone;
    private Button btnRegisterOrganizer;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_organizer);

        etName = findViewById(R.id.etOrgName);
        etEmail = findViewById(R.id.etOrgEmail);
        etPassword = findViewById(R.id.etOrgPassword);
        etCompany = findViewById(R.id.etCompany);
        etPhone = findViewById(R.id.etPhone);
        btnRegisterOrganizer = findViewById(R.id.btnRegisterOrganizer);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btnRegisterOrganizer.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String company = etCompany.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || company.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            Organizer org = new Organizer(name, email, company, phone);

                            database.getReference("organizadores").child(uid).setValue(org)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Organizador registrado correctamente", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, OrganizerHomeActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    public static class Organizer {
        public String nombre, correo, empresa, telefono;

        public Organizer() {}

        public Organizer(String nombre, String correo, String empresa, String telefono) {
            this.nombre = nombre;
            this.correo = correo;
            this.empresa = empresa;
            this.telefono = telefono;
        }
    }
}
