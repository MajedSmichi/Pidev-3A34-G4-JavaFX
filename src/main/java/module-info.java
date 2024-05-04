module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;

    //requires com.dlsc.formsfx;
    requires java.sql;
    requires unirest.java;
    requires jdk.jsobject;
    requires javafx.web;
    requires com.google.gson;
    requires twilio;
    requires stripe.java;
    requires com.google.zxing;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.swing;

    opens SportHub.Entity to com.google.gson;

    //requires java.persistence;
    opens SportHub to javafx.fxml;
    exports SportHub;
    exports connectionSql;
    opens connectionSql to javafx.fxml;
    exports SportHub.Controller;
    opens SportHub.Controller to javafx.fxml;
    exports SportHub.Entity;
    exports SportHub.Services;
    opens SportHub.Services to com.google.gson;
}