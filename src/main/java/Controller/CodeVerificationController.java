package Controller;

import Entity.User;
import Services.UserService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class CodeVerificationController {
    public static String verificationCodeEmail;
    public Button confirmCode;
    private User user;
    public TextField codeText;

    public Label codeSucces;
    public Label codeError;
    @FXML
    private Label loginLink=new Label();

    @FXML
    public void initialize() {
        confirmCode.setOnAction(event -> {
            try {
                handleConfirmCode();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        loginLink.setOnMouseClicked(event -> navigateToLogin());
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleConfirmCode() throws SQLException {
        String inputCode = codeText.getText().trim();
        System.out.println("Comparing Input: " + inputCode + " with Stored: " + verificationCodeEmail);
        if (inputCode.equals(CodeVerificationController.verificationCodeEmail)) {
            codeError.setText("");
            codeSucces.setText("Registration successfully...");
            user.setVerified(true);
            UserService.insertUser(user);

            PauseTransition pause = new PauseTransition(Duration.seconds(5)); // 3 seconds wait
            pause.setOnFinished(event -> navigateToLogin()); // Navigate to login after pause
            pause.play();
        } else {
            codeError.setText("Incorrect code. Please try again.");
        }
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