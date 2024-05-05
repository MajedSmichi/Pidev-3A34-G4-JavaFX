package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.Controller.SessionManager;
import Gestionreclamation.Entity.User;
import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import Gestionreclamation.SportHub.Services.EvenementService;
import Gestionreclamation.SportHub.Services.TicketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontViewController {



    @FXML
    private AnchorPane achorfront;

    @FXML
    private AnchorPane achorfront1;

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
        User user= SessionManager.getInstance().getCurrentUser();
        String userId = user.getId();

        TicketService ticketService = new TicketService();
        List<Ticket> tickets = ticketService.getUserTickets(userId);

        achorfront.getChildren().clear();

        if (tickets.isEmpty()) {
            Label messageLabel = new Label("Aucun tickets achete");
            achorfront.getChildren().add(messageLabel);
        } else {
            EvenementFront evenementFront = new EvenementFront();
            GridPane gridPane = new GridPane();

            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int column = 0;
            int row = 0;

            for (Ticket ticket : tickets) {
                EvenementService evenementService = new EvenementService();
                Evenement event = evenementService.getEventById(ticket.getEvenementId());
                GridPane ticketCard = evenementFront.createTicketCard(event);
                gridPane.add(ticketCard, column, row);
                ticketCard.getStyleClass().add("mes_tickets");

                column++;
                if (column > 3) {
                    column = 0;
                    row++;
                }
            }

            achorfront.getChildren().add(gridPane);
        }
        System.out.println("Number of children in achorfront: " + achorfront.getChildren().size()); // Debugging line
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

}