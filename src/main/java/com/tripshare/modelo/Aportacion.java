
package com.tripshare.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
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

    // Buscar una aportación por su ID
public static Aportacion buscarPorId(int idAportacion) {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;
    Aportacion aportacion = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return null;
        }

        String sql = "SELECT * FROM aportacion WHERE id_aportacion = ?";
        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idAportacion);

        resultado = sentencia.executeQuery();

        if (resultado.next()) {
            aportacion = new Aportacion(
                resultado.getInt("id_aportacion"),
                resultado.getInt("id_usuario"),
                resultado.getInt("id_receptor"),
                resultado.getInt("id_viaje"),
                resultado.getBigDecimal("importe"),
                resultado.getDate("fecha").toLocalDate()
            );
        }

    } catch (SQLException e) {
        System.out.println("Error al buscar la aportación.");
        e.printStackTrace();

    } finally {
        try {
            if (resultado != null) resultado.close();
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return aportacion;
}


// Registrar una aportación validando las deudas y los saldos
public boolean insertar() {

    Connection conexion = null;
    PreparedStatement sentenciaParticipantes = null;
    PreparedStatement sentencia = null;
    PreparedStatement sentenciaId = null;
    ResultSet resultado = null;
    ResultSet resultadoId = null;

    try {

        // Validar los datos básicos
        if (this.idUsuario == this.idReceptor
                || this.idUsuario <= 0
                || this.idReceptor <= 0
                || this.idViaje <= 0
                || this.importe == null
                || this.importe.compareTo(BigDecimal.ZERO) <= 0
                || this.fecha == null) {

            System.out.println("Los datos de la aportación no son válidos.");
            return false;
        }

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        conexion.setAutoCommit(false);

        // Comprobar que ambos usuarios están activos en el viaje.
        // Se bloquean sus registros para evitar aportaciones simultáneas.
        String sqlParticipantes =
                "SELECT id_usuario, activo FROM usuario_viaje "
              + "WHERE id_viaje = ? AND id_usuario IN (?, ?) "
              + "ORDER BY id_usuario FOR UPDATE";

        sentenciaParticipantes = conexion.prepareStatement(sqlParticipantes);
        sentenciaParticipantes.setInt(1, this.idViaje);
        sentenciaParticipantes.setInt(2, this.idUsuario);
        sentenciaParticipantes.setInt(3, this.idReceptor);

        resultado = sentenciaParticipantes.executeQuery();

        boolean emisorActivo = false;
        boolean receptorActivo = false;

        while (resultado.next()) {

            int id = resultado.getInt("id_usuario");
            boolean activo = resultado.getBoolean("activo");

            if (id == this.idUsuario && activo) {
                emisorActivo = true;
            }

            if (id == this.idReceptor && activo) {
                receptorActivo = true;
            }
        }

        resultado.close();
        resultado = null;

        sentenciaParticipantes.close();
        sentenciaParticipantes = null;

        if (!emisorActivo || !receptorActivo) {
            conexion.rollback();
            System.out.println("Ambos usuarios deben estar activos en el viaje.");
            return false;
        }

        // Calcular la deuda pendiente del emisor
        BigDecimal saldoEmisor = calcularSaldo(
                conexion, this.idUsuario, this.idViaje
        );

        // Calcular el saldo pendiente del receptor
        BigDecimal saldoReceptor = calcularSaldo(
                conexion, this.idReceptor, this.idViaje
        );

        if (saldoEmisor == null || saldoReceptor == null) {
            conexion.rollback();
            return false;
        }

        // El emisor debe tener una deuda pendiente
        if (saldoEmisor.compareTo(BigDecimal.ZERO) <= 0) {
            conexion.rollback();
            System.out.println("El emisor no tiene ninguna deuda pendiente.");
            return false;
        }

        // El receptor debe tener dinero pendiente de recibir
        if (saldoReceptor.compareTo(BigDecimal.ZERO) >= 0) {
            conexion.rollback();
            System.out.println("El receptor no tiene saldo pendiente de recibir.");
            return false;
        }

        // Comprobar los límites de la aportación
        if (this.importe.compareTo(saldoEmisor) > 0
                || this.importe.compareTo(saldoReceptor.abs()) > 0) {

            conexion.rollback();
            System.out.println("El importe supera el saldo pendiente.");
            return false;
        }

        // Insertar la aportación
        String sql = "INSERT INTO aportacion "
                   + "(id_usuario, id_receptor, id_viaje, importe, fecha) "
                   + "VALUES (?, ?, ?, ?, ?)";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, this.idUsuario);
        sentencia.setInt(2, this.idReceptor);
        sentencia.setInt(3, this.idViaje);
        sentencia.setBigDecimal(4, this.importe);
        sentencia.setDate(5, Date.valueOf(this.fecha));

        int filas = sentencia.executeUpdate();

        if (filas == 0) {
            conexion.rollback();
            return false;
        }

        // Recuperar el ID generado
        sentenciaId = conexion.prepareStatement("SELECT LAST_INSERT_ID()");
        resultadoId = sentenciaId.executeQuery();

        if (resultadoId.next()) {
            this.idAportacion = resultadoId.getInt(1);
        }

        conexion.commit();

        System.out.println("Aportación registrada correctamente.");
        return true;

    } catch (SQLException e) {

        System.out.println("Error al registrar la aportación.");
        e.printStackTrace();

        try {
            if (conexion != null) {
                conexion.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;

    } finally {

        try {
            if (resultado != null) resultado.close();
            if (resultadoId != null) resultadoId.close();
            if (sentenciaParticipantes != null) sentenciaParticipantes.close();
            if (sentencia != null) sentencia.close();
            if (sentenciaId != null) sentenciaId.close();
            if (conexion != null) conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Calcular el saldo pendiente de un usuario en un viaje
private BigDecimal calcularSaldo(
        Connection conexion, int idUsuario, int idViaje) {

    PreparedStatement sentencia = null;
    ResultSet resultado = null;

    try {

        String sql = "SELECT "
                + "(SELECT COALESCE(SUM(gp.importe_asignado), 0) "
                + "FROM gasto_participante gp "
                + "WHERE gp.id_usuario = ? AND gp.id_viaje = ?) "

                + "- (SELECT COALESCE(SUM(g.importe), 0) "
                + "FROM gasto g "
                + "WHERE g.id_pagador = ? AND g.id_viaje = ?) "

                + "+ (SELECT COALESCE(SUM(a.importe), 0) "
                + "FROM aportacion a "
                + "WHERE a.id_usuario = ? AND a.id_viaje = ?) "

                + "- (SELECT COALESCE(SUM(a.importe), 0) "
                + "FROM aportacion a "
                + "WHERE a.id_receptor = ? AND a.id_viaje = ?) "
                + "AS saldo";

        sentencia = conexion.prepareStatement(sql);

        sentencia.setInt(1, idUsuario);
        sentencia.setInt(2, idViaje);

        sentencia.setInt(3, idUsuario);
        sentencia.setInt(4, idViaje);

        sentencia.setInt(5, idUsuario);
        sentencia.setInt(6, idViaje);

        sentencia.setInt(7, idUsuario);
        sentencia.setInt(8, idViaje);

        resultado = sentencia.executeQuery();

        if (resultado.next()) {
            return resultado.getBigDecimal("saldo");
        }

    } catch (SQLException e) {

        System.out.println("Error al calcular el saldo.");
        e.printStackTrace();

    } finally {

        try {
            if (resultado != null) resultado.close();
            if (sentencia != null) sentencia.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return null;
}


// Actualizar una aportación
public void actualizar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return;
        }

        String sql = "UPDATE aportacion SET "
                   + "id_usuario = ?, id_receptor = ?, id_viaje = ?, "
                   + "importe = ?, fecha = ? "
                   + "WHERE id_aportacion = ?";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, this.idUsuario);
        sentencia.setInt(2, this.idReceptor);
        sentencia.setInt(3, this.idViaje);
        sentencia.setBigDecimal(4, this.importe);
        sentencia.setDate(5, Date.valueOf(this.fecha));
        sentencia.setInt(6, this.idAportacion);

        int filas = sentencia.executeUpdate();

        if (filas > 0) {
            System.out.println("Aportación actualizada correctamente.");
        } else {
            System.out.println("No se encontró la aportación.");
        }

    } catch (SQLException e) {
        System.out.println("Error al actualizar la aportación.");
        e.printStackTrace();

    } finally {
        try {
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
