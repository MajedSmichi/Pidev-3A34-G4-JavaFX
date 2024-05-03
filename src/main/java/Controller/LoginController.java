package Controller;

import Entity.User;
import Services.UserService;
import connectionSql.ConnectionSql;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LoginController {
    private int verificationCode;

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

    private int failedLoginAttempts = 0;


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

    private String convertBcryptPrefixTo2a(String hash) {
        if (hash != null && hash.startsWith("$2y$")) {
            return "$2a$" + hash.substring(4);
        }
        return hash;
    }


public void LoginButtonAction()  {

    String email = emailTextField.getText();
    String password = passwordTextField.getText();

    if (email.isBlank() || password.isBlank()) {
        errorLabel.setText("Please enter email and password");
        return;
    }

    try {
        Connection conn = ConnectionSql.getConnection();
        PreparedStatement checkEmailExists = conn.prepareStatement("SELECT count(1), Password FROM user WHERE email = ?");
        checkEmailExists.setString(1, email);
        ResultSet emailExistsRS = checkEmailExists.executeQuery();

        if (emailExistsRS.next() && emailExistsRS.getInt(1) == 0) {
            errorLabel.setText("User not registered yet.");
            return;
        }

        String storedPasswordHash = emailExistsRS.getString("Password");

        storedPasswordHash = convertBcryptPrefixTo2a(storedPasswordHash);

        if (BCrypt.checkpw(password, storedPasswordHash)) {
            failedLoginAttempts = 0;
            User user = UserService.findUserByEmail(email);
            SessionManager.getInstance().setCurrentUser(user);

            // Check if the user is verified
            if (!user.isVerified()) {
                errorLabel.setText("Your account is banned.");
                return;
            }

            if (Arrays.asList(user.getRoles()).contains("ROLE_CLIENT")) {
                navigateToFrontView();
            } else {
                navigateToUserList(email);
            }
        } else {
            failedLoginAttempts++;
            if (failedLoginAttempts == 2) {
                errorLabel.setText("This is your last try.");
            } else if (failedLoginAttempts >= 3){
                errorLabel.setText("Your account is banned.");
                User user = UserService.findUserByEmail(email);
                SessionManager.getInstance().setCurrentUser(user);
                user.setVerified(false);
                UserService.updateUserActiveStatus(user.getId(),user.isVerified());
                System.out.println("user: " + user);
                verificationCode = new Random().nextInt(900000) + 100000;
                String numTele = "+216" + user.getNumTele();
                WhatsAppSender.main(new String[]{String.valueOf(verificationCode), numTele});
                showVerificationCodeAlert();
                failedLoginAttempts = 0;
                errorLabel.setText("Your account is banned.");
                return;
            }
            errorLabel.setText("Invalid credentials");
        }
    } catch (Exception e) {
        e.printStackTrace();
        errorLabel.setText("Error connecting to the database.");
    }
}

private void showVerificationCodeAlert() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Verification Code");
    dialog.setHeaderText("Enter the verification code sent to your WhatsApp:");
    dialog.setContentText("Verification Code:");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(code -> {
        if (code.isEmpty()) {
            // Display an alert when the code is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Verification Failed");
            alert.setHeaderText(null);
            alert.setContentText("No code entered. Please enter the verification code.");
            alert.showAndWait();
        } else {
            int enteredCode = Integer.parseInt(code);
            User user = SessionManager.getInstance().getCurrentUser();

            if (enteredCode == verificationCode) {
                user.setVerified(true);
                UserService.updateUserActiveStatus(user.getId(),user.isVerified());
            } else {
                user.setVerified(false);
                UserService.updateUserActiveStatus(user.getId(),user.isVerified());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Verification Failed");
                alert.setHeaderText(null);
                alert.setContentText("The code is incorrect. Your account is banned.");
                alert.showAndWait();
                errorLabel.setText("The code is incorrect.");
            }
        }
    });
}


    private void navigateToUserList(String userEmail) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dash.fxml"));
            Parent userListRoot = loader.load();

            // Get the controller of userList.fxml and pass the email
            SampleController sampleController = loader.getController();
            UserController userController=new UserController();
            userController.setUserEmail(userEmail);

            // Show in the current stage
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(userListRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error navigating to user list.");
        }
    }

    private void navigateToFrontView() {
        try {
            Parent frontView = FXMLLoader.load(getClass().getResource("Frontview.fxml"));
            Scene scene = new Scene(frontView);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error navigating to the front view.");
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

//    private User getUserByEmail(String email) {
//        User user = null;
//
//        try {
//            Connection conn = ConnectionSql.getConnection();
//            PreparedStatement getUserStmt = conn.prepareStatement(
//                    "SELECT id, nom, prenom, email, roles, num_tele, Password, adresse, avatar, created_at, updated_at, is_verified FROM user WHERE email = ?"
//            );
//            getUserStmt.setString(1, email);
//
//            ResultSet rs = getUserStmt.executeQuery();
//
//            if (rs.next()) {
//                String id = rs.getString("id");
//                String nom = rs.getString("nom");
//                String prenom = rs.getString("prenom");
//                String roleJson = rs.getString("roles");
//                String[] roles = new Gson().fromJson(roleJson, new TypeToken<String[]>(){}.getType());
//                int numTele = rs.getInt("num_tele");
//                String Password = rs.getString("Password");
//                String adresse = rs.getString("adresse");
//                String avatar = rs.getString("avatar");
//                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
//                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
//                boolean isVerified = rs.getBoolean("is_verified");
//
//                user = new User(id, nom, prenom, email, roles, numTele, Password, adresse, avatar, createdAt, updatedAt, isVerified);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return user;
//    }




}

