module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;

    //requires com.dlsc.formsfx;
    requires java.sql;
    requires com.jfoenix;
    requires itextpdf;
    //requires java.persistence;
    opens SportHub to javafx.fxml;
    exports SportHub;
    exports connectionSql;
    opens connectionSql to javafx.fxml;
    exports SportHub.Controller;
    opens SportHub.Controller to javafx.fxml;
    exports SportHub.Entity;
    opens SportHub.Entity to javafx.fxml;
}