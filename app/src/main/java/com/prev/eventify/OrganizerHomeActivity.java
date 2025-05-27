package com.prev.eventify;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class OrganizerHomeActivity extends AppCompatActivity {

    private TextView organizerWelcomeText;
    private FirebaseAuth auth;
    private DatabaseReference organizerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);

        organizerWelcomeText = findViewById(R.id.organizerWelcomeText);
        auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();
        organizerRef = FirebaseDatabase.getInstance().getReference("organizadores").child(uid);

        organizerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombre = snapshot.child("nombre").getValue(String.class);
                organizerWelcomeText.setText("¡Bienvenido, " + nombre + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrganizerHomeActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
