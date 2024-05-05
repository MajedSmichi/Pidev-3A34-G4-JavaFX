package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import Gestionreclamation.SportHub.Services.EvenementService;
import Gestionreclamation.SportHub.Services.TicketService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.util.Optional;

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

    @FXML
    private Button modifier;

    @FXML
    private ImageView modifiericon1;

    @FXML
    private ImageView suppicon;

    @FXML
    private Button supprimer;

    private Evenement event;
    private Ticket ticket;
    private TicketService ticketService = new TicketService();
    private EvenementService evenementService = new EvenementService();

    private TicketBack ticketBackController;

    public void setTicketBackController(TicketBack ticketBackController) {
        this.ticketBackController = ticketBackController;
    }

    @FXML
    public void initialize() {
        supprimer.setOnAction(e -> deleteTicket());

        modifier.setOnAction(e -> {
            if (ticketBackController != null) {
                ticketBackController.populateFields(ticket);
            }
        });
    }

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

/*
public void deleteTicket() {
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirmation");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText("Vous êtes sûr de supprimer ce ticket?");

    Optional<ButtonType> result = confirmationAlert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
            ticketService.deleteTicket(ticket.getId());
            // Refresh the page
            PaneTicket.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Ticket supprimé avec succès");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

*/
@FXML
private void deleteTicket() {
    ticketBackController.setSelectedTicket(ticket);
    ticketBackController.deleteTicket();
}

}