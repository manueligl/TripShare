
package com.tripshare.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

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

    // Buscar un gasto por su ID
public static Gasto buscarPorId(int idGasto) {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;
    Gasto gasto = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return null;
        }

        String sql = "SELECT * FROM gasto WHERE id_gasto = ?";
        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idGasto);

        resultado = sentencia.executeQuery();

        if (resultado.next()) {
            gasto = new Gasto(
                resultado.getInt("id_gasto"),
                resultado.getString("concepto"),
                resultado.getBigDecimal("importe"),
                resultado.getDate("fecha").toLocalDate(),
                resultado.getInt("id_viaje"),
                resultado.getInt("id_pagador")
            );
        }

    } catch (SQLException e) {
        System.out.println("Error al buscar el gasto.");
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

    return gasto;
}


// Insertar un gasto
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

        String sql = "INSERT INTO gasto "
                   + "(concepto, importe, fecha, id_viaje, id_pagador) "
                   + "VALUES (?, ?, ?, ?, ?)";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, this.concepto);
        sentencia.setBigDecimal(2, this.importe);
        sentencia.setDate(3, Date.valueOf(this.fecha));
        sentencia.setInt(4, this.idViaje);
        sentencia.setInt(5, this.idPagador);

        sentencia.executeUpdate();

        // Recuperar el ID generado
        sentenciaId = conexion.prepareStatement("SELECT LAST_INSERT_ID()");
        resultado = sentenciaId.executeQuery();

        if (resultado.next()) {
            this.idGasto = resultado.getInt(1);
        }

        System.out.println("Gasto insertado correctamente.");

    } catch (SQLException e) {
        System.out.println("Error al insertar el gasto.");
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


// Actualizar un gasto
public void actualizar() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return;
        }

        String sql = "UPDATE gasto SET "
                   + "concepto = ?, importe = ?, fecha = ?, "
                   + "id_viaje = ?, id_pagador = ? "
                   + "WHERE id_gasto = ?";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, this.concepto);
        sentencia.setBigDecimal(2, this.importe);
        sentencia.setDate(3, Date.valueOf(this.fecha));
        sentencia.setInt(4, this.idViaje);
        sentencia.setInt(5, this.idPagador);
        sentencia.setInt(6, this.idGasto);

        int filas = sentencia.executeUpdate();

        if (filas > 0) {
            System.out.println("Gasto actualizado correctamente.");
        } else {
            System.out.println("No se encontró el gasto.");
        }

    } catch (SQLException e) {
        System.out.println("Error al actualizar el gasto.");
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
// Repartir el gasto entre los participantes activos del viaje
public boolean repartirEntreParticipantes() {

    Connection conexion = null;
    PreparedStatement sentenciaConsulta = null;
    PreparedStatement sentenciaEliminar = null;
    PreparedStatement sentenciaInsertar = null;
    ResultSet resultado = null;

    try {
        if (this.idGasto <= 0 || this.idViaje <= 0
                || this.importe == null || this.importe.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Convertir el importe a céntimos sin perder precisión
        long totalCentimos = this.importe.movePointRight(2).longValueExact();

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        conexion.setAutoCommit(false);

        // Obtener los participantes activos del viaje
        String sqlConsulta = "SELECT id_usuario FROM usuario_viaje "
                           + "WHERE id_viaje = ? AND activo = TRUE";

        sentenciaConsulta = conexion.prepareStatement(sqlConsulta);
        sentenciaConsulta.setInt(1, this.idViaje);

        resultado = sentenciaConsulta.executeQuery();

        ArrayList<Integer> participantes = new ArrayList<>();

        while (resultado.next()) {
            participantes.add(resultado.getInt("id_usuario"));
        }

        resultado.close();
        resultado = null;

        sentenciaConsulta.close();
        sentenciaConsulta = null;

        if (participantes.isEmpty()) {
            conexion.rollback();
            return false;
        }

        // Calcular el reparto en céntimos
        long parteBase = totalCentimos / participantes.size();
        long sobrante = totalCentimos % participantes.size();

        // Eliminar un reparto anterior, si existe
        String sqlEliminar = "DELETE FROM gasto_participante "
                           + "WHERE id_gasto = ? AND id_viaje = ?";

        sentenciaEliminar = conexion.prepareStatement(sqlEliminar);
        sentenciaEliminar.setInt(1, this.idGasto);
        sentenciaEliminar.setInt(2, this.idViaje);
        sentenciaEliminar.executeUpdate();

        sentenciaEliminar.close();
        sentenciaEliminar = null;

        // Insertar las nuevas asignaciones
        String sqlInsertar = "INSERT INTO gasto_participante "
                           + "(id_usuario, id_gasto, id_viaje, importe_asignado) "
                           + "VALUES (?, ?, ?, ?)";

        sentenciaInsertar = conexion.prepareStatement(sqlInsertar);

        for (int i = 0; i < participantes.size(); i++) {

            long centimos = parteBase;

            // Repartir los céntimos sobrantes entre los primeros participantes
            if (i < sobrante) {
                centimos++;
            }

            BigDecimal importeAsignado = BigDecimal.valueOf(centimos, 2);

            sentenciaInsertar.setInt(1, participantes.get(i));
            sentenciaInsertar.setInt(2, this.idGasto);
            sentenciaInsertar.setInt(3, this.idViaje);
            sentenciaInsertar.setBigDecimal(4, importeAsignado);

            sentenciaInsertar.executeUpdate();
        }

        conexion.commit();

        System.out.println("Gasto repartido correctamente.");
        return true;

    } catch (SQLException | ArithmeticException e) {

        System.out.println("Error al repartir el gasto.");
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
            if (sentenciaConsulta != null) sentenciaConsulta.close();
            if (sentenciaEliminar != null) sentenciaEliminar.close();
            if (sentenciaInsertar != null) sentenciaInsertar.close();
            if (conexion != null) conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


// Consultar los participantes y sus importes asignados
public HashMap<Integer, BigDecimal> listarParticipantes() {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;

    HashMap<Integer, BigDecimal> participantes = new HashMap<>();

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return participantes;
        }

        String sql = "SELECT id_usuario, importe_asignado "
                   + "FROM gasto_participante "
                   + "WHERE id_gasto = ? AND id_viaje = ?";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, this.idGasto);
        sentencia.setInt(2, this.idViaje);

        resultado = sentencia.executeQuery();

        while (resultado.next()) {
            participantes.put(
                resultado.getInt("id_usuario"),
                resultado.getBigDecimal("importe_asignado")
            );
        }

    } catch (SQLException e) {

        System.out.println("Error al listar los participantes del gasto.");
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

    return participantes;
}


// Eliminar el reparto de un gasto
public boolean eliminarReparto() {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        String sql = "DELETE FROM gasto_participante "
                   + "WHERE id_gasto = ? AND id_viaje = ?";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, this.idGasto);
        sentencia.setInt(2, this.idViaje);

        int filas = sentencia.executeUpdate();

        return filas > 0;

    } catch (SQLException e) {

        System.out.println("Error al eliminar el reparto.");
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
