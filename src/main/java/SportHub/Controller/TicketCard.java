package SportHub.Controller;

import SportHub.Entity.MyEntity.Evenement;
import SportHub.Entity.MyEntity.Ticket;
import SportHub.Services.MyServices.EvenementService;
import SportHub.Services.MyServices.TicketService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class TicketCard {

    @FXML
    private Label Date;

    @FXML
    private Label Nbre_tickets;

    @FXML
    private Pane PaneTicket;

    @FXML
    private Label Prix;

    @FXML
    private Label Type;

    @FXML
    private Label name;

    private Evenement event;
    private Ticket ticket;
    private TicketService ticketService = new TicketService();
    private EvenementService evenementService = new EvenementService();


public void setData(Ticket ticket, String eventName) {
    this.ticket = ticket;
    try {
        this.ticket = ticketService.getTicketByEventId(ticket.getEvenementId());
        this.event = evenementService.getEventById(ticket.getEvenementId());
    } catch (SQLException e) {
        e.printStackTrace();
    }
    name.setText(eventName);
    Date.setText(event.getDateEvenement().toString());
    Nbre_tickets.setText(Integer.toString(ticket.getNbreTicket()));
    Type.setText(ticket.getType());
    Prix.setText(Integer.toString(ticket.getPrix()));
}
}