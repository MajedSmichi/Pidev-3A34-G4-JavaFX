package GestionSalle.Services;

import GestionSalle.Entity.Activite;
import GestionSalle.Entity.Salle;
import connectionSql.ConnectionSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalleService {
    Connection connection;

    public SalleService() {
        connection = ConnectionSql.getInstance().getConnection();
    }
    public List<Salle> getAllSalle() throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String query = "SELECT * FROM salle";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Salle salle = new Salle();
            salle.setId(resultSet.getInt("id"));
            salle.setNom(resultSet.getString("nom"));
            salle.setAddresse(resultSet.getString("addresse"));
            salle.setNum_tel(resultSet.getInt("num_tel"));
            salle.setCapacite(resultSet.getInt("capacite"));
            salle.setDescription(resultSet.getString("description"));
            salle.setNbr_client(resultSet.getInt("nbr_client"));
            salle.setLogo_salle(resultSet.getString("logo_salle"));
            salles.add(salle);
        }
        return salles;
    }
    public void addEvent(Salle salle) throws SQLException {
        String query = "INSERT INTO salle (nom, addresse, num_tel, capacite, description, nbr_client, logo_salle) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, salle.getNom());
        preparedStatement.setString(2, salle.getAddresse());
        preparedStatement.setInt(3, salle.getNum_tel());
        preparedStatement.setInt(4, salle.getCapacite());
        preparedStatement.setString(5, salle.getDescription());
        preparedStatement.setInt(6, salle.getNbr_client());
        preparedStatement.setString(7, salle.getLogo_salle());
        preparedStatement.executeUpdate();
    }

    public void deleteSalle(int id) throws SQLException {
        // Create an instance of ActiviteService
        ActiviteService activiteService = new ActiviteService();

        // Get all activities associated with the salle
        List<Activite> activites = activiteService.getActiviteBySalle(id);

        // Delete all activities
        for (Activite activite : activites) {
            activiteService.deleteActiviteUser(activite.getId(), id);
        }

        // Delete the salle
        String query = "DELETE FROM salle WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
    public void updateSalle(int id, String nom, String addresse, int num_tel , int capacite, String description, int nbr_client, String logo_salle ) throws SQLException {
        String query = "UPDATE salle SET nom = ?, addresse = ?, num_tel = ?, capacite = ?, description= ?, nbr_client = ?, logo_salle = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);


        preparedStatement.setString(1, nom);
        preparedStatement.setString(2, addresse);
        preparedStatement.setInt(3, num_tel);
        preparedStatement.setInt(4, capacite);
        preparedStatement.setString(5, description);
        preparedStatement.setInt(6, nbr_client);
        preparedStatement.setString(7, logo_salle);
        preparedStatement.setInt(8, id);

        preparedStatement.executeUpdate();
    }
    public Salle getSalleById(int id) throws SQLException {
        String query = "SELECT * FROM salle WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Salle salle = new Salle();
            salle.setId(resultSet.getInt("id"));
            salle.setNom(resultSet.getString("nom"));
            // Set other properties...
            return salle;
        }
        return null;
    }
    public Salle getSalleByName(String name) throws SQLException {
        String query = "SELECT * FROM salle WHERE nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Salle salle = new Salle();
            salle.setId(resultSet.getInt("id"));
            salle.setNom(resultSet.getString("nom"));
            salle.setAddresse(resultSet.getString("addresse"));
            salle.setNum_tel(resultSet.getInt("num_tel"));
            salle.setCapacite(resultSet.getInt("capacite"));
            salle.setDescription(resultSet.getString("description"));
            salle.setNbr_client(resultSet.getInt("nbr_client"));
            salle.setLogo_salle(resultSet.getString("logo_salle"));
            return salle;
        }
        return null;
    }
    public boolean isUnique(String nom, String addresse) {
    String query = "SELECT * FROM salle WHERE nom = ? AND addresse = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nom);
        preparedStatement.setString(2, addresse);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            // A Salle with the same nom and addresse already exists
            return false;
        } else {
            // No Salle with the same nom and addresse exists
            return true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
