package Gestionreclamation.Controller;

import Gestionreclamation.Entity.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Properties;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterController {
    private User user = new User();


    @FXML
    private Label loginLink = new Label();

    private static String recipientEmail;

    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField, phoneTextField, adressTextField;
    @FXML
    private PasswordField passwordTextField = new PasswordField(), confirmPasswordTextField = new PasswordField();

    @FXML
    private Label firstNameError, LastNameerror, emailError, passworderror, phoneError, adresserror, registererror, succesLabel;

    private final UserController userController = new UserController();

    @FXML
    private Button registerButton = new Button();

    @FXML
    private ImageView passwordToggleImageView, confirmPasswordToggleImageView;

    @FXML
    private TextField plainPasswordField = new TextField(), plainConfirmPasswordField = new TextField();

    @FXML
    private CheckBox checkBoxTerms;

    @FXML
    private Label errorTerms;

    public WebView captchaWebView;


    @FXML
    private void initialize() {

        loginLink.setOnMouseClicked(event -> navigateToLogin());
        registerButton.setOnAction(event -> {
            handleRegisterButtonAction();
            if (validateInput()) {
                handleRegisterButtonAction();
            }
        });

        plainPasswordField.managedProperty().bind(plainPasswordField.visibleProperty());
        passwordTextField.managedProperty().bind(passwordTextField.visibleProperty());
        plainPasswordField.visibleProperty().bind(passwordTextField.visibleProperty().not());
        plainPasswordField.textProperty().bindBidirectional(passwordTextField.textProperty());

        plainConfirmPasswordField.managedProperty().bind(plainConfirmPasswordField.visibleProperty());
        confirmPasswordTextField.managedProperty().bind(confirmPasswordTextField.visibleProperty());
        plainConfirmPasswordField.visibleProperty().bind(confirmPasswordTextField.visibleProperty().not());
        plainConfirmPasswordField.textProperty().bindBidirectional(confirmPasswordTextField.textProperty());
        WebEngine engine = captchaWebView.getEngine();
        engine.load("http://localhost/captcha.html");

        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();

            if (newValue.contains(firstName) || newValue.contains(lastName)) {
                passworderror.setText("PWD must not contain first name or last name.");
            } else if (!newValue.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
                passworderror.setText("PWD must contain at least letter,number, and be at least 6 characters long.");
            } else {
                int strength = calculatePasswordStrength(newValue);
                switch (strength) {
                    case 0:
                    case 1:
                        passworderror.setText("Weak password");
                        break;
                    case 2:
                        passworderror.setText("Medium password");
                        break;
                    case 3:
                        passworderror.setText("Strong password");
                        break;
                }
            }
        });

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
            File destDir = new File("src/main/resources/avatars/");
            if (!destDir.exists()) {
                destDir.mkdirs(); // Create the directory if it does not exist
            }

            File destFile = new File(destDir, file.getName());
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
            avatarFilePath = "/Gestionreclamation/avatars/" + file.getName();
            System.out.println("Avatar File Path: " + avatarFilePath);
        }
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;

        // If it has at least one number, one lowercase letter, one uppercase letter, and at least 8 characters, it is strong
        if (password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}")) {
            strength = 3;
        }
        // If it has at least one number, one letter, and at least 6 characters, it is medium
        else if (password.matches("(?=.*[0-9])(?=.*[a-zA-Z]).{6,}")) {
            strength = 2;
        }
        // If it has at least one number or one letter, and at least 4 characters, it is weak
        else if (password.matches("(?=.*[0-9a-zA-Z]).{4,}")) {
            strength = 1;
        }

        return strength;
    }

    @FXML
    private void handleRegisterButtonAction() {
        clearErrors();
        if (!validateInput()) {
            registererror.setText("Please fill in all fields correctly.");
            return;
        }

        try {
            WebEngine engine = captchaWebView.getEngine();
            String result = (String) engine.executeScript(
                    "function isRecaptchaVerified() {" +
                            " var isVerified = grecaptcha.getResponse().length > 0;" +
                            " return String(isVerified);" +
                            "} " +
                            "isRecaptchaVerified();"
            );
            if(result.equals("false")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("captcha");
                alert.setContentText("Please check the captcha.");
                alert.showAndWait();
                System.out.println("erreur");
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Assign user details from form
        this.user.setNom(firstNameTextField.getText());
        this.user.setPrenom(lastNameTextField.getText());
        this.user.setEmail(emailTextField.getText());
        this.user.setPassword(passwordTextField.getText());
        this.user.setNumTele(Integer.parseInt(phoneTextField.getText()));
        this.user.setAdresse(adressTextField.getText());
        this.user.setAvatar(avatarFilePath);



        // Continue with registration and send email
        String code = generateVerificationCode();
        CodeVerificationController.verificationCodeEmail = code;
        try {
            registererror.setText("Wait an email!Check your mailbox.");
            registererror.setVisible(true);
            registererror.setOpacity(1.0);
            sendEmail(this.user.getEmail(), code);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> navigateToCodeVerificationView(user));
            pause.play();
        } catch (MessagingException e) {
            registererror.setText("Failed to send email. Please try again.");
            e.printStackTrace();
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

        if (phoneTextField.getText().isEmpty() || !phoneTextField.getText().matches("\\d{8}")) {
            phoneError.setText("Phone must be 8 digits.");
            isValid = false;
        }

        if (adressTextField.getText().isEmpty()) {
            adresserror.setText("Address is required.");
            isValid = false;
        }

        if (!checkBoxTerms.isSelected()) {
            errorTerms.setText("You must agree to the terms and conditions.");
            isValid = false;
        }

        return isValid;
    }


    @FXML
    private void navigateToLogin() {
        try {
            Parent registrationView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/Login/login.fxml")); // Assuming correct path
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
        passwordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "/Gestionreclamation/Assets/hide.png" : "/Gestionreclamation/Assets/view.png").toExternalForm()));
    }

    @FXML
    private void toggleConfirmPasswordVisibility() {
        boolean visibility = confirmPasswordTextField.isVisible();
        confirmPasswordTextField.setVisible(!visibility);
        confirmPasswordToggleImageView.setImage(new Image(getClass().getResource(visibility ? "/Gestionreclamation/Assets/hide.png" : "/Gestionreclamation/Assets/view.png").toExternalForm()));
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

    private void navigateToCodeVerificationView(User user) {
        try {
            FXMLLoader codeVerificationViewLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/Register/codeConfirm.fxml"));
            Parent root = codeVerificationViewLoader.load();


            CodeVerificationController codeVerificationController = codeVerificationViewLoader.getController();
            codeVerificationController.setUser(user);

            Scene scene = new Scene(root);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            if (stage == null) {
                // Handle case where stage is null
                System.out.println("Stage is null. Cannot navigate.");
                return;
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            registererror.setText("Error navigating to the verification form." + e.getMessage());
        }
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

    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000000);
        return String.format("%06d", num);
    }



}
