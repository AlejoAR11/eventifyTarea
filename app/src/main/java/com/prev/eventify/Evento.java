package com.prev.eventify;

public class Evento {
    public String id;           // ID del nodo en Firebase (NO se guarda, solo lo usamos localmente)
    public String nombre;
    public String ubicacion;
    public String fecha;
    public String descripcion;
    public String organizadorId; // Esto es útil si quieres filtrar eventos por organizador

    public Evento() {
        // Requerido por Firebase
    }

    public Evento(String nombre, String ubicacion, String fecha, String descripcion, String organizadorId) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.organizadorId = organizadorId;
    }
}
