package com.tripshare.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;

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
public static Viaje buscarPorId(int idViaje) {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;
    Viaje viaje = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return null;
        }

        String sql = "SELECT * FROM viaje WHERE id_viaje = ?";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idViaje);

        resultado = sentencia.executeQuery();

        if (resultado.next()) {

            viaje = new Viaje(
                resultado.getInt("id_viaje"),
                resultado.getString("nombre"),
                resultado.getString("destino"),
                resultado.getDate("fecha_inicio").toLocalDate(),
                resultado.getDate("fecha_fin").toLocalDate(),
                resultado.getBigDecimal("presupuesto"),
                resultado.getInt("id_creador")
            );
        }

    } catch (SQLException e) {

        System.out.println("Error al buscar el viaje.");
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

    return viaje;
}
public boolean insertar() {

    Connection conexion = null;
    PreparedStatement sentenciaViaje = null;
    PreparedStatement sentenciaParticipante = null;
    PreparedStatement sentenciaId = null;
    ResultSet resultado = null;

    try {

        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        // Iniciar transacción
        conexion.setAutoCommit(false);

        // Insertar el viaje
        String sqlViaje = "INSERT INTO viaje "
                + "(nombre, destino, fecha_inicio, fecha_fin, presupuesto, id_creador) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        sentenciaViaje = conexion.prepareStatement(sqlViaje);

        sentenciaViaje.setString(1, this.nombre);
        sentenciaViaje.setString(2, this.destino);
        sentenciaViaje.setDate(3, Date.valueOf(this.fechaInicio));
        sentenciaViaje.setDate(4, Date.valueOf(this.fechaFin));
        sentenciaViaje.setBigDecimal(5, this.presupuesto);
        sentenciaViaje.setInt(6, this.idCreador);

        int filas = sentenciaViaje.executeUpdate();

        if (filas == 0) {
            conexion.rollback();
            return false;
        }

        // Recuperar el ID generado por MySQL
        sentenciaId = conexion.prepareStatement("SELECT LAST_INSERT_ID()");
        resultado = sentenciaId.executeQuery();

        if (!resultado.next()) {
            conexion.rollback();
            return false;
        }

        this.idViaje = resultado.getInt(1);

        // Inscribir al creador como participante
        String sqlParticipante = "INSERT INTO usuario_viaje "
                + "(id_usuario, id_viaje, fecha_inscripcion, activo) "
                + "VALUES (?, ?, CURRENT_DATE, TRUE)";

        sentenciaParticipante = conexion.prepareStatement(sqlParticipante);

        sentenciaParticipante.setInt(1, this.idCreador);
        sentenciaParticipante.setInt(2, this.idViaje);

        int filasParticipante = sentenciaParticipante.executeUpdate();

        if (filasParticipante > 0) {
            conexion.commit();
            return true;
        } else {
            conexion.rollback();
            return false;
        }

    } catch (SQLException e) {

        System.out.println("Error al insertar el viaje.");
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
            if (sentenciaId != null) sentenciaId.close();
            if (sentenciaParticipante != null) sentenciaParticipante.close();
            if (sentenciaViaje != null) sentenciaViaje.close();

            if (conexion != null) conexion.close();

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

        String sql = "UPDATE viaje SET nombre = ?, destino = ?, "
                + "fecha_inicio = ?, fecha_fin = ?, presupuesto = ? "
                + "WHERE id_viaje = ?";

        sentencia = conexion.prepareStatement(sql);

        sentencia.setString(1, this.nombre);
        sentencia.setString(2, this.destino);
        sentencia.setDate(3, Date.valueOf(this.fechaInicio));
        sentencia.setDate(4, Date.valueOf(this.fechaFin));
        sentencia.setBigDecimal(5, this.presupuesto);
        sentencia.setInt(6, this.idViaje);

        int filas = sentencia.executeUpdate();

        return filas > 0;

    } catch (SQLException e) {

        System.out.println("Error al actualizar el viaje.");
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
// Agregar un participante al viaje
public boolean agregarParticipante(int idUsuario) {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        // Comprobar si el usuario ya está inscrito
        String sqlComprobar = "SELECT activo FROM usuario_viaje "
                            + "WHERE id_usuario = ? AND id_viaje = ?";

        sentencia = conexion.prepareStatement(sqlComprobar);
        sentencia.setInt(1, idUsuario);
        sentencia.setInt(2, this.idViaje);

        ResultSet resultado = sentencia.executeQuery();

        if (resultado.next()) {
            resultado.close();
            sentencia.close();

            // Si ya estaba inscrito, reactivar su participación
            String sqlActualizar = "UPDATE usuario_viaje SET activo = TRUE "
                                 + "WHERE id_usuario = ? AND id_viaje = ?";

            sentencia = conexion.prepareStatement(sqlActualizar);
            sentencia.setInt(1, idUsuario);
            sentencia.setInt(2, this.idViaje);

            return sentencia.executeUpdate() > 0;
        }

        resultado.close();
        sentencia.close();

        // Si no estaba inscrito, crear la inscripción
        String sqlInsertar = "INSERT INTO usuario_viaje "
                           + "(id_usuario, id_viaje, fecha_inscripcion, activo) "
                           + "VALUES (?, ?, CURRENT_DATE, TRUE)";

        sentencia = conexion.prepareStatement(sqlInsertar);
        sentencia.setInt(1, idUsuario);
        sentencia.setInt(2, this.idViaje);

        return sentencia.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al agregar el participante.");
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


// Listar los participantes activos del viaje
public ArrayList<Usuario> listarParticipantes() {

    Connection conexion = null;
    PreparedStatement sentencia = null;
    ResultSet resultado = null;

    ArrayList<Usuario> participantes = new ArrayList<>();

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return participantes;
        }

        String sql = "SELECT u.* FROM usuario u "
                   + "INNER JOIN usuario_viaje uv "
                   + "ON u.id_usuario = uv.id_usuario "
                   + "WHERE uv.id_viaje = ? AND uv.activo = TRUE";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, this.idViaje);

        resultado = sentencia.executeQuery();

        while (resultado.next()) {
            Usuario usuario = new Usuario(
                resultado.getInt("id_usuario"),
                resultado.getString("nombre"),
                resultado.getString("email"),
                resultado.getString("contrasena")
            );

            participantes.add(usuario);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar los participantes.");
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


// Dar de baja a un participante sin eliminar su historial
public boolean darDeBajaParticipante(int idUsuario) {

    Connection conexion = null;
    PreparedStatement sentencia = null;

    try {
        conexion = Conexion.conectar();

        if (conexion == null) {
            return false;
        }

        // No permitir que el creador abandone su propio viaje
        if (idUsuario == this.idCreador) {
            System.out.println("El creador no puede darse de baja del viaje.");
            return false;
        }

        String sql = "UPDATE usuario_viaje SET activo = FALSE "
                   + "WHERE id_usuario = ? AND id_viaje = ? AND activo = TRUE";

        sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idUsuario);
        sentencia.setInt(2, this.idViaje);

        return sentencia.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al dar de baja al participante.");
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
