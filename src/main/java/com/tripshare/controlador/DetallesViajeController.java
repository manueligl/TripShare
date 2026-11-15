
package com.tripshare.controlador;

import com.tripshare.App;
import com.tripshare.modelo.Actividad;
import com.tripshare.modelo.Usuario;
import com.tripshare.modelo.Viaje;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DetallesViajeController {

    @FXML
    private Label nombreViajeLabel;

    @FXML
    private Label destinoLabel;

    @FXML
    private Label fechaInicioLabel;

    @FXML
    private Label fechaFinLabel;

    @FXML
    private Label presupuestoLabel;

    @FXML
    private Label contadorParticipantesLabel;

    @FXML
    private VBox participantesContainer;

    @FXML
    private Label mensajeParticipantesLabel;

    @FXML
    private VBox actividadesContainer;

    @FXML
    private Label mensajeActividadesLabel;

    private Viaje viaje;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {

        viaje = App.viajeSeleccionado;

        if (viaje == null) {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se ha seleccionado ningún viaje."
            );

            volverMisViajes();
            return;
        }

        cargarInformacionViaje();
        cargarParticipantes();
        cargarActividades();
    }

    // Cargar información general del viaje
    private void cargarInformacionViaje() {

        nombreViajeLabel.setText(viaje.getNombre());

        destinoLabel.setText("📍 " + viaje.getDestino());

        fechaInicioLabel.setText(
            viaje.getFechaInicio().format(formatoFecha)
        );

        fechaFinLabel.setText(
            viaje.getFechaFin().format(formatoFecha)
        );

        presupuestoLabel.setText(
            viaje.getPresupuesto().toPlainString() + " €"
        );
    }

    // Cargar participantes
    private void cargarParticipantes() {

        participantesContainer.getChildren().clear();

        ArrayList<Usuario> participantes =
                viaje.listarParticipantes();

        contadorParticipantesLabel.setText(
            participantes.size() + " participantes"
        );

        if (participantes.isEmpty()) {

            mensajeParticipantesLabel.setVisible(true);
            mensajeParticipantesLabel.setManaged(true);

            return;
        }

        mensajeParticipantesLabel.setVisible(false);
        mensajeParticipantesLabel.setManaged(false);

        for (Usuario usuario : participantes) {

            VBox tarjeta = new VBox(5);

            tarjeta.setStyle(
                "-fx-background-color: #F8FAFC;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 12;"
            );

            Label nombre = new Label(usuario.getNombre());

            nombre.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #334155;"
            );

            Label email = new Label(usuario.getEmail());

            email.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748B;"
            );

            tarjeta.getChildren().addAll(nombre, email);

            participantesContainer.getChildren().add(tarjeta);
        }
    }

    // Cargar actividades
    private void cargarActividades() {

        actividadesContainer.getChildren().clear();

        ArrayList<Actividad> actividades =
                viaje.listarActividades();

        if (actividades.isEmpty()) {

            mensajeActividadesLabel.setVisible(true);
            mensajeActividadesLabel.setManaged(true);

            return;
        }

        mensajeActividadesLabel.setVisible(false);
        mensajeActividadesLabel.setManaged(false);

        for (Actividad actividad : actividades) {

            VBox tarjeta = new VBox(7);

            tarjeta.setStyle(
                "-fx-background-color: #F8FAFC;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 12;"
            );

            Label nombre = new Label(actividad.getNombre());

            nombre.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #334155;"
            );

            Label fecha = new Label(
                "Fecha: "
                + actividad.getFecha().format(formatoFecha)
            );

            fecha.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748B;"
            );

            String textoDescripcion =
                    actividad.getDescripcion() == null
                    || actividad.getDescripcion().isBlank()
                    ? "Sin descripción"
                    : actividad.getDescripcion();

            Label descripcion = new Label(textoDescripcion);
            descripcion.setWrapText(true);

            descripcion.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748B;"
            );

            Label precio = new Label(
                "Precio: "
                + actividad.getPrecio().toPlainString()
                + " €"
            );

            precio.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #15966A;"
            );

            tarjeta.getChildren().addAll(
                nombre,
                fecha,
                descripcion,
                precio
            );

            actividadesContainer.getChildren().add(tarjeta);
        }
    }

    // Volver a mis viajes
    @FXML
    private void volverMisViajes() {

        try {

            App.setRoot("mis-viajes");

        } catch (IOException e) {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se pudo volver a la pantalla de mis viajes."
            );

            e.printStackTrace();
        }
    }

    // Mostrar alertas
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

