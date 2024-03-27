package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Label loginLink;
    @FXML
    private void initialize() {

        loginLink.setOnMouseClicked(event -> navigateToLogin());
    }


    @FXML
    private void navigateToLogin() {
        try {
            Parent registrationView = FXMLLoader.load(getClass().getResource("Login/login.fxml")); // Assuming correct path
            Scene scene = new Scene(registrationView);
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
