package SportHub.Services;

import connectionSql.ConnectionSql;
import SportHub.Entity.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService {

    Connection connection;

    public EvenementService() {
        connection = ConnectionSql.getInstance().getConnection();
    }


    public void addEvent(Evenement event) throws SQLException {
        String query = "INSERT INTO evenement (nom, description, lieu, date_evenement, image_evenement) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, event.getNom());
        preparedStatement.setString(2, event.getDescription());
        preparedStatement.setString(3, event.getLieu());
        preparedStatement.setDate(4, event.getDateEvenement());
        preparedStatement.setString(5, event.getImageEvenement());
        preparedStatement.executeUpdate();
    }

public void deleteEvent(int id) throws SQLException {
    // First, delete the tickets related to the event
    String deleteTicketsQuery = "DELETE FROM ticket WHERE evenement_id = ?";
    PreparedStatement deleteTicketsStmt = connection.prepareStatement(deleteTicketsQuery);
    deleteTicketsStmt.setInt(1, id);
    deleteTicketsStmt.executeUpdate();

    // Then, delete the event
    String deleteEventQuery = "DELETE FROM evenement WHERE id = ?";
    PreparedStatement deleteEventStmt = connection.prepareStatement(deleteEventQuery);
    deleteEventStmt.setInt(1, id);
    deleteEventStmt.executeUpdate();
}

/*
    public void updateEvent(int id, String nom, String description, String lieu, Date date_evenement, String image_evenement) throws SQLException {
        String query = "UPDATE evenement SET nom = ?, description = ?, lieu = ?, date_evenement = ?, image_evenement = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nom);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, lieu);
        preparedStatement.setDate(4, date_evenement);
        preparedStatement.setString(5, image_evenement);
        preparedStatement.setInt(6, id);
        preparedStatement.executeUpdate();
    }
*/

    public void updateEvent(int id, String nom, String description, String lieu, Date date_evenement, String image_evenement) throws SQLException {
    String query = "UPDATE evenement SET nom = ?, description = ?, lieu = ?, date_evenement = ?, image_evenement = ? WHERE id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, nom);
    preparedStatement.setString(2, description);
    preparedStatement.setString(3, lieu);
    preparedStatement.setDate(4, date_evenement);
    preparedStatement.setString(5, image_evenement);
    preparedStatement.setInt(6, id);

    System.out.println("Executing update query: " + preparedStatement.toString());

    int rowsUpdated = preparedStatement.executeUpdate();

    if (rowsUpdated > 0) {
        System.out.println("An existing event was updated successfully!");
    } else {
        System.out.println("No event was updated. Check if the event with the given ID exists.");
    }
}

    public List<Evenement> getAllEvents() throws SQLException {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Evenement event = new Evenement();
            event.setId(resultSet.getInt("id"));
            event.setNom(resultSet.getString("nom"));
            event.setDescription(resultSet.getString("description"));
            event.setLieu(resultSet.getString("lieu"));
            event.setDateEvenement(resultSet.getDate("date_evenement"));
            event.setImageEvenement(resultSet.getString("image_evenement"));
            events.add(event);
        }
        return events;
    }



    public List<String> getAllEventNames() throws SQLException {
        List<String> eventNames = new ArrayList<>();
        String query = "SELECT nom FROM evenement";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            eventNames.add(resultSet.getString("nom"));
        }
        return eventNames;
    }

    public Evenement getEventByName(String eventName) throws SQLException {
        String query = "SELECT * FROM evenement WHERE nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, eventName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Evenement event = new Evenement();
            event.setId(resultSet.getInt("id"));
            event.setNom(resultSet.getString("nom"));
            event.setDescription(resultSet.getString("description"));
            event.setLieu(resultSet.getString("lieu"));
            event.setDateEvenement(resultSet.getDate("date_evenement"));
            event.setImageEvenement(resultSet.getString("image_evenement"));
            return event;
        }
        return null;
    }

    public String getEventNameById(int id) throws SQLException {
        String query = "SELECT nom FROM evenement WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("nom");
        }
        return null;
    }



    public Evenement getEventById(int id) throws SQLException {
    String query = "SELECT * FROM evenement WHERE id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, id);
    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
        Evenement event = new Evenement();
        event.setId(resultSet.getInt("id"));
        event.setNom(resultSet.getString("nom"));
        event.setDescription(resultSet.getString("description"));
        event.setLieu(resultSet.getString("lieu"));
        event.setDateEvenement(resultSet.getDate("date_evenement"));
        event.setImageEvenement(resultSet.getString("image_evenement"));
        return event;
    }
    return null;
}



}