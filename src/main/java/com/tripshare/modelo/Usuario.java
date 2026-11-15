package com.tripshare.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class Usuario {
    
    private int idUsuario;
    private String nombre;
    private String email;
    private String contrasena;

public Usuario(int idUsuario, String nombre, String email, String contrasena) {
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.email = email;
    this.contrasena = contrasena;
}

public Usuario() {

}

public int getIdUsuario() {
    return idUsuario;
}

public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getContrasena() {
    return contrasena;
}

public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
}
public static Usuario buscarPorEmail(String email) {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;
    Usuario usuario = null;

    try {

        // 1. Conexión con la base de datos
        conexion = Conexion.conectar();
        if (conexion == null) {
        return null;
        }

        // 2. Preparación de la consulta
        String sql = "SELECT * FROM usuario WHERE email = ?";
        sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, email);

        // 3. Ejecución de la consulta
        resultado = sentencia.executeQuery();

        // 4. Manipulación de los resultados
        if (resultado.next()) {

            usuario = new Usuario(
                resultado.getInt("id_usuario"),
                resultado.getString("nombre"),
                resultado.getString("email"),
                resultado.getString("contrasena")
            );
        }

    } catch (SQLException e) {

        System.out.println("Error al buscar el usuario.");
        e.printStackTrace();

    } finally {

        try {
            if (resultado != null) {
                resultado.close();
            }

            if (sentencia != null) {
                sentencia.close();
            }

            if (conexion != null) {
                conexion.close();
            }

        } catch (SQLException e) {
            System.out.println("Error al cerrar los recursos.");
            e.printStackTrace();
        }
    }

    return usuario;
}
public boolean insertar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        String sql = "INSERT INTO usuario (nombre, email, contrasena) VALUES (?, ?, ?)";

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, this.nombre);
        sentencia.setString(2, this.email);
        sentencia.setString(3, this.contrasena);

        int filas = sentencia.executeUpdate();

        return filas > 0;

    } catch (SQLException e) {

        System.out.println("Error al insertar el usuario.");
        e.printStackTrace();
        return false;

    } finally {

        try {
            if (sentencia != null) {
                sentencia.close();
            }

            if (conexion != null) {
                conexion.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
public boolean actualizar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        String sql = "UPDATE usuario SET nombre = ?, email = ?, contrasena = ? WHERE id_usuario = ?";

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, this.nombre);
        sentencia.setString(2, this.email);
        sentencia.setString(3, this.contrasena);
        sentencia.setInt(4, this.idUsuario);

        int filas = sentencia.executeUpdate();

        return filas > 0;

    } catch (SQLException e) {

        System.out.println("Error al actualizar el usuario.");
        e.printStackTrace();
        return false;

    } finally {

        try {
            if (sentencia != null) {
                sentencia.close();
            }

            if (conexion != null) {
                conexion.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
// Calcular el saldo del usuario en un viaje
public BigDecimal calcularSaldo(int idViaje) {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;

    BigDecimal saldo = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return null;
        }

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

        sentencia.setInt(1, this.idUsuario);
        sentencia.setInt(2, idViaje);

        sentencia.setInt(3, this.idUsuario);
        sentencia.setInt(4, idViaje);

        sentencia.setInt(5, this.idUsuario);
        sentencia.setInt(6, idViaje);

        sentencia.setInt(7, this.idUsuario);
        sentencia.setInt(8, idViaje);

        resultado = sentencia.executeQuery();

        if (resultado.next()) {
            saldo = resultado.getBigDecimal("saldo");
        }

    } catch (SQLException e) {

        System.out.println("Error al calcular el saldo del usuario.");
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

    return saldo;
}
}
