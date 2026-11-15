
package com.tripshare.controlador;

import com.tripshare.App;
import com.tripshare.modelo.Usuario;
import com.tripshare.modelo.Viaje;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class CrearViajeController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField destinoField;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private TextField presupuestoField;

    @FXML
    private void guardarViaje() {

        String nombre = nombreField.getText().trim();
        String destino = destinoField.getText().trim();
        LocalDate fechaInicio = fechaInicioPicker.getValue();
        LocalDate fechaFin = fechaFinPicker.getValue();
        String presupuestoTexto = presupuestoField.getText().trim();

        // Comprobar campos vacíos
        if (nombre.isEmpty() || destino.isEmpty()
                || fechaInicio == null || fechaFin == null
                || presupuestoTexto.isEmpty()) {

            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Campos incompletos",
                "Debes rellenar todos los campos."
            );
            return;
        }

        // Comprobar fechas
        if (fechaFin.isBefore(fechaInicio)) {

            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Fechas incorrectas",
                "La fecha de finalización no puede ser anterior a la de inicio."
            );
            return;
        }

        // Comprobar presupuesto
        BigDecimal presupuesto;

        try {
            presupuesto = new BigDecimal(
                presupuestoTexto.replace(",", ".")
            );

            if (presupuesto.compareTo(BigDecimal.ZERO) < 0) {
                mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Presupuesto incorrecto",
                    "El presupuesto no puede ser negativo."
                );
                return;
            }

        } catch (NumberFormatException e) {

            mostrarAlerta(
                Alert.AlertType.WARNING,
                "Presupuesto incorrecto",
                "Introduce una cantidad numérica válida."
            );
            return;
        }

        // Comprobar usuario autenticado
        Usuario usuario = App.usuarioActual;

        if (usuario == null) {
            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error de sesión",
                "No se ha encontrado el usuario autenticado."
            );
            return;
        }

        // Crear el viaje
        Viaje viaje = new Viaje(
            0,
            nombre,
            destino,
            fechaInicio,
            fechaFin,
            presupuesto,
            usuario.getIdUsuario()
        );

        // Insertar el viaje y registrar al creador
        if (viaje.insertar()) {

            mostrarAlerta(
                Alert.AlertType.INFORMATION,
                "Viaje creado",
                "El viaje se ha creado correctamente."
            );

            volverAMisViajes();

        } else {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error al crear el viaje",
                "No se ha podido guardar el viaje. Inténtalo de nuevo."
            );
        }
    }

    @FXML
    private void cancelar() {
        volverAMisViajes();
    }

    private void volverAMisViajes() {

        try {
            App.setRoot("mis-viajes");

        } catch (java.io.IOException e) {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se ha podido volver a Mis viajes."
            );

            e.printStackTrace();
        }
    }

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
