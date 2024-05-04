module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.java;
    requires unirest.java;
    requires json;
    requires javax.mail.api;
    requires java.net.http;
    requires assemblyai.java;
    opens Gestionreclamation to javafx.fxml;
    exports Gestionreclamation;
    exports connectionSql;
    opens connectionSql to javafx.fxml;
    exports Gestionreclamation.Controller;
    opens Gestionreclamation.Controller to javafx.fxml;
    exports Gestionreclamation.Entity;
    opens Gestionreclamation.Entity to javafx.fxml;
}