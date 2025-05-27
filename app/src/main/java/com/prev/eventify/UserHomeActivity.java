package com.prev.eventify;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        welcomeText = findViewById(R.id.welcomeText);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombre = snapshot.child("nombre").getValue(String.class);
                welcomeText.setText("¡Bienvenido, " + nombre + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserHomeActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
