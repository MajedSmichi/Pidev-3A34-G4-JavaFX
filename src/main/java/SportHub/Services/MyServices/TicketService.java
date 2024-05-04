package SportHub.Services.MyServices;

import connectionSql.Myconnection.ConnectionSql;
import SportHub.Entity.MyEntity.Ticket;
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
}
