package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import Gestionreclamation.SportHub.Services.EvenementService;
import Gestionreclamation.SportHub.Services.TicketService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;



import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TicketController {

    @FXML
    private ComboBox<String> ticket_evenement;

    @FXML
    private TextField ticket_prix;

    @FXML
    private TextField ticket_type;

    @FXML
    private TextField ticket_nbre;

    @FXML
    private Button ticket_add;

    @FXML
    private Button ticket_delete;

    @FXML
    private Button ticket_update;

    @FXML
    private Button ticket_clear;


    @FXML
    private TableColumn<Ticket,String> col_ticket_evenement;

    @FXML
    private TableColumn<Ticket,String> col_ticket_nbre;

    @FXML
    private TableColumn<Ticket,String> col_ticket_prix;

    @FXML
    private TableColumn<Ticket,String> col_ticket_type;

    @FXML
    private TableView<Ticket> ticketTableView;

    private EvenementService evenementService;
    private TicketService ticketService;

    public TicketController() {
        evenementService = new EvenementService();
        ticketService = new TicketService();
    }

    @FXML
    public void initialize() throws SQLException {
        loadEvents();
        showListTicket();

        // Add a change listener to the TableView's selection model
        ticketTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected ticket
                Ticket selectedTicket = newValue;

                // Set the ticket details to the update fields
                ticket_prix.setText(String.valueOf(selectedTicket.getPrix()));
                ticket_type.setText(selectedTicket.getType());
                ticket_nbre.setText(String.valueOf(selectedTicket.getNbreTicket()));
                try {
                    ticket_evenement.setValue(evenementService.getEventNameById(selectedTicket.getEvenementId()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadEvents() throws SQLException {
        ObservableList<String> eventNames = FXCollections.observableArrayList(evenementService.getAllEventNames());
        ticket_evenement.setItems(eventNames);
    }

 /*   @FXML
    public void addTicket() {
        try {
            Alert alert;

            if (ticket_prix.getText().isEmpty() || ticket_type.getText().isEmpty() || ticket_nbre.getText().isEmpty() || ticket_evenement.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Les champs sont obligatoires");
                alert.showAndWait();
            } else {
                try {
                    int prix = Integer.parseInt(ticket_prix.getText());
                    int nbre = Integer.parseInt(ticket_nbre.getText());

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
                } catch (NumberFormatException e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Prix et Nombre de tickets sont des entiers");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    public void addTicket() {
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
                showListTicket();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*    public void showListTicket() throws SQLException {
        TicketService service = new TicketService();
        List<Ticket> tickets = service.getAllTickets();

        ObservableList<Ticket> ob = FXCollections.observableArrayList();
        for (Ticket ticket : tickets) {
            ob.add(ticket);
        }

        col_ticket_evenement.setCellValueFactory(new PropertyValueFactory<>("evenementId"));
        col_ticket_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_ticket_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        col_ticket_nbre.setCellValueFactory(new PropertyValueFactory<>("nbreTicket"));
        ticketTableView.setItems(ob);
    }*/


    public void showListTicket() throws SQLException {
        TicketService ticketService = new TicketService();
        EvenementService evenementService = new EvenementService();
        List<Ticket> tickets = ticketService.getAllTickets();

        ObservableList<Ticket> ob = FXCollections.observableArrayList();
        for (Ticket ticket : tickets) {
            ob.add(ticket);
        }

        col_ticket_evenement.setCellValueFactory(cellData -> {
            int eventId = cellData.getValue().getEvenementId();
            String eventName = null;
            try {
                eventName = evenementService.getEventNameById(eventId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(eventName);
        });
        col_ticket_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_ticket_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        col_ticket_nbre.setCellValueFactory(new PropertyValueFactory<>("nbreTicket"));
        ticketTableView.setItems(ob);
    }


    @FXML
    public void ticketDelete() {
        // Get the selected ticket
        Ticket selectedTicket = ticketTableView.getSelectionModel().getSelectedItem();

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
                showListTicket();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void ticketUpdate() {
        // Get the selected ticket
        Ticket selectedTicket = ticketTableView.getSelectionModel().getSelectedItem();
        int num = ticketTableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) { return; }

        try {
            Alert alert;

            if (ticket_prix.getText().isEmpty() || ticket_type.getText().isEmpty() || ticket_nbre.getText().isEmpty() || ticket_evenement.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Les champs sont obligatoires");
                alert.showAndWait();
            } else {
                // Get the updated values from the input fields
                String selectedEventName = ticket_evenement.getSelectionModel().getSelectedItem();
                Evenement selectedEvent = evenementService.getEventByName(selectedEventName);
                int prix = Integer.parseInt(ticket_prix.getText());
                String type = ticket_type.getText();
                int nbre = Integer.parseInt(ticket_nbre.getText());

                // Update the ticket
                ticketService.updateTicket(selectedTicket.getId(), selectedEvent.getId(), prix, type, nbre);

                // Refresh the table view
                showListTicket();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ticketClear() {
        ticket_evenement.getSelectionModel().clearSelection();
        ticket_prix.clear();
        ticket_type.clear();
        ticket_nbre.clear();
    }

}