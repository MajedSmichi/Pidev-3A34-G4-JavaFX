package Gestionreclamation.SportHub.Services;

import Gestionreclamation.SportHub.Entity.User;
import connectionSql.ConnectionSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private Connection connection;

    public UserService() {
        this.connection = ConnectionSql.getConnection();
    }

    public User getUserById(String userId) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setNom(resultSet.getString("username"));
            // Set other user properties...
            return user;
        }

        return null;
    }
}