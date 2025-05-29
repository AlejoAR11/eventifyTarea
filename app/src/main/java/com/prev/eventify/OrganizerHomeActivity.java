package com.prev.eventify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class OrganizerHomeActivity extends AppCompatActivity {

    private TextView organizerWelcomeText;
    private Button btnGoToCreateEvent;
    private RecyclerView recyclerEventosOrganizador;
    private EventoAdapter adapter;
    private List<Evento> listaEventos;

    private FirebaseAuth auth;
    private DatabaseReference eventosRef, organizadorRef;
    private String organizadorUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);

        organizerWelcomeText = findViewById(R.id.organizerWelcomeText);
        btnGoToCreateEvent = findViewById(R.id.btnGoToCreateEvent);
        recyclerEventosOrganizador = findViewById(R.id.recyclerEventosOrganizador);

        recyclerEventosOrganizador.setLayoutManager(new LinearLayoutManager(this));
        listaEventos = new ArrayList<>();

        // ✅ Constructor corregido con los 3 parámetros
        adapter = new EventoAdapter(listaEventos, new onEventoClickListener.OnEventoClickListener() {
            @Override
            public void onEditar(Evento evento) {
                Intent intent = new Intent(OrganizerHomeActivity.this, EditEventActivity.class);
                intent.putExtra("eventoId", evento.id);
                startActivity(intent);
            }

            @Override
            public void onEliminar(Evento evento) {
                new AlertDialog.Builder(OrganizerHomeActivity.this)
                        .setTitle("¿Eliminar evento?")
                        .setMessage("Esta acción no se puede deshacer.")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            FirebaseDatabase.getInstance()
                                    .getReference("eventos")
                                    .child(evento.id)
                                    .removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(OrganizerHomeActivity.this, "Evento eliminado", Toast.LENGTH_SHORT).show();
                                        cargarEventosDelOrganizador(); // Recargar lista
                                    });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        }, true); // ✅ Mostrar botones

        recyclerEventosOrganizador.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        organizadorUid = auth.getCurrentUser().getUid();

        organizadorRef = FirebaseDatabase.getInstance().getReference("organizadores").child(organizadorUid);
        eventosRef = FirebaseDatabase.getInstance().getReference("eventos");

        organizadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombre = snapshot.child("nombre").getValue(String.class);
                organizerWelcomeText.setText("¡Bienvenido, " + nombre + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                organizerWelcomeText.setText("Bienvenido");
            }
        });

        btnGoToCreateEvent.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateEventActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEventosDelOrganizador(); // 🔁 Refrescar siempre que vuelva a esta pantalla
    }

    private void cargarEventosDelOrganizador() {
        eventosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEventos.clear();
                for (DataSnapshot eventoSnap : snapshot.getChildren()) {
                    Evento evento = eventoSnap.getValue(Evento.class);
                    if (evento != null) {
                        evento.id = eventoSnap.getKey();
                        String creador = eventoSnap.child("organizadorId").getValue(String.class);
                        if (organizadorUid.equals(creador)) {
                            listaEventos.add(evento);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrganizerHomeActivity.this, "Error al cargar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
