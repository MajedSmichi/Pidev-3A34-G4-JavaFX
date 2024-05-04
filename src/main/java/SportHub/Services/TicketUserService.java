package SportHub.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TicketUserService {
    private Connection connection;

    public TicketUserService(Connection connection) {
        this.connection = connection;
    }

    public void registerUserTicket(int userId, int ticketId) throws SQLException {
        String query = "INSERT INTO ticket_user (user_id, ticket_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, ticketId);
        preparedStatement.executeUpdate();
    }
}