package com.tripshare.modelo;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection conexion = Conexion.conectar();

        if (conexion != null) {
            System.out.println("La prueba de conexión ha sido satisfactoria.");

            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }

        } else {
            System.out.println("No se ha podido conectar con MySQL.");
        }
    }
}