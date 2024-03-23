module com.example.pidev3a34g4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.pidev3a34g4 to javafx.fxml;
    exports com.example.pidev3a34g4;
}