package Gestionreclamation.Services;

import Gestionreclamation.Entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connectionSql.ConnectionSql;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserService {
    private static final String INSERT_USERS_SQL = "INSERT INTO user ( nom, prenom, email, roles, num_tele, Password, adresse, avatar, created_at, updated_at, is_verified) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,nom,prenom,email,roles,num_tele,Password,adresse,avatar,created_at,updated_at,is_verified from user where id =?";
    private static final String SELECT_ALL_USERS = "select * from user";
    private static final String DELETE_USERS_SQL = "delete from user where id = ?;";
    private static final String UPDATE_USERS_SQL = "update user set nom = ?, prenom= ?, email =?, roles =?, num_tele =?, Password =?, adresse =?, updated_at = ? where id = ?;";

    public static void insertUser(User user) throws SQLException {
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
// Update user
    public boolean updateUser(User user, boolean updatePassword) throws SQLException {
        // Fetch existing user data
        User existingUser = selectUser(user.getId());
        if (existingUser == null) {
            throw new SQLException("User not found with ID: " + user.getId());
        }

        // Check if a new password is provided and should be updated
        String hashedPassword = updatePassword ? BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) : existingUser.getPassword();

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
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(9, user.getId());

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
            String roleJson = rs.getString("roles");
            String[] roles = new Gson().fromJson(roleJson, new TypeToken<String[]>(){}.getType());

            // Check if the user has the role "ROLE_CLIENT"
            if (Arrays.asList(roles).contains("ROLE_CLIENT")) {
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
        }
    } catch (SQLException e) {
        printSQLException(e);
    }
    return users;
}

    public static boolean emailExists(String email) {
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

    public static User findUserByEmail(String email) {
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

    public static boolean updateUserActiveStatus(String userId, boolean isActive) {
        boolean rowUpdated = false;
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE user SET is_verified = ? WHERE id = ?")) {
            statement.setBoolean(1, isActive);
            statement.setString(2, userId);
            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rowUpdated;
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
}