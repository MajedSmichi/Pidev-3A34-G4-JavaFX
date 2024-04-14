package Controller;

import Entity.User;
import connectionSql.ConnectionSql;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static Controller.UserController.selectUser;


public class SampleController implements UserCardRefreshListener {
    public Button closeDataViewButton;
    public Label viewAddressLabel;
    public Label viewPhoneLabel;
    public Label viewLastNameLabel;
    public Label viewFirstNameLabel;
    public Label viewEmailLabel;
    public AnchorPane DataView;
    private User currentUser;
    public AnchorPane EditField;
    public TextField updateEmailText;
    public Label updateEmailError;
    public TextField updateFirstNameText;
    public TextField updateLastNameText;
    public Label updateFirstNameError;
    public Label updateLastNameError;
    public TextField updatePhoneText;
    public Label updatePhoneError;
    public TextField updateAdressText;
    public Button updateDataButton;
    public Label updateAdressError;
    @FXML
    private TilePane HBoxList;
    @FXML
    private ScrollPane userListScrollPane;
    @FXML
    private TextField searchField;
    private static final String UPDATE_USERS_SQL = "update users set nom = ?,prenom= ?, email =?, role =?, numTele =?, motDePass =?, adresse =? where id = ?;";

    public void initialize() {
        EditField.setVisible(false);
        // loadUserListLayout();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
    }

    public void loadUserListLayout() {
        UserCardController.loadUsersIntoCards(HBoxList, this);
        userListScrollPane.setVisible(true);
        searchField.setVisible(true);
    }

    @Override
    public void refreshUserList() {
        loadUserListLayout();
    }

    private void filterUsers(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadUserListLayout();
        } else {
            HBoxList.getChildren().clear();
            UserCardController.loadFilteredUsersIntoCards(HBoxList, searchTerm, this);
        }
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

    public void showEditUser(User user) {
        this.currentUser = user;
        userListScrollPane.setVisible(false);
        searchField.setVisible(false);
        EditField.setVisible(true);
        DataView.setVisible(false);
        updateEmailText.setText(user.getEmail());
        updateFirstNameText.setText(user.getPrenom());
        updateLastNameText.setText(user.getNom());
        updatePhoneText.setText(String.valueOf(user.getNumTele()));
        updateAdressText.setText(user.getAdresse());
    }

    public boolean updateUser(User user, boolean updatePassword) throws SQLException {
        // Fetch existing user data
        User existingUser = selectUser(user.getId());
        if (existingUser == null) {
            throw new SQLException("User not found with ID: " + user.getId());
        }

        // Check if a new password is provided and should be updated
        String hashedPassword = updatePassword ? BCrypt.hashpw(user.getMotDePass(), BCrypt.gensalt()) : existingUser.getMotDePass();

        boolean rowUpdated;
        try (Connection connection = ConnectionSql.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            // Use the role from the provided user object or existing user if not updated
            statement.setString(4, user.getRole() != null && !user.getRole().isEmpty() ? user.getRole() : existingUser.getRole());
            statement.setInt(5, user.getNumTele());
            statement.setString(6, hashedPassword);
            statement.setString(7, user.getAdresse());
            statement.setInt(8, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }


    @FXML
    private void handleUpdateAction() {
        String email = updateEmailText.getText();
        String firstName = updateFirstNameText.getText();
        String lastName = updateLastNameText.getText();
        String phone = updatePhoneText.getText();
        String address = updateAdressText.getText();

        Map<String, Boolean> validationResults = validateInputs(email, phone, firstName, lastName, address);

        if (!validationResults.get("isEmailValid")) {
            updateEmailError.setText("Invalid email format.");
            return;
        }
        if (!validationResults.get("isPhoneValid")) {
            updatePhoneError.setText("Phone must be 8 digits.");
            return;
        }
        if (!validationResults.get("isFirstNameValid")) {
            updateFirstNameError.setText("First name is required.");
            return;
        }
        if (!validationResults.get("isLastNameValid")) {
            updateLastNameError.setText("Last name is required.");
            return;
        }
        if (!validationResults.get("isAddressValid")) {
            updateAdressError.setText("Address is required.");
            return;
        }

        int userId = currentUser.getId();
        User currentUser = selectUser(userId);
        boolean updatePassword = false;

        User userToUpdate = new User(userId, firstName, lastName, email, currentUser.getRole(), Integer.parseInt(phone), currentUser.getMotDePass(), address);

        try {
            if (updateUser(userToUpdate, updatePassword)) {
                System.out.println("User updated successfully.");
                clearErrors();
                EditField.setVisible(false);
                loadUserListLayout();
            } else {
                System.out.println("Failed to update user.");
            }
        } catch (SQLException ex) {
            System.out.println("Error updating user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearErrors() {
        updateEmailError.setText("");
        updatePhoneError.setText("");
        updateFirstNameError.setText("");
        updateLastNameError.setText("");
        updateAdressError.setText("");
    }

    public void showViewUser(User user) {

        this.currentUser = user;

        userListScrollPane.setVisible(false);
        searchField.setVisible(false);
        EditField.setVisible(false);

        // Set user details in view labels
        viewFirstNameLabel.setText(user.getPrenom());
        viewLastNameLabel.setText(user.getNom());
        viewEmailLabel.setText(user.getEmail());
        viewPhoneLabel.setText(String.valueOf(user.getNumTele()));
        viewAddressLabel.setText(user.getAdresse());
        DataView.setVisible(true);
        //closeDataViewButton.setVisible(true); // Ensure this button is configured to hide the user details view when clicked
    }

    @FXML
    private void handleCloseDataViewAction() {
        viewFirstNameLabel.setText("");
        viewLastNameLabel.setText("");
        viewEmailLabel.setText("");
        viewPhoneLabel.setText("");
        viewAddressLabel.setText("");

        DataView.setVisible(false);
        userListScrollPane.setVisible(true);
        searchField.setVisible(true);
    }

    @FXML
    private void handleEditFromView() {
        if (currentUser != null) {
            showEditUser(currentUser);
        }
    }


}
