
package com.tripshare.controlador;

import com.tripshare.App;
import com.tripshare.modelo.Usuario;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SecondaryController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    // Registrar un nuevo usuario
    @FXML
    private void registrarUsuario() {

        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String contrasena = passwordField.getText();
        String confirmarContrasena = confirmPasswordField.getText();

        // Comprobar campos vacíos
        if (nombre.isEmpty() || email.isEmpty()
                || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {

            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Campos incompletos",
                "Debes rellenar todos los campos."
            );
            return;
        }

        // Comprobar formato básico del correo
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {

            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Correo no válido",
                "Introduce una dirección de correo electrónico válida."
            );
            return;
        }

        // Comprobar que las contraseñas coinciden
        if (!contrasena.equals(confirmarContrasena)) {

            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Contraseñas diferentes",
                "Las contraseñas introducidas no coinciden."
            );
            return;
        }

        // Crear el usuario
        Usuario usuario = new Usuario(
            0,
            nombre,
            email,
            contrasena
        );

        // Guardar el usuario en MySQL
        if (usuario.insertar()) {

            mostrarAlerta(
                Alert.AlertType.INFORMATION,
                "Registro completado",
                "Tu cuenta se ha creado correctamente."
            );

            volverLogin();

        } else {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error de registro",
                "No se ha podido crear la cuenta. Comprueba si el correo ya está registrado."
            );
        }
    }

    // Volver al inicio de sesión
    @FXML
    private void volverLogin() {

        try {
            App.setRoot("primary");

        } catch (IOException e) {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se ha podido volver al inicio de sesión."
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
