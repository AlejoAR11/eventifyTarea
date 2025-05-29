package com.prev.eventify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private final List<Evento> listaEventos;
    private final onEventoClickListener.OnEventoClickListener listener;
    private final boolean mostrarControles;

    // Constructor con control de botones
    public EventoAdapter(List<Evento> listaEventos, onEventoClickListener.OnEventoClickListener listener, boolean mostrarControles) {
        this.listaEventos = listaEventos;
        this.listener = listener;
        this.mostrarControles = mostrarControles;
    }

    // Constructor de solo lectura (por ejemplo: para usuarios)
    public EventoAdapter(List<Evento> listaEventos) {
        this(listaEventos, new onEventoClickListener.OnEventoClickListener() {
            @Override public void onEditar(Evento evento) {}
            @Override public void onEliminar(Evento evento) {}
        }, false);
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

        if (mostrarControles) {
            holder.btnEditar.setVisibility(View.VISIBLE);
            holder.btnEliminar.setVisibility(View.VISIBLE);

            holder.btnEditar.setOnClickListener(v -> listener.onEditar(evento));
            holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(evento));
        } else {
            holder.btnEditar.setVisibility(View.GONE);
            holder.btnEliminar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvFecha, tvUbicacion, tvDescripcion;
        Button btnEditar, btnEliminar;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
