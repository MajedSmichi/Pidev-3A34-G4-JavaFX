package SportHub.Services;

import SportHub.Entity.Evenement;
import connectionSql.ConnectionSql;
import SportHub.Entity.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class TicketService {

    Connection connection;

    public TicketService() {
        connection = ConnectionSql.getInstance().getConnection();
    }

    public void addTicket(Ticket ticket) throws SQLException {
        String query = "INSERT INTO ticket ( evenement_id,prix, type, nbre_ticket) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ticket.getEvenementId());
        preparedStatement.setInt(2, ticket.getPrix());
        preparedStatement.setString(3, ticket.getType());
        preparedStatement.setInt(4, ticket.getNbreTicket());
        preparedStatement.executeUpdate();
    }

    public void deleteTicket(int id) throws SQLException {
        String query = "DELETE FROM ticket WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public void updateTicket(int id, Integer evenement_id, Integer prix, String type, Integer nbre_ticket) throws SQLException {
        String query = "UPDATE ticket SET evenement_id = ?, prix = ?, type = ?, nbre_ticket = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, evenement_id);
        preparedStatement.setInt(2, prix);
        preparedStatement.setString(3, type);
        preparedStatement.setInt(4, nbre_ticket);
        preparedStatement.setInt(5, id);
        preparedStatement.executeUpdate();
    }

    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM ticket";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setId(resultSet.getInt("id"));
            ticket.setEvenementId(resultSet.getInt("evenement_id"));
            ticket.setPrix(resultSet.getInt("prix"));
            ticket.setType(resultSet.getString("type"));
            ticket.setNbreTicket(resultSet.getInt("nbre_ticket"));
            tickets.add(ticket);
        }
        return tickets;
    }


    public Ticket getTicketByEventId(int eventId) throws SQLException {
        String query = "SELECT * FROM ticket WHERE evenement_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setId(resultSet.getInt("id"));
            ticket.setEvenementId(resultSet.getInt("evenement_id"));
            ticket.setPrix(resultSet.getInt("prix"));
            ticket.setType(resultSet.getString("type"));
            ticket.setNbreTicket(resultSet.getInt("nbre_ticket"));
            return ticket;
        }
        return null;
    }

public Ticket testTicketByEventId(int eventId, int currentTicketId) throws SQLException {
    String query = "SELECT * FROM ticket WHERE evenement_id = ? AND id != ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, eventId);
    preparedStatement.setInt(2, currentTicketId);
    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getInt("id"));
        ticket.setEvenementId(resultSet.getInt("evenement_id"));
        ticket.setPrix(resultSet.getInt("prix"));
        ticket.setType(resultSet.getString("type"));
        ticket.setNbreTicket(resultSet.getInt("nbre_ticket"));
        return ticket;
    }
    return null;
}
/*public boolean ticketExistsForEvent(int eventId) throws SQLException {
    String query = "SELECT * FROM ticket WHERE evenement_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, eventId);
    ResultSet resultSet = preparedStatement.executeQuery();
    return resultSet.next();
}*/



public void registerUserTicket(int ticketId, String userId) throws SQLException {
    String query = "INSERT INTO ticket_user (ticket_id, user_id) VALUES (?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, ticketId);
    preparedStatement.setString(2, userId);
    preparedStatement.executeUpdate();
}


public List<Ticket> getUserTickets(String userId) throws SQLException {
    List<Ticket> tickets = new ArrayList<>();
    String query = "SELECT * FROM ticket_user JOIN ticket ON ticket_user.ticket_id = ticket.id WHERE user_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setString(1, userId);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getInt("id"));
        ticket.setEvenementId(resultSet.getInt("evenement_id"));
        ticket.setPrix(resultSet.getInt("prix"));
        ticket.setType(resultSet.getString("type"));
        ticket.setNbreTicket(resultSet.getInt("nbre_ticket"));
        tickets.add(ticket);
    }
    return tickets;
}


public List<Evenement> getMostPopularEvents() throws SQLException {
    // Create a Statement object
    Statement statement = connection.createStatement();

    // Create an EvenementService object
    EvenementService evenementService = new EvenementService();

    // Query to get the 4 events with the most unique users
    String query = "SELECT ticket.evenement_id, COUNT(DISTINCT ticket_user.user_id) as user_count " +
                   "FROM ticket_user " +
                   "JOIN ticket ON ticket_user.ticket_id = ticket.id " +
                   "GROUP BY ticket.evenement_id " +
                   "ORDER BY user_count DESC LIMIT 4";

    // Execute the query and get the results
    ResultSet rs = statement.executeQuery(query);

    // Create a list to store the most popular events
    List<Evenement> mostPopularEvents = new ArrayList<>();

    // For each result, get the event and add it to the list
    while (rs.next()) {
        int eventId = rs.getInt("evenement_id");
        Evenement event = evenementService.getEventById(eventId);
        mostPopularEvents.add(event);
    }

    return mostPopularEvents;
}

public int getTotalPurchasedTicketsByEventId(int eventId) throws SQLException {
    String query = "SELECT COUNT(DISTINCT user_id) as total_tickets FROM ticket_user JOIN ticket ON ticket_user.ticket_id = ticket.id WHERE ticket.evenement_id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    preparedStatement.setInt(1, eventId);
    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
        return resultSet.getInt("total_tickets");
    }
    return 0;
}
}
