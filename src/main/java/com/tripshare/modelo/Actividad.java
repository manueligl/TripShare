
package com.tripshare.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
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

    // Buscar actividad por ID
public static Actividad buscarPorId(int idActividad) {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;
    Actividad actividad = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return null;
        }

        String sql = "SELECT * FROM actividad WHERE id_actividad = ?";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idActividad);

        resultado = sentencia.executeQuery();

        if (resultado.next()) {

            actividad = new Actividad(
                resultado.getInt("id_actividad"),
                resultado.getString("nombre"),
                resultado.getString("descripcion"),
                resultado.getDate("fecha").toLocalDate(),
                resultado.getBigDecimal("precio")
            );
        }

    } catch (SQLException e) {

        System.out.println("Error al buscar la actividad.");
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

    return actividad;
}


// Insertar actividad
public boolean insertar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        String sql = "INSERT INTO actividad "
                + "(nombre, descripcion, fecha, precio) "
                + "VALUES (?, ?, ?, ?)";

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, this.nombre);
        sentencia.setString(2, this.descripcion);
        sentencia.setDate(3, Date.valueOf(this.fecha));
        sentencia.setBigDecimal(4, this.precio);

        int filas = sentencia.executeUpdate();

        return filas > 0;

    } catch (SQLException e) {

        System.out.println("Error al insertar la actividad.");
        e.printStackTrace();
        return false;

    } finally {

        try {
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


// Actualizar actividad
public boolean actualizar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        String sql = "UPDATE actividad SET nombre = ?, descripcion = ?, "
                + "fecha = ?, precio = ? WHERE id_actividad = ?";

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, this.nombre);
        sentencia.setString(2, this.descripcion);
        sentencia.setDate(3, Date.valueOf(this.fecha));
        sentencia.setBigDecimal(4, this.precio);
        sentencia.setInt(5, this.idActividad);

        int filas = sentencia.executeUpdate();

        return filas > 0;

    } catch (SQLException e) {

        System.out.println("Error al actualizar la actividad.");
        e.printStackTrace();
        return false;

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
