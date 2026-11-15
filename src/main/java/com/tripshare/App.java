package com.tripshare;

import com.tripshare.modelo.Usuario;
import com.tripshare.modelo.Viaje;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    // Usuario que ha iniciado sesión
    public static Usuario usuarioActual;
    
    public static Viaje viajeSeleccionado;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("primary"), 1000, 700);

        stage.setTitle("TripShare");
        stage.setMinWidth(850);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
            App.class.getResource("/com/tripshare/vista/" + fxml + ".fxml")
        );

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
