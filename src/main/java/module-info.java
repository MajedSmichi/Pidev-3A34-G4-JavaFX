module Controller {
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


    opens Controller to javafx.fxml;
    opens Entity to javafx.base;
    exports Controller;
    exports connectionSql;
    opens connectionSql to javafx.fxml;
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