package Controller;

import Entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connectionSql.ConnectionSql;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


public class UserController {
    private User currentUser;
    private String userEmail;
    private static final String INSERT_USERS_SQL = "INSERT INTO user ( nom, prenom, email, roles, num_tele, Password, adresse, avatar, created_at, updated_at, is_verified) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,nom,prenom,email,roles,num_tele,Password,adresse,avatar,created_at,updated_at,is_verified from user where id =?";
    private static final String SELECT_ALL_USERS = "select * from user";
    private static final String DELETE_USERS_SQL = "delete from user where id = ?;";
    private static final String UPDATE_USERS_SQL = "update user set nom = ?,prenom= ?, email =?, roles =?, num_tele =?, Password =?, adresse =? where id = ?;";
    public TextField updateEmailText;
    public Label updateEmailError;
    public TextField updateFirstNameText;
    public TextField updateLastNameText;
    public Label updateFirstNameError;
    public Label updateLastNameError;
    public TextField updatePhoneText;
    public Label updatePhoneError;
    public TextField updateAdressText;
    public Label updateAdressError;


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

    @FXML
    private Label firstNameConnect;

    @FXML
    private Label lastNameConnect;

    @FXML
    private Label emailConnect;

    @FXML
    private Button logoutButton=new Button();

    public void initialize() {
        updateUserInfo();
        firstname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumTele())));
        adress.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        logoutButton.setOnAction(event -> logout());
    }

    private void updateUserInfo() {

        firstNameConnect.setText(SessionManager.getInstance().getUserFirstName());
        lastNameConnect.setText(SessionManager.getInstance().getUserLastName());
        emailConnect.setText(SessionManager.getInstance().getUserEmail());
    }

    private void logout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    //  insert user
    public void insertUser(User user) throws SQLException {
        if (emailExists(user.getEmail())) {
            throw new SQLException("Email already registered.");
        }

        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            hashedPassword = hashedPassword.replaceFirst("\\$2a\\$", "\\$2y\\$");
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            user.setRoles(new String[]{"ROLE_CLIENT"});
            String rolesJson = new Gson().toJson(user.getRoles());
            preparedStatement.setString(4, rolesJson);
            preparedStatement.setInt(5, user.getNumTele());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.setString(7, user.getAdresse());
            preparedStatement.setString(8, user.getAvatar());
            user.setCreatedAt(LocalDateTime.now());
            preparedStatement.setTimestamp(9, Timestamp.valueOf(user.getCreatedAt()));
            user.setUpdatedAt(LocalDateTime.now());
            preparedStatement.setTimestamp(10, Timestamp.valueOf(user.getUpdatedAt()));
            preparedStatement.setBoolean(11, user.isVerified());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    // Update user
    public boolean updateUser(User user, boolean updatePassword) throws SQLException {
        // Fetch existing user data
        User existingUser = selectUser(user.getId());
        if (existingUser == null) {
            throw new SQLException("User not found with ID: " + user.getId());
        }

        // Check if a new Password is provided and should be updated
        String hashedPassword = updatePassword ? BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) : existingUser.getPassword();
        if (emailExists(user.getEmail())) {
            throw new SQLException("Email already registered.");
        }
        boolean rowUpdated;
        try (Connection connection = ConnectionSql.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            String rolesJson = new Gson().toJson(user.getRoles());
            statement.setString(4, rolesJson);
            statement.setInt(5, user.getNumTele());
            statement.setString(6, hashedPassword);
            statement.setString(7, user.getAdresse());
            statement.setString(8, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }


    // Delete user
    public static boolean deleteUser(String id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = ConnectionSql.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setString(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Select user by id
    public static User selectUser(String id) {
        User user = null;

        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setString(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String roleJson = rs.getString("roles");
                String[] roles = new Gson().fromJson(roleJson, String[].class);
                int numTele = rs.getInt("num_tele");
                String Password = rs.getString("Password");
                String adresse = rs.getString("adresse");
                String avatar = rs.getString("avatar");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                boolean isVerified = rs.getBoolean("is_verified");

                user = new User(id, nom, prenom, email, roles, numTele, Password, adresse, avatar, createdAt, updatedAt, isVerified);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }


    // Select all users
    public static List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                //String role = rs.getString("role");
                String roleJson = rs.getString("roles");
                String[] roles = new Gson().fromJson(roleJson, new TypeToken<String[]>(){}.getType());
                int numTele = rs.getInt("num_tele");
                String Password = rs.getString("Password");
                String adresse = rs.getString("adresse");
                String avatar = rs.getString("avatar");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                boolean isVerified = rs.getBoolean("is_verified");

                User user = new User(id, nom, prenom, email, roles, numTele, Password, adresse, avatar, createdAt, updatedAt, isVerified);
                users.add(user);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }


    public boolean emailExists(String email) {
        boolean exists = false;
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM user WHERE email = ?")) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exists = resultSet.getInt("count") > 0;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return exists;
    }


    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public User findUserByEmail(String email) {
        User user = null;
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
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
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }


    public boolean changePasswordByEmail(String email, String newPassword) {
        boolean PasswordChanged = false;
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET Password = ? WHERE email = ?")) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            PasswordChanged = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return PasswordChanged;
    }

    public void setUserDetails(User user) {
        updateEmailText.setText(user.getEmail());
        updateFirstNameText.setText(user.getNom());
        updateLastNameText.setText(user.getPrenom());
        updatePhoneText.setText(String.valueOf(user.getNumTele()));
        updateAdressText.setText(user.getAdresse());
        this.currentUser = user;
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
        String[] roles = currentUser.getRoles();
        String userId = currentUser.getId();
        User currentUser = selectUser(userId);

        // Updated to use current values for new attributes
        User userToUpdate = new User(
                currentUser.getId(),
                firstName,
                lastName,
                email,
                roles,
                Integer.parseInt(phone),
                currentUser.getPassword(),
                address,
                currentUser.getAvatar(),
                currentUser.getCreatedAt(),
                LocalDateTime.now(),
                currentUser.isVerified()
        );

        try {
            if (updateUser(userToUpdate, false)) {
                System.out.println("User updated successfully.");
                navigateToUserList();
            } else {
                System.out.println("Failed to update user.");
            }
        } catch (SQLException ex) {
            System.out.println("Error updating user: " + ex.getMessage());
            ex.printStackTrace();
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

    private void navigateToUserList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCrud/userList.fxml"));
            Parent rootView = loader.load();

            Stage currentStage = (Stage) updateEmailText.getScene().getWindow(); // Assuming updateEmailText is part of the current scene
            currentStage.setScene(new Scene(rootView));

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load user list view: " + e.getMessage());
        }
    }





    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }




}
