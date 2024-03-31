module Controller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires mysql.connector.java;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires java.mail;


    opens Controller to javafx.fxml;
    opens Entity to javafx.base;
    exports Controller;

}