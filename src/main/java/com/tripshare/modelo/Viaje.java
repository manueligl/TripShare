package com.tripshare.modelo;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Viaje {

    private int idViaje;
    private String nombre;
    private String destino;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal presupuesto;
    private int idCreador;

public Viaje() {

}

public Viaje(int idViaje, String nombre, String destino,
             LocalDate fechaInicio, LocalDate fechaFin,
             BigDecimal presupuesto, int idCreador) {

    this.idViaje = idViaje;
    this.nombre = nombre;
    this.destino = destino;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.presupuesto = presupuesto;
    this.idCreador = idCreador;
}

public int getIdViaje() {
    return idViaje;
}

public void setIdViaje(int idViaje) {
    this.idViaje = idViaje;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public String getDestino() {
    return destino;
}

public void setDestino(String destino) {
    this.destino = destino;
}

public LocalDate getFechaInicio() {
    return fechaInicio;
}

public void setFechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
}

public LocalDate getFechaFin() {
    return fechaFin;
}

public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
}

public BigDecimal getPresupuesto() {
    return presupuesto;
}

public void setPresupuesto(BigDecimal presupuesto) {
    this.presupuesto = presupuesto;
}

public int getIdCreador() {
    return idCreador;
}

public void setIdCreador(int idCreador) {
    this.idCreador = idCreador;
}
}
