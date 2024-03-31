package Controller;

import Entity.User;
import connectionSql.ConnectionSql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {
    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (nom, prenom, email, role, numTele, motDePass, adresse) VALUES " + " (?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "select id,nom,prenom,email,role,numTele,motDePass,adresse from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set nom = ?,prenom= ?, email =?, role =?, numTele =?, motDePass =?, adresse =? where id = ?;";
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
    private TableView<User> userTableView=new TableView<>(); // Assuming User is your model class

    @FXML
    private TableColumn<User, Void> actionColumn;


    @FXML
    private TableColumn<User, String> firstname;
    @FXML
    private TableColumn<User, String> lastName;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, String> phone;
    @FXML
    private TableColumn<User, String> adress;

    public void initialize() {
        // Assuming User class uses standard naming for getters
        firstname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        // Adjust according to your User class field names
        lastName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumTele())));
        adress.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        setupActionColumn();
        loadUsersIntoTable();
    }


    // Create or insert user
    public void insertUser(User user) throws SQLException {
        // First, check if the email already exists
        if (emailExists(user.getEmail())) {
            throw new SQLException("Email already registered.");
        }

        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            String hashedPassword = BCrypt.hashpw(user.getMotDePass(), BCrypt.gensalt());
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, "CLIENT_ROLE"); // Set the role to CLIENT_ROLE by default
            preparedStatement.setInt(5, user.getNumTele());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.setString(7, user.getAdresse());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Re-throw the exception for the caller to handle
            throw e;
        }
    }


    // Update user
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = ConnectionSql.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            String hashedPassword = BCrypt.hashpw(user.getMotDePass(), BCrypt.gensalt());
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole());
            statement.setInt(5, user.getNumTele());
            statement.setString(6,hashedPassword);
            statement.setString(7, user.getAdresse());
            statement.setInt(8, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Delete user
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = ConnectionSql.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Select user by id
    public static User selectUser(int id) {
        User user = null;

        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);


            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String role = rs.getString("role");
                int numTele = rs.getInt("numTele");
                String motDePass = rs.getString("motDePass");
                String adresse = rs.getString("adresse");
                user = new User(id, nom, prenom, email, role, numTele, motDePass, adresse);
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


             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {

            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String role = rs.getString("role");
                int numTele = rs.getInt("numTele");
                String motDePass = rs.getString("motDePass");
                String adresse = rs.getString("adresse");
                users.add(new User(id, nom, prenom, email, role, numTele, motDePass, adresse));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean emailExists(String email) {
        boolean exists = false;
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE email = ?")) {
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
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
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
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public boolean changePasswordByEmail(String email, String newPassword) {
        boolean passwordChanged = false;
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET motDePass = ? WHERE email = ?")) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            passwordChanged = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return passwordChanged;
    }


    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox container = new HBox(5, editButton, deleteButton); // 5 is the spacing between buttons

            {
                // Edit button action
                editButton.setOnAction(event -> {
                    User currentUser = getTableView().getItems().get(getIndex());
                    // Handle Edit Action Here
                    System.out.println("Edit button clicked for " + currentUser);
                });

                // Delete button action
                deleteButton.setOnAction(event -> {
                    User currentUser = getTableView().getItems().get(getIndex());
                    try {
                        // Call deleteUser method to delete the user
                        boolean deleted = deleteUser(currentUser.getId());
                        if (deleted) {
                            // If deletion was successful, refresh the table
                            getTableView().getItems().remove(currentUser);
                            System.out.println("User deleted successfully.");
                        } else {
                            System.out.println("Failed to delete the user.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error occurred while deleting the user: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }


    private void loadUsersIntoTable() {
        List<User> userList = selectAllUsers(); // Assuming this method fetches your users
        userTableView.setItems(FXCollections.observableArrayList(userList));
    }
}
