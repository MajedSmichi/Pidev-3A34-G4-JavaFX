module com.example.gestionreclamation {
    requires javafx.controls;
    requires javafx.fxml;

    //requires com.dlsc.formsfx;


    requires java.sql;
    requires java.persistence;
    requires javafx.swing;
    requires java.desktop;
    //requires activation;
    //requires java.mail;
    //requires sendgrid.java;
    //requires java.http.client;
    opens SportHub to javafx.fxml;
    exports SportHub;
    exports connectionSql.Myconnection;
    opens connectionSql.Myconnection to javafx.fxml;
    exports SportHub.Controller;
    opens SportHub.Controller to javafx.fxml;


    exports SportHub.Controller.MyController;
    opens SportHub.Controller.MyController to javafx.fxml;
    exports SportHub.Entity.MyEntity;
    opens SportHub.Entity.MyEntity to javafx.fxml;
    exports SportHub.Services.MyServices;
    opens SportHub.Services.MyServices to javafx.fxml;


}