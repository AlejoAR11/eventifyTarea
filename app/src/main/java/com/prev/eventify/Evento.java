package com.prev.eventify;

public class Evento {
    public String nombre, ubicacion, fecha, descripcion;

    public Evento() {} // Requerido por Firebase

    public Evento(String nombre, String ubicacion, String fecha, String descripcion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
}
