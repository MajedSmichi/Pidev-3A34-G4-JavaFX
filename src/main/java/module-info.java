module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
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