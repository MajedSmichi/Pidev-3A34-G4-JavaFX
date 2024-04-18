package Controller;

import Services.UserService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.util.Duration;

public class ForgetPasswordController {

    public Button sendEmail= new Button();
    public Button returnToLogin = new Button();
    public TextField codeText;
    public Button confirmCode=new Button();
    public Label codeError;
    public PasswordField passwordText=new PasswordField();
    public Button changePassword= new Button();
    public Label cgError;
    public PasswordField confirmPasswordText=new PasswordField();

    public Label successMessage;
    public Label loginLink =new Label();

    @FXML
    private TextField emailText;

    @FXML
    private Label fpError;
    private static String verificationCode;
    private static String recipientEmail;



    @FXML
    private TextField plainPasswordField=new TextField(), plainConfirmPasswordField=new TextField();

    @FXML
    private ImageView passwordToggleImageView,confirmPasswordToggleImageView;

    public ForgetPasswordController() {
    }


    @FXML
    private void initialize() {
        sendEmail.setOnAction(event -> handleSendEmail());
        returnToLogin.setOnMouseClicked(event -> returnToLoginView());
        changePassword.setOnMouseClicked(event->handleChangePassword());
        loginLink.setOnMouseClicked(event -> returnToLoginView1());

        plainPasswordField.managedProperty().bind(plainPasswordField.visibleProperty());
        passwordText.managedProperty().bind(passwordText.visibleProperty());
        plainPasswordField.visibleProperty().bind(passwordText.visibleProperty().not());
        plainPasswordField.textProperty().bindBidirectional(passwordText.textProperty());

        plainConfirmPasswordField.managedProperty().bind(plainConfirmPasswordField.visibleProperty());
        confirmPasswordText.managedProperty().bind(confirmPasswordText.visibleProperty());
        plainConfirmPasswordField.visibleProperty().bind(confirmPasswordText.visibleProperty().not());
        plainConfirmPasswordField.textProperty().bindBidirectional(confirmPasswordText.textProperty());



    }

    // Generate a random 6-digit verification code
    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000000);
        return String.format("%06d", num);
    }


    public void sendEmail(String recipientEmail, String code) throws MessagingException {
        final String username = "smichimajed@gmail.com";
        final String password = "oxaxivwyxzrnzelz";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("smichimajed@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Password Reset Code");
            message.setText("Your password reset code is: " + code);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void handleSendEmail() {
        recipientEmail = emailText.getText().trim(); // Get email from TextField
        if (recipientEmail.isEmpty()) {
            fpError.setText("Please enter an email address."); // Prompt for an email
            return;
        }

        if (!isValidEmail(recipientEmail)) {
            fpError.setText("Please enter a valid email address."); // Email format validation failed
            return;
        }

        // Assuming UserController or the equivalent has an isEmailRegistered method
        UserService userManager = new UserService();
        if (userManager.findUserByEmail(recipientEmail)==null) {
            fpError.setText("Email not registered."); // Email is not registered
            return;
        }

        String code = generateVerificationCode();
        ForgetPasswordController.verificationCode = code;
        try {
            sendEmail(recipientEmail, code);
            navigateToCodeVerificationView(); // Refactor navigation to a method for clarity
        } catch (MessagingException e) {
            fpError.setText("Failed to send email. Please try again."); // Display error message
            e.printStackTrace();
        }
    }

    private void navigateToCodeVerificationView() {
        try {
            FXMLLoader codeVerificationViewLoader = new FXMLLoader(getClass().getResource("forgetPassword/codeConfirm.fxml"));
            Parent root = codeVerificationViewLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) sendEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            fpError.setText("Error navigating to the verification form." + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    private void handleConfirmCode() {
        String inputCode = codeText.getText().trim();
        System.out.println("Comparing Input: " + inputCode + " with Stored: " + verificationCode);
        if (inputCode.equals(ForgetPasswordController.verificationCode)) {
            switchToChangePasswordView();
        } else {
            codeError.setText("Incorrect code. Please try again.");
        }
    }

    @FXML
    public void handleChangePassword() {
        String newPassword = passwordText.getText().trim();
        String confirmPassword = confirmPasswordText.getText().trim();

        // Check if either field is empty
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            cgError.setText("Both fields are required.");
            successMessage.setVisible(false); // Ensure success message is not shown
            cgError.setVisible(true);
            return;
        }

        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            cgError.setText("Passwords do not match.");
            successMessage.setVisible(false); // Ensure success message is not shown
            cgError.setVisible(true);
            return;
        }

        // Attempt to change the password
        boolean passwordChanged = changeUserPassword(recipientEmail, newPassword); // Use recipientEmail to identify the user

        if (passwordChanged) {
            successMessage.setText("Password successfully changed.");
            successMessage.setVisible(true);
            cgError.setVisible(false);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event ->returnToLoginView2()); // Navigate to login after pause
            pause.play();
        } else {
            cgError.setText("Failed to change password.");
            successMessage.setVisible(false);
            cgError.setVisible(true);
        }
    }




    // Placeholder for actual password change logic
    private boolean changeUserPassword(String email, String newPassword) {

        UserService userManager = new UserService();
        return userManager.changePasswordByEmail(email, newPassword);
    }


    @FXML
    private void returnToLoginView() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
            Scene scene = new Scene(loginView);
            // Find a stage to use
//            Stage stage = (Stage) loginLink.getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//            Stage stage1 = (Stage) changePassword.getScene().getWindow();
//            stage1.setScene(scene);
//            stage1.show();
            Stage stage = (Stage) returnToLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Adjust the error handling as needed
            if (fpError != null) {
                fpError.setText("Error navigating to the login form.");
            }
        }
    }

    @FXML
    private void returnToLoginView1() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Adjust the error handling as needed
            if (fpError != null) {
                fpError.setText("Error navigating to the login form.");
            }
        }
    }

    @FXML
    private void returnToLoginView2() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) changePassword.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Adjust the error handling as needed
            if (fpError != null) {
                fpError.setText("Error navigating to the login form.");
            }
        }
    }



    private void switchToChangePasswordView() {
        try {
            // Load the change password view
            Parent changePasswordView = FXMLLoader.load(getClass().getResource("forgetPassword/changePassword.fxml")); // Ensure the path is correct
            Scene scene = new Scene(changePasswordView);
            Stage stage = (Stage) confirmCode.getScene().getWindow(); // Assuming `confirmCode` is part of the current scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            fpError.setText("Error navigating to the change password form.");
        }
    }


    @FXML
    private void togglePasswordVisibility() {
        boolean visibility = passwordText.isVisible();
        passwordText.setVisible(!visibility);
        passwordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "Assets/hide.png" : "Assets/view.png").toExternalForm()));
    }

    @FXML
    private void toggleConfirmPasswordVisibility() {
        boolean visibility = confirmPasswordText.isVisible();
        confirmPasswordText.setVisible(!visibility);
        confirmPasswordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "Assets/hide.png" : "Assets/view.png").toExternalForm()));
    }



}
