package Controller;

import connectionSql.ConnectionSql;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField plainPasswordField;

    @FXML
    private ImageView passwordToggleImageView;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label createAccountLabel;


    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> LoginButtonAction());
        createAccountLabel.setOnMouseClicked(event -> navigateToRegistration());
        plainPasswordField.managedProperty().bind(plainPasswordField.visibleProperty());
        passwordTextField.managedProperty().bind(passwordTextField.visibleProperty());
        plainPasswordField.visibleProperty().bind(passwordTextField.visibleProperty().not());
        plainPasswordField.textProperty().bindBidirectional(passwordTextField.textProperty());
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean visibility = passwordTextField.isVisible();
        passwordTextField.setVisible(!visibility);
        passwordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "Assets/hide.png" : "Assets/view.png").toExternalForm()));
    }


    public void LoginButtonAction() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Please enter email and password");
            return;
        }

        try {
            Connection conn = ConnectionSql.getConnection();

            PreparedStatement checkEmailExists = conn.prepareStatement("SELECT count(1), motDePass FROM users WHERE email = ?");
            checkEmailExists.setString(1, email);
            ResultSet emailExistsRS = checkEmailExists.executeQuery();

            if (emailExistsRS.next() && emailExistsRS.getInt(1) == 0) {
                errorLabel.setText("User not registered yet.");
                return;
            }


            String storedPasswordHash = emailExistsRS.getString("motDePass");


            if (BCrypt.checkpw(password, storedPasswordHash)) {
                errorLabel.setText("Login Successful");
            } else {
                errorLabel.setText("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error connecting to the database.");
        }
    }



    @FXML
    private void navigateToRegistration() {
        try {
            Parent registrationView = FXMLLoader.load(getClass().getResource("Register/register.fxml")); // Assuming correct path
            Scene scene = new Scene(registrationView);
            Stage stage = (Stage) createAccountLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error navigating to the registration form.");
        }
    }

}

