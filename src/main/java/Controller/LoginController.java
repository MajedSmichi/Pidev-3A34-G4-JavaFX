package Controller;

import Entity.User;
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
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
            User user = getUserByEmail(email);
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
            if (failedLoginAttempts >= 3) {
                // Send an email to the user
                sendEmail(email);
                failedLoginAttempts = 0;
            }
            errorLabel.setText("Invalid credentials");
        }
    } catch (Exception e) {
        e.printStackTrace();
        errorLabel.setText("Error connecting to the database.");
    }
}

    private void sendEmail(String email) {
        // Your email sending code here...
        String host = "smtp.gmail.com"; // Gmail SMTP server
        String from = "smichimajed@gmail.com"; // replace with your email
        String password = "oxaxivwyxzrnzelz"; // replace with your email password

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Account Login Attempt");

            // HTML message
            String htmlMessage = "<h1 style='color:blue;'>Account Login Attempt</h1>" +
                    "<p>There have been multiple failed login attempts on your account. If this was not you, please click the 'No' button below.</p>" +
                    "<a href='http://localhost:80443/response?email=" + email + "&action=yes'>Yes</a>" +
                    "<a href='http://localhost:80443/response?email=" + email + "&action=no'>No</a>";

            message.setContent(htmlMessage, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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

    private User getUserByEmail(String email) {
        User user = null;

        try {
            Connection conn = ConnectionSql.getConnection();
            PreparedStatement getUserStmt = conn.prepareStatement(
                    "SELECT id, nom, prenom, email, roles, num_tele, Password, adresse, avatar, created_at, updated_at, is_verified FROM user WHERE email = ?"
            );
            getUserStmt.setString(1, email);

            ResultSet rs = getUserStmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String roleJson = rs.getString("roles");
                String[] roles = new Gson().fromJson(roleJson, new TypeToken<String[]>(){}.getType());
                int numTele = rs.getInt("num_tele");
                String Password = rs.getString("Password");
                String adresse = rs.getString("adresse");
                String avatar = rs.getString("avatar");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                boolean isVerified = rs.getBoolean("is_verified");

                user = new User(id, nom, prenom, email, roles, numTele, Password, adresse, avatar, createdAt, updatedAt, isVerified);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }




}

