package com.prev.eventify;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerEventos;
    private EventoAdapter adapter;
    private final List<Evento> listaEventos = new ArrayList<>();
    private final DatabaseReference refEventos = FirebaseDatabase.getInstance().getReference("eventos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerEventos = findViewById(R.id.recyclerEventos);
        recyclerEventos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventoAdapter(listaEventos);
        recyclerEventos.setAdapter(adapter);

        cargarEventos();
    }

    private void cargarEventos() {
        refEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEventos.clear();
                for (DataSnapshot eventoSnap : snapshot.getChildren()) {
                    Evento evento = eventoSnap.getValue(Evento.class);
                    if (evento != null) {
                        listaEventos.add(evento);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al cargar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
