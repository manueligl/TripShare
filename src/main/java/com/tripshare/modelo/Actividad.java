
package com.tripshare.modelo;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Actividad {

    // Atributos
    private int idActividad;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private BigDecimal precio;

    // Constructor vacío
    public Actividad() {

    }

    // Constructor con parámetros
    public Actividad(int idActividad, String nombre, String descripcion,
                     LocalDate fecha, BigDecimal precio) {

        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.precio = precio;
    }

    // Getters y setters
    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
