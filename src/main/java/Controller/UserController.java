package Controller;

import Entity.User;

import Services.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class UserController {
    public TextField updateProfileEmailText;
    public Label updateProfileEmailError;
    public TextField updateProfileFirstNameText;
    public Label updateProfileFirstNameError;
    public TextField updateProfileLastNameText;
    public Label updateProfileLastNameError;
    public TextField updateProfilePhoneText;
    public Label updateProfilePhoneError;
    public TextField updateProfileAdressText;
    public Label updateProfileAdressError;
    public Button updateProfileButton;
    public ImageView avatarImageView;
    @FXML
    private TableColumn<User, String> firstname=new TableColumn<>();
    @FXML
    private TableColumn<User, String> lastName=new TableColumn<>();
    @FXML
    private TableColumn<User, String> email=new TableColumn<>();
    @FXML
    private TableColumn<User, String> phone=new TableColumn<>();
    @FXML
    private TableColumn<User, String> adress=new TableColumn<>();
    private User currentUser1;
    private UserService userService = new UserService();

    @FXML
    private Label firstNameConnect;

    @FXML
    private Label lastNameConnect;

    @FXML
    private Label emailConnect;


    private String userEmail;

    public void initialize() {
        clearErrors1();
        firstname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumTele())));
        adress.setCellValueFactory(new PropertyValueFactory<>("adresse"));

    }




    private void clearErrors1() {
        updateProfileEmailError.setText("");
        updateProfilePhoneError.setText("");
        updateProfileFirstNameError.setText("");
        updateProfileLastNameError.setText("");
        updateProfileAdressError.setText("");
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    private Map<String, Boolean> validateInputs(String email, String phone, String firstName, String lastName, String address) {
        Map<String, Boolean> validationResults = new HashMap<>();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String phoneRegex = "\\d{8}";

        validationResults.put("isEmailValid", email.matches(emailRegex));
        validationResults.put("isPhoneValid", phone.matches(phoneRegex));
        validationResults.put("isFirstNameValid", !firstName.trim().isEmpty());
        validationResults.put("isLastNameValid", !lastName.trim().isEmpty());
        validationResults.put("isAddressValid", !address.trim().isEmpty());

        return validationResults;
    }

    public void showEditUser1(User user) {
        // Set the user details in the updateProfile fields
        updateProfileEmailText.setText(user.getEmail());
        updateProfileFirstNameText.setText(user.getPrenom());
        updateProfileLastNameText.setText(user.getNom());
        updateProfilePhoneText.setText(String.valueOf(user.getNumTele()));
        updateProfileAdressText.setText(user.getAdresse());
        // avatarImageView.setImage(new Image(getClass().getResource(user.getAvatar()).toExternalForm()));
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            try {
                // Get the URL of the avatar image
                URL avatarUrlResource = getClass().getResource(avatarUrl);
                if (avatarUrlResource != null) {
                    avatarImageView.setImage(new Image(avatarUrlResource.toExternalForm()));
                } else {
                    // If the URL is invalid, set a default image
                    avatarImageView.setImage(new Image(getClass().getResource("/avatars/default.png").toExternalForm()));
                }
            } catch (IllegalArgumentException e) {
                // If the URL is invalid, set a default image
                avatarImageView.setImage(new Image(getClass().getResource("/avatars/default.png").toExternalForm()));
            }
        } else {
            // If the URL is null, set a default image
            avatarImageView.setImage(new Image(getClass().getResource("/avatars/default.png").toExternalForm()));
        }

    }

    boolean handleUpdateAction1() {
        String email = updateProfileEmailText.getText();
        String firstName = updateProfileFirstNameText.getText();
        String lastName = updateProfileLastNameText.getText();
        String phone = updateProfilePhoneText.getText();
        String address = updateProfileAdressText.getText();

        Map<String, Boolean> validationResults = validateInputs(email, phone, firstName, lastName, address);

        if (!validationResults.get("isEmailValid")) {
            updateProfileEmailError.setText("Invalid email format.");
            return false;
        }
        if (!validationResults.get("isPhoneValid")) {
            updateProfilePhoneError.setText("Phone must be 8 digits.");
            return false;
        }
        if (!validationResults.get("isFirstNameValid")) {
            updateProfileFirstNameError.setText("First name is required.");
            return false;
        }
        if (!validationResults.get("isLastNameValid")) {
            updateProfileLastNameError.setText("Last name is required.");
            return false;
        }
        if (!validationResults.get("isAddressValid")) {
            updateProfileAdressError.setText("Address is required.");
            return false;
        }
        this.currentUser1 = SessionManager.getInstance().getCurrentUser();
        if (this.currentUser1 == null) {
            System.out.println("Current user is null. Cannot update user.");
            return false;
        }



        String userId = currentUser1.getId();
        boolean updatePassword = false;
        String[] roles = currentUser1.getRoles();
        User userToUpdate = new User(userId, firstName, lastName, email, roles, Integer.parseInt(phone), currentUser1.getPassword(), address,
                currentUser1.getAvatar(), currentUser1.getCreatedAt(), LocalDateTime.now(), currentUser1.isVerified());

        try {
            if (userService.updateUser(userToUpdate, updatePassword)) {
                System.out.println("User updated successfully.");
                clearErrors1();
                return true;
            } else {
                System.out.println("Failed to update user.");
                return false;
            }
        } catch (SQLException ex) {
            updateProfileEmailError.setText("Email already in use. Please use a different email.");
        }
        return false;
    }


    public void updateProfile() {
        if (updateProfileButton.getText().equals("Enregistrer Data")) {
            // Call the method to update the data in the database
            if (handleUpdateAction1()) {
                // If the update is successful, disable the text fields and change the button text
                String disabledStyle = "-fx-background-color: #938f8f; -fx-text-fill: #000000;-fx-border-color:#070707;";
                updateProfileEmailText.setEditable(false);
                updateProfileEmailText.setStyle(disabledStyle);
                updateProfileFirstNameText.setEditable(false);
                updateProfileFirstNameText.setStyle(disabledStyle);
                updateProfileLastNameText.setEditable(false);
                updateProfileLastNameText.setStyle(disabledStyle);
                updateProfilePhoneText.setEditable(false);
                updateProfilePhoneText.setStyle(disabledStyle);
                updateProfileAdressText.setEditable(false);
                updateProfileAdressText.setStyle(disabledStyle);
                updateProfileButton.setText("Update Data");
                updateProfileButton.setStyle("-fx-background-color: #318fdc; -fx-text-fill: white;");
            }
        } else {
            // Enable the text fields
            String enabledStyle = "-fx-background-color: #ffffff; -fx-text-fill: #090909;-fx-border-color:#070707;";
            updateProfileEmailText.setEditable(true);
            updateProfileEmailText.setStyle(enabledStyle);
            updateProfileFirstNameText.setEditable(true);
            updateProfileFirstNameText.setStyle(enabledStyle);
            updateProfileLastNameText.setEditable(true);
            updateProfileLastNameText.setStyle(enabledStyle);
            updateProfilePhoneText.setEditable(true);
            updateProfilePhoneText.setStyle(enabledStyle);
            updateProfileAdressText.setEditable(true);
            updateProfileAdressText.setStyle(enabledStyle);

            // Change the button text
            updateProfileButton.setText("Enregistrer Data");
            updateProfileButton.setStyle("-fx-background-color: #318fdc; -fx-text-fill: white;");
        }
    }




}
