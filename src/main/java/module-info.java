module Controller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;

    requires mysql.connector.java;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
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
}