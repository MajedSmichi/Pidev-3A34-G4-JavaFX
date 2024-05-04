module com.example.GestionSalle {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.mail;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    //requires java.persistence;
    opens GestionSalle to javafx.fxml;
    exports GestionSalle;
    exports connectionSql;
    opens connectionSql to javafx.fxml;
    exports GestionSalle.Controller;
    opens GestionSalle.Controller to javafx.fxml;
    exports GestionSalle.Entity;
    opens GestionSalle.Entity to javafx.fxml;
}