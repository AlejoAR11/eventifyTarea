package com.prev.eventify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private final List<Evento> listaEventos;

    public EventoAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);
        holder.tvNombre.setText(evento.nombre);
        holder.tvFecha.setText("Fecha: " + evento.fecha);
        holder.tvUbicacion.setText("Ubicación: " + evento.ubicacion);
        holder.tvDescripcion.setText(evento.descripcion);
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvFecha, tvUbicacion, tvDescripcion;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
