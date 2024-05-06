module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;

    requires mysql.connector.java;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires jbcrypt;
    requires java.mail;
    requires com.google.gson;
    requires com.google.api.client;
    requires google.api.client;
    requires com.google.api.client.json.jackson2;
    requires jdk.httpserver;
    requires java.json;
    requires twilio;
    requires java.net.http;
    requires org.json;

    exports connectionSql;
    opens connectionSql to javafx.fxml;
    requires unirest.java;
    requires assemblyai.java;
    requires com.google.zxing;
    requires com.google.zxing.javase;


    requires poi.ooxml;
    requires poi;
    requires itextpdf;
    requires org.controlsfx.controls;
    requires jdk.jsobject;
    requires java.datatransfer;
    requires stripe.java;
    requires java.desktop;
    requires javafx.swing; // Only use poi-ooxml
    opens Gestionreclamation to javafx.fxml;
    exports Gestionreclamation;
    exports Gestionreclamation.Controller;
    opens Gestionreclamation.Controller to javafx.fxml;
    exports Gestionreclamation.Entity;
    opens Gestionreclamation.Entity to javafx.fxml;
    exports Gestionreclamation.Controller.Salle to javafx.fxml;
    opens Gestionreclamation.Controller.Salle to javafx.fxml;

    // Add these lines for gestionsalle

    exports Gestionreclamation.SportHub.Controller to javafx.fxml;
    opens Gestionreclamation.SportHub.Controller to javafx.fxml;

    // Add these lines for gestionsalle
}