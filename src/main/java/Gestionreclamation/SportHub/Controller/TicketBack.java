package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import Gestionreclamation.SportHub.Services.EvenementService;
import Gestionreclamation.SportHub.Services.TicketService;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    private AnchorPane ajouterPane;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    void addTicket(MouseEvent event) {

    }

    @FXML
    void ticketClear(MouseEvent event) {

    }


    private EvenementService evenementService;
    private TicketService ticketService;
    private int currentTicketId;

    // Add these getter methods to your TicketBack class
    public AnchorPane getAjouterPane() {
        return ajouterPane;
    }

    public TextField getTicket_prix() {
        return ticket_prix;
    }

    public TextField getTicket_type() {
        return ticket_type;
    }

    public TextField getTicket_nbre() {
        return ticket_nbre;
    }

    public ComboBox<String> getTicket_evenement() {
        return ticket_evenement;
    }

    public TicketBack() {
        evenementService = new EvenementService();
        ticketService = new TicketService();
    }

    private Ticket selectedTicket;

    public void setSelectedTicket(Ticket ticket) {
        this.selectedTicket = ticket;
    }

    @FXML
    public void initialize() {

        // Bind the visibility of the hide button to the visibility of ajouterPane
        hide.visibleProperty().bind(ajouterPane.visibleProperty());

        ajouterPane.setVisible(false); // Make ajouterPane not visible

        ajouterTicket.setOnAction(e -> {
            ajouterPane.setVisible(true);
        });
        hide.setOnAction(e -> {
            ajouterPane.setVisible(false);
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchTicketsByEventName();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        loadEvents();
        // Get all tickets
        List<Ticket> tickets = null;
        try {
            tickets = ticketService.getAllTickets();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Call showListTicket method
        if (tickets != null) {
            showListTicket(tickets);
        }

        // Add sorting options to the sortComboBox
sortComboBox.getItems().addAll("Sort by Price", "Sort by Event Name");
        sortComboBox.setPromptText("Sort");
// Add a listener to the sortComboBox
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
switch ((String) newValue) {
    case "Sort by Price":
        sortTicketsByPrice();
        break;
    case "Sort by Event Name":
        sortTicketsByEventName();
        break;
}
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });
    }


    private void sortTicketsByPrice() throws SQLException, IOException {
        List<Ticket> tickets = ticketService.getAllTickets();
        tickets.sort(Comparator.comparing(Ticket::getPrix)); // Sort tickets by price
        showListTicket(tickets);
    }

    private void sortTicketsByEventName() throws SQLException, IOException {
        List<Ticket> tickets = ticketService.getAllTickets();
        // Sort tickets by event name
        tickets.sort(Comparator.comparing(ticket -> {
            try {
                return evenementService.getEventById(ticket.getEvenementId()).getNom();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }));
        showListTicket(tickets);
    }
@FXML
private void searchTicketsByEventName() throws SQLException, IOException {
    String searchTerm = searchField.getText();
    List<Ticket> tickets = ticketService.searchTicketsByEventName(searchTerm);
    ticketContainerBack.getChildren().clear(); // Clear the GridPane
    if (tickets.isEmpty()) {
        Label label = new Label("Aucun ticket ne correspond à votre recherche");
        label.getStyleClass().add("error-message"); // Add a style class to style the error message
        ticketContainerBack.add(label, 0, 0);
    } else {
        showListTicket(tickets);
    }
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
            Ticket existingTicket = ticketService.getTicketByEventId(selectedEvent.getId());

            if (existingTicket != null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Un ticket est déjà créé avec cet événement");
                alert.showAndWait();
            } else {
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
               List<Ticket> tickets = ticketService.getAllTickets();
showListTicket(tickets);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void showListTicket(List<Ticket> tickets) {
        try {
            // Clear the container before repopulating it
            ticketContainerBack.getChildren().clear();

            for (int i = 0; i < tickets.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Gestionreclamation/SportHub/TicketCard.fxml"));
                Pane pane = fxmlLoader.load();

                TicketCard controller = fxmlLoader.getController();
                controller.setData(tickets.get(i), evenementService.getEventNameById(tickets.get(i).getEvenementId()));
                controller.setTicketBackController(this);

                // Add the pane to the GridPane at the specified column and row
                ticketContainerBack.add(pane, i % 3, i / 3);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

public void populateFields(Ticket ticket) {
    ajouterPane.setVisible(true);
    ticket_prix.setText(String.valueOf(ticket.getPrix()));
    ticket_type.setText(ticket.getType());
    ticket_nbre.setText(String.valueOf(ticket.getNbreTicket()));
    try {
        ticket_evenement.setValue(evenementService.getEventNameById(ticket.getEvenementId()));
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    // Store the id of the ticket to be updated
    currentTicketId = ticket.getId();
}


@FXML
public void ticketUpdate() {
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
            // Get the updated values from the input fields
            String selectedEventName = ticket_evenement.getSelectionModel().getSelectedItem();
            String type = ticket_type.getText();

            // Get the selected event
            Evenement selectedEvent = evenementService.getEventByName(selectedEventName);

            // Check if a ticket with the same event already exists, excluding the current event of the modifying ticket
            Ticket existingTicket = ticketService.testTicketByEventId(selectedEvent.getId(), currentTicketId);
            if (existingTicket != null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("A ticket with the selected event already exists.");
                alert.showAndWait();
                return;
            }

            // Update the ticket
            ticketService.updateTicket(currentTicketId, selectedEvent.getId(), prix, type, nbre);

            // Refresh the table view
       List<Ticket> tickets = ticketService.getAllTickets();
showListTicket(tickets);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the exception appropriately, e.g., show an error message to the user
    }
}


    public void deleteTicket() {
        // Check if a ticket is selected
        if (selectedTicket == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a ticket in the table.");
            alert.showAndWait();
            return;
        }

        // Show a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Ticket");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this ticket?");
        Optional<ButtonType> result = alert.showAndWait();

        // If the user confirmed the deletion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete the ticket
                ticketService.deleteTicket(selectedTicket.getId());

                // Refresh the table view
           List<Ticket> tickets = ticketService.getAllTickets();
showListTicket(tickets);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void clear() {
        ticket_evenement.getSelectionModel().clearSelection();
        ticket_prix.clear();
        ticket_type.clear();
        ticket_nbre.clear();
    }

}