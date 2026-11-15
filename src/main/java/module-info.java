module com.tripshare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.tripshare.controlador to javafx.fxml;
    exports com.tripshare;
}
