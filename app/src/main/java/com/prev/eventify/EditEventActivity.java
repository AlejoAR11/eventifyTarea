package com.prev.eventify;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class EditEventActivity extends AppCompatActivity {

    private EditText etNombre, etUbicacion, etFecha, etDescripcion;
    private Button btnGuardarCambios;

    private DatabaseReference eventosRef;
    private String eventoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        etNombre = findViewById(R.id.etNombre);
        etUbicacion = findViewById(R.id.etUbicacion);
        etFecha = findViewById(R.id.etFecha);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        eventoId = getIntent().getStringExtra("eventoId");
        if (eventoId == null) {
            Toast.makeText(this, "Error: Evento no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        eventosRef = FirebaseDatabase.getInstance().getReference("eventos");

        // Cargar datos del evento
        eventosRef.child(eventoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Evento evento = snapshot.getValue(Evento.class);
                if (evento != null) {
                    etNombre.setText(evento.nombre);
                    etUbicacion.setText(evento.ubicacion);
                    etFecha.setText(evento.fecha);
                    etDescripcion.setText(evento.descripcion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEventActivity.this, "Error al cargar evento", Toast.LENGTH_SHORT).show();
            }
        });

        // Guardar cambios
        btnGuardarCambios.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String ubicacion = etUbicacion.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty() || fecha.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            eventosRef.child(eventoId).child("nombre").setValue(nombre);
            eventosRef.child(eventoId).child("ubicacion").setValue(ubicacion);
            eventosRef.child(eventoId).child("fecha").setValue(fecha);
            eventosRef.child(eventoId).child("descripcion").setValue(descripcion)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(EditEventActivity.this, "Evento actualizado", Toast.LENGTH_SHORT).show()
                    );
        });
    }
}
