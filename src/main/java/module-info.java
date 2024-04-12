module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.java;
    requires unirest.java;
    requires json;
    opens Gestionreclamation to javafx.fxml;
    exports Gestionreclamation;
    exports connectionSql;
    opens connectionSql to javafx.fxml;
    exports Gestionreclamation.Controller;
    opens Gestionreclamation.Controller to javafx.fxml;
    exports Gestionreclamation.Entity;
    opens Gestionreclamation.Entity to javafx.fxml;
}