package Gestionreclamation.Services.Salle;

import Gestionreclamation.Entity.Salle.Activite;
import Gestionreclamation.Entity.User;
import connectionSql.ConnectionSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActiviteService {
    Connection connection;

    public ActiviteService() {
        connection = ConnectionSql.getConnection();
    }

    public List<Activite> getAllActivite() throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String query = "SELECT * FROM activite";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Activite activite = new Activite();
            activite.setId(resultSet.getInt("id"));
            activite.setSalle_id(resultSet.getInt("salle_id"));
            activite.setNom(resultSet.getString("nom"));
            activite.setDate(resultSet.getTimestamp("date"));
            activite.setNbr_max(resultSet.getInt("nbr_max"));
            activite.setDescription(resultSet.getString("description"));
            activite.setImage(resultSet.getString("image_activte"));
            activite.setCoach(resultSet.getString("coach"));
            activites.add(activite);
        }
        return activites;
    }

    public void addActivite(Activite activite) throws SQLException {
        String query = "INSERT INTO activite (salle_id, nom, date, nbr_max, description, image_activte, coach) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activite.getSalle_id());
        preparedStatement.setString(2, activite.getNom());
        preparedStatement.setDate(3, new java.sql.Date(activite.getDate().getTime()));
        preparedStatement.setInt(4, activite.getNbr_max());
        preparedStatement.setString(5, activite.getDescription());
        preparedStatement.setString(6, activite.getImage());
        preparedStatement.setString(7, activite.getCoach());
        preparedStatement.executeUpdate();
    }

    public void deleteActivite(int id) throws SQLException {
        String query = "DELETE FROM activite WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public void updateActivite(Activite activite) throws SQLException {
        String query = "UPDATE activite SET salle_id = ?, nom = ?, date = ?, nbr_max = ?, description = ?, image_activte = ?, coach = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activite.getSalle_id());
        preparedStatement.setString(2, activite.getNom());
        preparedStatement.setDate(3, new java.sql.Date(activite.getDate().getTime()));
        preparedStatement.setInt(4, activite.getNbr_max());
        preparedStatement.setString(5, activite.getDescription());
        preparedStatement.setString(6, activite.getImage());
        preparedStatement.setString(7, activite.getCoach());
        preparedStatement.setInt(8, activite.getId());
        preparedStatement.executeUpdate();
    }
    public List<Activite> getActiviteBySalle(int salle_id) throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String query = "SELECT * FROM activite WHERE salle_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, salle_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Activite activite = new Activite();
            activite.setId(resultSet.getInt("id"));
            activite.setSalle_id(resultSet.getInt("salle_id"));
            activite.setNom(resultSet.getString("nom"));
            activite.setDate(resultSet.getTimestamp("date"));
            activite.setNbr_max(resultSet.getInt("nbr_max"));
            activite.setDescription(resultSet.getString("description"));
            activite.setImage(resultSet.getString("image_activte"));
            activite.setCoach(resultSet.getString("coach"));
            activites.add(activite);
        }
        return activites;
    }
    public void reserverActivite( int activiteId ,int userId) throws SQLException {
        String query = "INSERT INTO activite_user (activite_id, user_id) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, activiteId);
        stmt.setInt(2, userId);
        stmt.executeUpdate();

        String updateQuery = "UPDATE activite SET nbr_max = nbr_max - 1 WHERE id = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, activiteId);
        updateStmt.executeUpdate();
    }

    public List<User> getUsersByActiviteId(int activiteId) throws SQLException {
    List<User> users = new ArrayList<>();
    String query = "SELECT u.* FROM user u JOIN activite_user au ON u.id = au.user_id WHERE au.activite_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, activiteId);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
        User user = new User();
        // Assuming User class has id, name, and numTel fields.
        // Replace these with the actual fields in your User class.
        user.setId(resultSet.getInt("id"));
        user.setNom(resultSet.getString("nom"));
        user.setNumTele(resultSet.getInt("num_Tele"));
        users.add(user);
    }
    return users;
    }
    public Activite getActiviteById(int id) throws SQLException {
    String query = "SELECT * FROM activite WHERE id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, id);
    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
        Activite activite = new Activite();
        activite.setId(resultSet.getInt("id"));
        activite.setSalle_id(resultSet.getInt("salle_id"));
        activite.setNom(resultSet.getString("nom"));
        activite.setDate(resultSet.getTimestamp("date"));
        activite.setNbr_max(resultSet.getInt("nbr_max"));
        activite.setDescription(resultSet.getString("description"));
        activite.setImage(resultSet.getString("image_activte"));
        activite.setCoach(resultSet.getString("coach"));
        return activite;
    }
    return null;
}
    public List<Activite> getActivitesByUserId(int userId) throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String query = "SELECT * FROM activite_user WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int activiteId = resultSet.getInt("activite_id");
            Activite activite = getActiviteById(activiteId);
            if (activite != null) {
                activites.add(activite);
            }
        }
        return activites;
    }
    public void deleteActiviteUser(int activiteId, int userId) throws SQLException {
        String query = "DELETE FROM activite_user WHERE activite_id = ? AND user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activiteId);
        preparedStatement.setInt(2, userId);
        preparedStatement.executeUpdate();

        String updateQuery = "UPDATE activite SET nbr_max = nbr_max + 1 WHERE id = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, activiteId);
        updateStmt.executeUpdate();
    }

}