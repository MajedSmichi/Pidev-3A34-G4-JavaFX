package Controller;

import Entity.User;
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

    public Label forgetPassword;
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
        forgetPassword.setOnMouseClicked(event -> navigateToFogetPassword());
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
                // Assuming you have a method getUserByEmail that returns a User object
                User user = getUserByEmail(email);
                SessionManager.getInstance().setCurrentUser(user); // Set the current user in session

                navigateToUserList(email);
            } else {
                errorLabel.setText("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error connecting to the database.");
        }

    }


    private void navigateToUserList(String userEmail) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCrud/userList.fxml"));
            Parent userListRoot = loader.load();

            // Get the controller of userList.fxml and pass the email
            UserController userController = loader.getController();
            userController.setUserEmail(userEmail); // Assuming you have a method in UserController to set the email

            // Show in the current stage
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(userListRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error navigating to user list.");
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

    @FXML
    private void navigateToFogetPassword() {
        try {
            Parent forgetPasswordView = FXMLLoader.load(getClass().getResource("forgetPassword/sendEmail.fxml"));
            Scene scene = new Scene(forgetPasswordView);
            Stage stage = (Stage) createAccountLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error navigating to the forgetPassword form.");
        }
    }

    private User getUserByEmail(String email) {
        User user = null;

        try {
            Connection conn = ConnectionSql.getConnection();
            PreparedStatement getUserStmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            getUserStmt.setString(1, email);

            ResultSet rs = getUserStmt.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");

                String role = rs.getString("role");
                int numTele = rs.getInt("numTele");
                String motDePass = rs.getString("motDePass");
                String adresse = rs.getString("adresse");

                user = new User(id, nom, prenom, email, role, numTele, motDePass, adresse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }



}

