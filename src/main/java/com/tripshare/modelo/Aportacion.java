
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


// Insertar una aportación
public void insertar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    PreparedStatement sentenciaId = null;
    ResultSet resultado = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return;
        }

        String sql = "INSERT INTO aportacion "
                   + "(id_usuario, id_receptor, id_viaje, importe, fecha) "
                   + "VALUES (?, ?, ?, ?, ?)";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, this.idUsuario);
        sentencia.setInt(2, this.idReceptor);
        sentencia.setInt(3, this.idViaje);
        sentencia.setBigDecimal(4, this.importe);
        sentencia.setDate(5, Date.valueOf(this.fecha));

        sentencia.executeUpdate();

        // Recuperar el ID generado
        sentenciaId = conexion.prepareStatement("SELECT LAST_INSERT_ID()");
        resultado = sentenciaId.executeQuery();

        if (resultado.next()) {
            this.idAportacion = resultado.getInt(1);
        }

        System.out.println("Aportación insertada correctamente.");

    } catch (SQLException e) {
        System.out.println("Error al insertar la aportación.");
        e.printStackTrace();

    } finally {
        try {
            if (resultado != null) resultado.close();
            if (sentenciaId != null) sentenciaId.close();
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
