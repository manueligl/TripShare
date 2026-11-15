
package com.tripshare.controlador;

import com.tripshare.App;
import com.tripshare.modelo.Usuario;
import com.tripshare.modelo.Viaje;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MisViajesController {

    @FXML
    private Label saludoLabel;

    @FXML
    private FlowPane viajesContainer;

    @FXML
    private Label mensajeVacioLabel;

    @FXML
    public void initialize() {
        cargarViajes();
    }

    // Cargar los viajes del usuario autenticado
    private void cargarViajes() {

        Usuario usuario = App.usuarioActual;

        if (usuario == null) {
            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error de sesión",
                "No se ha encontrado ningún usuario autenticado."
            );
            return;
        }

        saludoLabel.setText("¡Hola, " + usuario.getNombre() + "!");

        ArrayList<Viaje> viajes =
                Viaje.listarPorUsuario(usuario.getIdUsuario());

        viajesContainer.getChildren().clear();

        if (viajes.isEmpty()) {
            mensajeVacioLabel.setVisible(true);
            mensajeVacioLabel.setManaged(true);
            return;
        }

        mensajeVacioLabel.setVisible(false);
        mensajeVacioLabel.setManaged(false);

        for (Viaje viaje : viajes) {
            viajesContainer.getChildren().add(
                crearTarjetaViaje(viaje)
            );
        }
    }

    // Crear una tarjeta visual para cada viaje
    private VBox crearTarjetaViaje(Viaje viaje) {

        VBox tarjeta = new VBox(12);

        tarjeta.setPrefWidth(400);
        tarjeta.setMinHeight(200);

        tarjeta.setStyle(
            "-fx-background-color: white;"
            + "-fx-background-radius: 12;"
            + "-fx-padding: 20;"
            + "-fx-border-color: #E2E8F0;"
            + "-fx-border-radius: 12;"
        );

        // Indicar que la tarjeta es seleccionable
        tarjeta.setCursor(Cursor.HAND);

        tarjeta.setOnMouseClicked(event -> abrirDetallesViaje(viaje));

        Label nombre = new Label(viaje.getNombre());
        nombre.setStyle(
            "-fx-font-size: 19px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: #25364A;"
        );

        Label destino = new Label("📍 " + viaje.getDestino());
        destino.setStyle(
            "-fx-font-size: 14px;"
            + "-fx-text-fill: #64748B;"
        );

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Label fechas = new Label(
            "📅 " + viaje.getFechaInicio().format(formato)
            + " - " + viaje.getFechaFin().format(formato)
        );

        fechas.setStyle(
            "-fx-font-size: 12px;"
            + "-fx-text-fill: #64748B;"
        );

        Label presupuesto = new Label(
            "Presupuesto: "
            + viaje.getPresupuesto().toPlainString()
            + " €"
        );

        presupuesto.setStyle(
            "-fx-font-size: 14px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: #15966A;"
        );

        Label participantes = new Label(
            "Participantes: "
            + viaje.listarParticipantes().size()
        );

        participantes.setStyle(
            "-fx-font-size: 12px;"
            + "-fx-text-fill: #64748B;"
        );

        tarjeta.getChildren().addAll(
            nombre,
            destino,
            fechas,
            presupuesto,
            participantes
        );

        return tarjeta;
    }

    // Abrir los detalles del viaje seleccionado
    private void abrirDetallesViaje(Viaje viaje) {

        try {
            App.viajeSeleccionado = viaje;
            App.setRoot("detalles-viaje");

        } catch (java.io.IOException e) {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se pudo abrir la pantalla de detalles del viaje."
            );

            e.printStackTrace();
        }
    }

    // Acción del botón Crear viaje
    @FXML
    private void crearViaje() {

        try {
            App.setRoot("crear-viaje");

        } catch (java.io.IOException e) {

            mostrarAlerta(
                Alert.AlertType.ERROR,
                "Error",
                "No se pudo abrir la pantalla de creación de viajes."
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
