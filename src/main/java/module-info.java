module com.tripshare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.tripshare to javafx.fxml;
    exports com.tripshare;
}
