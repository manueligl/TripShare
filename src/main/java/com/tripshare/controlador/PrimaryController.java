
package com.tripshare.controlador;

import com.tripshare.App;
import com.tripshare.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    // Iniciar sesión
    @FXML
    private void iniciarSesion() {

        String email = emailField.getText().trim();
        String contrasena = passwordField.getText();

        if (email.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Campos incompletos",
                "Introduce tu correo electrónico y contraseña."
            );
            return;
        }

        Usuario usuario = Usuario.buscarPorEmail(email);

        if (usuario == null || !usuario.getContrasena().equals(contrasena)) {
            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error de acceso",
                "El correo electrónico o la contraseña son incorrectos."
            );
            return;
        }

        // Guardar el usuario que ha iniciado sesión
        App.usuarioActual = usuario;

        // Abrir la pantalla Mis viajes
        try {
            App.setRoot("mis-viajes");
        } catch (java.io.IOException e) {
            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se pudo abrir la pantalla de viajes."
            );
            e.printStackTrace();
        }
    }

    // Acceder al registro
    @FXML
    private void registrarse() {

        try {
            App.setRoot("secondary");
        } catch (java.io.IOException e) {
            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se pudo abrir la pantalla de registro."
            );
            e.printStackTrace();
        }
    }

    // Mostrar mensajes
    private void mostrarAlerta(
            Alert.AlertType tipo, String titulo, String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
