package Controller;

import Entity.User;
import connectionSql.ConnectionSql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {
    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (nom, prenom, email, role, numTele, motDePass, adresse) VALUES " + " (?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "select id,nom,prenom,email,role,numTele,motDePass,adresse from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set nom = ?,prenom= ?, email =?, role =?, numTele =?, motDePass =?, adresse =? where id = ?;";



    // Create or insert user
    public void insertUser(User user) throws SQLException {
        try (Connection connection = ConnectionSql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            String hashedPassword = BCrypt.hashpw(user.getMotDePass(), BCrypt.gensalt());
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setInt(5, user.getNumTele());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.setString(7, user.getAdresse());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
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
}
