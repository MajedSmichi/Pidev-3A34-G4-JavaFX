package Gestionreclamation.Controller;

import Gestionreclamation.Entity.User;
import Gestionreclamation.SportHub.Controller.EvenementFront;
import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import Gestionreclamation.SportHub.Services.EvenementService;
import Gestionreclamation.SportHub.Services.TicketService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class client {


    @FXML
    private AnchorPane achorfront;
    @FXML
    private ImageView logoutButton;


    public AnchorPane getAnchor() {
        return achorfront;
    }


    public void loadSalleCards() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/FrontSalle.fxml"));
            BorderPane salleGrid = loader.load();
            achorfront.getChildren().setAll(salleGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadActivite() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/FrontActivite.fxml"));
            AnchorPane activiteGrid = loader.load();
            achorfront.getChildren().setAll(activiteGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    void logout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }




    @FXML
    public void loadUpdateClient() {
        try {
            System.out.println("current user email: " + SessionManager.getInstance().getUserEmail());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/UserCrud/updateClient.fxml"));
            AnchorPane updateClientView = loader.load();

            // Get the UserController and call handleUpdateAction1
            UserController userController = loader.getController();
            //userController.handleUpdateAction1();

            // Get the current user and show the user's data in the form
            User currentUser = SessionManager.getInstance().getCurrentUser();
            userController.showEditUser1(currentUser);

            achorfront.getChildren().setAll(updateClientView);
            System.out.println("Update client view loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the update client view");
        }
    }


    @FXML
    public void reclamation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/AjouterReclamation.fxml"));
            Pane reclamtion = fxmlLoader.load(); // Load as Pane
            achorfront.getChildren().setAll(reclamtion);
            System.out.println("reclmation front view loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the evenement front view");
        }
    }

    private AfficherReclamtion afficherReclamtion;

    // ...

    public void listeReclamtion() {
        afficherReclamtion = new AfficherReclamtion();
        afficherReclamtion.createCards();
        achorfront.getChildren().setAll(afficherReclamtion.getVbox());
    }



    ///////////////////////////////////////////////////GESTION EVENEMENTS////////////////////////////////////////////

    @FXML
    private Button mes_tickets;

    @FXML
    private Label list_event;


    @FXML
    private ScrollPane most_popular_events;

    @FXML
    public void loadEvenementFront() {
        try {
            AnchorPane evenementFront = FXMLLoader.load(getClass().getResource("/Gestionreclamation/SportHub/EvenementFront.fxml"));
            evenementFront.getStyleClass().add("center-content"); // Add the CSS class
            achorfront.getChildren().setAll(evenementFront);
            System.out.println("Evenement front view loaded successfully");
            mes_tickets.setVisible(true);
            list_event.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the evenement front view");
        }
    }





    @FXML
    public void loadUserTickets() {
        try {
            // Assuming the user ID is "1" for testing purposes
            User user= SessionManager.getInstance().getCurrentUser();
            String userId = user.getId();
            TicketService ticketService = new TicketService();
            List<Ticket> tickets = ticketService.getUserTickets(userId);

            // Clear the event container
            achorfront.getChildren().clear();

            if (tickets.isEmpty()) {
                // If there are no tickets, display a message
                Label messageLabel = new Label("Aucun tickets achete");
                achorfront.getChildren().add(messageLabel);
            } else {
                // If there are tickets, display them
                EvenementFront evenementFront = new EvenementFront();
                GridPane gridPane = new GridPane();

                gridPane.setHgap(10); // Horizontal gap between columns
                gridPane.setVgap(10); // Vertical gap between rows

                int column = 0;
                int row = 0;

                for (Ticket ticket : tickets) {
                    EvenementService evenementService = new EvenementService();
                    Evenement event = evenementService.getEventById(ticket.getEvenementId());
                    GridPane ticketCard = evenementFront.createTicketCard(event);
                    gridPane.add(ticketCard, column, row);
                    ticketCard.getStyleClass().add("mes_tickets"); // Add the new style class


                    column++;
                    if (column > 3) {
                        column = 0;
                        row++;
                    }
                }

                // Add the GridPane to the AnchorPane
                achorfront.getChildren().add(gridPane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void loadEvenementAndMostPopularEvents() {
        loadEvenementFront();

        // Fetch the most popular events
        TicketService ticketService = new TicketService();
        List<Evenement> popularEvents = null;
        try {
            popularEvents = ticketService.getMostPopularEvents();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        HBox hbox = new HBox(); // Use HBox instead of VBox
        hbox.setSpacing(10); // Set the space between the cards

        for (Evenement popularEvent : popularEvents) {
            GridPane popularEventCard = createPopularEventCard(popularEvent);
            hbox.getChildren().add(popularEventCard);
        }

        // Add the HBox to the ScrollPane
        most_popular_events.setContent(hbox);
    }

    public GridPane createPopularEventCard(Evenement event) {
        GridPane popularEventCard = new GridPane();
        popularEventCard.getStyleClass().add("popular-event-card"); // Add the style class



        // Create an ImageView for the event image
        ImageView eventImage = new ImageView(new Image(event.getImageEvenement()));
        eventImage.setFitWidth(100); // Adjust the width as needed
        eventImage.setFitHeight(100); // Adjust the height as needed

        // Create a Label for the event name
        Label eventName = new Label(event.getNom());
        eventName.getStyleClass().add("event-name"); // Add a CSS class for styling

        // Create a Label for the event date
        Label eventDate = new Label(event.getDateEvenement().toString());
        eventDate.getStyleClass().add("event-date"); // Add a CSS class for styling

        // Add the ImageView and Labels to the GridPane
        popularEventCard.add(eventImage, 0, 0, 1, 2); // Span 2 rows
        popularEventCard.add(eventName, 1, 0); // Top right
        popularEventCard.add(eventDate, 1, 1); // Bottom right

        return popularEventCard;
    }


    ///////////////////////////////////////////////////GESTION EVENEMENTS////////////////////////////////////////////


}
