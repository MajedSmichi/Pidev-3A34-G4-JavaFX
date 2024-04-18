package Controller;

import Entity.User;

import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class RegisterController {
    @FXML
    private Label loginLink;

    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField, phoneTextField, adressTextField;
    @FXML
    private PasswordField passwordTextField,confirmPasswordTextField;

    @FXML
    private Label firstNameError, LastNameerror, emailError, passworderror, phoneError, adresserror, registererror,succesLabel;

    private final UserController userController = new UserController();

    @FXML
    private Button registerButton;

    @FXML
    private ImageView passwordToggleImageView,confirmPasswordToggleImageView;

    @FXML
    private TextField plainPasswordField,plainConfirmPasswordField;

    @FXML
    private CheckBox checkBoxTerms;

    @FXML
    private Label errorTerms;


    @FXML
    private void initialize() {

        loginLink.setOnMouseClicked(event -> navigateToLogin());
        registerButton.setOnAction(event -> handleRegisterButtonAction());

        plainPasswordField.managedProperty().bind(plainPasswordField.visibleProperty());
        passwordTextField.managedProperty().bind(passwordTextField.visibleProperty());
        plainPasswordField.visibleProperty().bind(passwordTextField.visibleProperty().not());
        plainPasswordField.textProperty().bindBidirectional(passwordTextField.textProperty());

        plainConfirmPasswordField.managedProperty().bind(plainConfirmPasswordField.visibleProperty());
        confirmPasswordTextField.managedProperty().bind(confirmPasswordTextField.visibleProperty());
        plainConfirmPasswordField.visibleProperty().bind(confirmPasswordTextField.visibleProperty().not());
        plainConfirmPasswordField.textProperty().bindBidirectional(confirmPasswordTextField.textProperty());
    }

    @FXML
    private ImageView avatarImageView;
    private String avatarFilePath;

    @FXML
    private void handleUploadAvatar() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {

            File destFile = new File("src/main/resources/avatars/" + file.getName());
            try {
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Create an image from the copied file
            Image image = new Image(destFile.toURI().toString());
            avatarImageView.setImage(image);

            // Save the relative path
            avatarFilePath = "/avatars/" + file.getName();
            System.out.println("Avatar File Path: " + avatarFilePath);
        }
    }


    @FXML
    private void handleRegisterButtonAction() {
        clearErrors();
        if (!validateInput()) {
            return;
        }

        User newUser = new User();
        newUser.setNom(firstNameTextField.getText());
        newUser.setPrenom(lastNameTextField.getText());
        newUser.setEmail(emailTextField.getText());
        newUser.setPassword(passwordTextField.getText());
        newUser.setNumTele(Integer.parseInt(phoneTextField.getText()));
        newUser.setAdresse(adressTextField.getText());
        newUser.setAvatar(avatarFilePath);

        try {
            UserService.insertUser(newUser);
            // Display a success message
            succesLabel.setText("Registration successful. Redirecting to login...");
            registererror.setStyle("-fx-text-fill: green;");

            // Use PauseTransition to wait for a few seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(5)); // 3 seconds wait
            pause.setOnFinished(event -> navigateToLogin()); // Navigate to login after pause
            pause.play();

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Email already registered")) {
                emailError.setText("Email already in use. Please use a different email.");
            } else {
                registererror.setText("Error during registration: " + e.getMessage());
            }
        }
    }


    private boolean validateInput() {
        boolean isValid = true;

        if (firstNameTextField.getText().isEmpty()) {
            firstNameError.setText("First name is required.");
            isValid = false;
        }

        if (lastNameTextField.getText().isEmpty()) {
            LastNameerror.setText("Last name is required.");
            isValid = false;
        }

        if (emailTextField.getText().isEmpty() || !emailTextField.getText().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            emailError.setText("Valid email is required.");
            isValid = false;
        }

        if (passwordTextField.getText().isEmpty() || passwordTextField.getText().length() < 6) {
            passworderror.setText("Password must be at least 6 characters.");
            isValid = false;
        } else if (!passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
            passworderror.setText("Passwords do not match.");
            isValid = false;
        }

        if (phoneTextField.getText().isEmpty() || phoneTextField.getText().length() != 8) {
            phoneError.setText("Phone must be 8 digits.");
            isValid = false;
        }

        if (adressTextField.getText().isEmpty()) {
            adresserror.setText("Address is required.");
            isValid = false;
        }

        // Check if the terms and conditions checkbox is not selected
        if (!checkBoxTerms.isSelected()) {
            errorTerms.setText("You must agree to the terms and conditions.");
            isValid = false;
        }

        return isValid;
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

    @FXML
    private void togglePasswordVisibility() {
        boolean visibility = passwordTextField.isVisible();
        passwordTextField.setVisible(!visibility);
        passwordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "Assets/hide.png" : "Assets/view.png").toExternalForm()));
    }

    @FXML
    private void toggleConfirmPasswordVisibility() {
        boolean visibility = confirmPasswordTextField.isVisible();
        confirmPasswordTextField.setVisible(!visibility);
        confirmPasswordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "Assets/hide.png" : "Assets/view.png").toExternalForm()));
    }

    private void clearErrors() {
        firstNameError.setText("");
        LastNameerror.setText("");
        emailError.setText("");
        passworderror.setText("");
        phoneError.setText("");
        adresserror.setText("");
        registererror.setText("");
    }
}
