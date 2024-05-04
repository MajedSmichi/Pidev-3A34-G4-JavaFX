package SportHub.Controller;

import SportHub.Entity.MyEntity.Evenement;
import SportHub.Entity.MyEntity.Ticket;
import SportHub.Services.MyServices.EvenementService;
import SportHub.Services.MyServices.TicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TicketBack {

    @FXML
    private Button ajouterTicket;

    @FXML
    private Button hide;

    @FXML
    private ImageView modifiericon;

    @FXML
    private AnchorPane root3;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane ticketContainerBack;

    @FXML
    private Button ticket_add;

    @FXML
    private Button ticket_clear;

    @FXML
    private ComboBox<String> ticket_evenement;

    @FXML
    private TextField ticket_nbre;

    @FXML
    private TextField ticket_prix;

    @FXML
    private TextField ticket_type;

    @FXML
    void addTicket(MouseEvent event) {

    }

    @FXML
    void ticketClear(MouseEvent event) {

    }


    private EvenementService evenementService;
    private TicketService ticketService;


    public TicketBack() {
        evenementService = new EvenementService();
        ticketService = new TicketService();
    }

    @FXML
    public void initialize() {
        loadEvents();
        showListTicket();

    }

    private void loadEvents() {
        try {
            List<String> eventNames = evenementService.getAllEventNames();
            ObservableList<String> observableList = FXCollections.observableArrayList(eventNames);
            ticket_evenement.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to load events from the database.");
            alert.showAndWait();
        }
    }

    @FXML
    public void addTicket1() {
        try {
            Alert alert;
            int prix = 0;
            int nbre = 0;
            boolean isPrixInteger = true;
            boolean isNbreInteger = true;

            try {
                prix = Integer.parseInt(ticket_prix.getText());
            } catch (NumberFormatException e) {
                isPrixInteger = false;
            }

            try {
                nbre = Integer.parseInt(ticket_nbre.getText());
            } catch (NumberFormatException e) {
                isNbreInteger = false;
            }

            if (ticket_prix.getText().isEmpty() || ticket_type.getText().isEmpty() || ticket_nbre.getText().isEmpty() || ticket_evenement.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Les champs sont obligatoires");
                alert.showAndWait();
            } else if (!isPrixInteger && !isNbreInteger) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Prix et Nombre de tickets sont des entiers");
                alert.showAndWait();
            } else if (!isPrixInteger) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Prix doit être un entier");
                alert.showAndWait();
            } else if (!isNbreInteger) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Nombre de tickets doit être un entier");
                alert.showAndWait();
            } else if (prix <= 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Prix doit être supérieur à zéro");
                alert.showAndWait();
            } else if (nbre <= 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Nombre de tickets doit être supérieur à zéro");
                alert.showAndWait();
            } else {
                String selectedEventName = ticket_evenement.getSelectionModel().getSelectedItem();
                Evenement selectedEvent = evenementService.getEventByName(selectedEventName);
                String type = ticket_type.getText();

                Ticket ticket = new Ticket();
                ticket.setEvenementId(selectedEvent.getId());
                ticket.setPrix(prix);
                ticket.setType(type);
                ticket.setNbreTicket(nbre);

                ticketService.addTicket(ticket);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                // Refresh the table view
                //showListTicket();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




private void showListTicket() {
    ticketContainerBack.getChildren().clear();
    try {
        List<Ticket> tickets = ticketService.getAllTickets();
        for (Ticket ticket : tickets) {
            String eventName = evenementService.getEventNameById(ticket.getEvenementId());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/SportHub/TicketCard.fxml"));
            Pane pane = fxmlLoader.load();

            TicketCard controller = fxmlLoader.getController();
            controller.setData(ticket, eventName);

            ticketContainerBack.add(pane, ticket.getId() % 3, ticket.getId() / 3);
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
        System.out.printf("Error: %s%n", e.getMessage());
    }
}


}