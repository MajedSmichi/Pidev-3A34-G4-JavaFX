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
    requires org.apache.poi.poi;
    requires twilio;
    requires java.net.http;
    requires org.json;


    exports connectionSql;
    opens connectionSql to javafx.fxml;
    requires unirest.java;
    requires assemblyai.java;
    opens Gestionreclamation to javafx.fxml;
    exports Gestionreclamation;
    exports Gestionreclamation.Controller;
    opens Gestionreclamation.Controller to javafx.fxml;
    exports Gestionreclamation.Entity;
    opens Gestionreclamation.Entity to javafx.fxml;
}