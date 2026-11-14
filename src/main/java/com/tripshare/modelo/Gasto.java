
package com.tripshare.modelo;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Gasto {

    // Atributos
    private int idGasto;
    private String concepto;
    private BigDecimal importe;
    private LocalDate fecha;
    private int idViaje;
    private int idPagador;

    // Constructor vacío
    public Gasto() {

    }

    // Constructor con parámetros
    public Gasto(int idGasto, String concepto, BigDecimal importe,
                 LocalDate fecha, int idViaje, int idPagador) {

        this.idGasto = idGasto;
        this.concepto = concepto;
        this.importe = importe;
        this.fecha = fecha;
        this.idViaje = idViaje;
        this.idPagador = idPagador;
    }

    // Getters y setters
    public int getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(int idGasto) {
        this.idGasto = idGasto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
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

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public int getIdPagador() {
        return idPagador;
    }

    public void setIdPagador(int idPagador) {
        this.idPagador = idPagador;
    }
}
