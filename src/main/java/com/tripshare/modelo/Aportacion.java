
package com.tripshare.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Aportacion {

    // Atributos
    private int idAportacion;
    private int idUsuario;
    private int idReceptor;
    private int idViaje;
    private BigDecimal importe;
    private LocalDate fecha;

    // Constructor vacío
    public Aportacion() {

    }

    // Constructor con parámetros
    public Aportacion(int idAportacion, int idUsuario, int idReceptor,
                      int idViaje, BigDecimal importe, LocalDate fecha) {

        this.idAportacion = idAportacion;
        this.idUsuario = idUsuario;
        this.idReceptor = idReceptor;
        this.idViaje = idViaje;
        this.importe = importe;
        this.fecha = fecha;
    }

    // Getters y setters
    public int getIdAportacion() {
        return idAportacion;
    }

    public void setIdAportacion(int idAportacion) {
        this.idAportacion = idAportacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(int idReceptor) {
        this.idReceptor = idReceptor;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
