package com.prev.eventify;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateEventActivity extends AppCompatActivity {

    private EditText etEventName, etLocation, etDate, etDescription;
    private Button btnCreateEvent;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etEventName = findViewById(R.id.etEventName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(v -> {
            String nombre = etEventName.getText().toString().trim();
            String ubicacion = etLocation.getText().toString().trim();
            String fecha = etDate.getText().toString().trim();
            String descripcion = etDescription.getText().toString().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty() || fecha.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = auth.getCurrentUser().getUid();
            String eventoId = database.getReference("eventos").push().getKey();

            HashMap<String, Object> evento = new HashMap<>();
            evento.put("nombre", nombre);
            evento.put("ubicacion", ubicacion);
            evento.put("fecha", fecha);
            evento.put("descripcion", descripcion);
            evento.put("organizadorId", uid);

            database.getReference("eventos").child(eventoId).setValue(evento)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Evento creado correctamente", Toast.LENGTH_SHORT).show();
                        finish(); // Cierra la actividad
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al guardar evento", Toast.LENGTH_SHORT).show());
        });
    }
}
