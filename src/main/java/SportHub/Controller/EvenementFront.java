package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Entity.Ticket;
import SportHub.Services.EvenementService;
import SportHub.Services.TicketService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.SQLException;
import java.util.List;

public class EvenementFront {

    @FXML
    private GridPane eventContainer; // This is the container where you will add the event cards

    public void initialize() {
        try {
            List<Evenement> events = fetchEventsFromDatabase();

            int column = 0;
            int row = 0;

            for (Evenement event : events) {
                GridPane eventCard = createEventCard(event);
                eventContainer.add(eventCard, column, row);

                column++;
                if (column > 2) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private GridPane createEventCard(Evenement event) {
    GridPane eventCard = new GridPane();
    eventCard.getStyleClass().add("card"); // Add the style class

    // Create an ImageView and load the image from the file path
    ImageView eventImage = new ImageView();
    Image image = new Image("file:" + event.getImageEvenement());
    eventImage.setImage(image);
    eventImage.setFitWidth(190);  // Set the width of the ImageView
    eventImage.setFitHeight(220);
    eventImage.setPreserveRatio(true);  // Preserve the aspect ratio
    eventCard.add(eventImage, 0, 0);

    Label eventName = new Label(event.getNom());
    eventName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
    eventCard.add(eventName, 0, 1);

    Label eventDate = new Label(event.getDateEvenement().toString());
    eventDate.setStyle("-fx-font-size: 14;");
    eventCard.add(eventDate, 0, 2);

    // Add an event handler to the card
    eventCard.setOnMouseClicked(e -> {
        // Create and display the detailed card
        GridPane detailedCard = createDetailedCard(event);
        eventContainer.getChildren().clear(); // Clear the event container
        eventContainer.add(detailedCard, 0, 0); // Add the detailed card to the event container
    });

    return eventCard;
}

private GridPane createDetailedCard(Evenement event) {
    GridPane detailedCard = new GridPane();
    detailedCard.getStyleClass().add("detailed-card"); // Add the style class

    // Create an ImageView and load the image from the file path
    ImageView eventImage = new ImageView();
    Image image = new Image("file:" + event.getImageEvenement());
    eventImage.setImage(image);
    eventImage.setFitWidth(300);  // Set the width of the ImageView
    eventImage.setFitHeight(450);
    eventImage.setPreserveRatio(true);  // Preserve the aspect ratio
    detailedCard.add(eventImage, 0, 0);

    Label eventName = new Label("Event Name: " + event.getNom());
    eventName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
    detailedCard.add(eventName, 0, 1);

    Label eventDate = new Label("Event Date: " + event.getDateEvenement().toString());
    eventDate.setStyle("-fx-font-size: 14;");
    detailedCard.add(eventDate, 0, 2);

    Label eventLieu = new Label("Event Lieu: " + event.getLieu());
    eventDate.setStyle("-fx-font-size: 14;");
    detailedCard.add(eventLieu, 0, 3);

    Label eventDescription = new Label("Event Description: " + event.getDescription());
    eventDescription.setStyle("-fx-font-size: 14;");
    detailedCard.add(eventDescription, 0, 4);



    Button getTicketButton = new Button("Get Ticket");
    getTicketButton.setOnAction(e -> {
        // Create and display the ticket card
        GridPane ticketCard = null;
        try {
            ticketCard = createTicketCard(event);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        //eventContainer.getChildren().clear(); // Clear the event container
        eventContainer.add(ticketCard, 1, 0); // Add the ticket card to the event container
    });
    detailedCard.add(getTicketButton, 0, 6); // Add the button to the detailed card



    // Create a back button with text
    Button backButton = new Button("Back");

    // Add an event handler to the back button
    backButton.setOnAction(e -> {
        try {
            // Clear the event container
            eventContainer.getChildren().clear();

            // Repopulate the event container with the event cards
            List<Evenement> events = fetchEventsFromDatabase();
            int column = 0;
            int row = 0;
            for (Evenement ev : events) {
                GridPane eventCard = createEventCard(ev);
                if (column == 3) {
                    column = 0;
                    row++;
                }
                eventContainer.add(eventCard, column++, row);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    });

    // Add the back button to the detailed card
    detailedCard.add(backButton, 1, 6);

    return detailedCard;
}


private GridPane createTicketCard(Evenement event) throws SQLException {
    GridPane ticketCard = new GridPane();
    ticketCard.getStyleClass().add("ticket-card"); // Add the style class

    // Create a TicketService instance to fetch the ticket details
    TicketService ticketService = new TicketService();
    Ticket ticket = ticketService.getTicketByEventId(event.getId());

    // Check if the ticket is not null before accessing it
    if (ticket != null) {
        // Add the ticket details to the card


        Label eventName = new Label("Event Name: " + event.getNom());
        eventName.getStyleClass().add("ticket-label");

        Label eventTitle = new Label("     VOTRE TICKET " );
        Label eventTi = new Label("------------------------------- " );
        Label eventTi1 = new Label("------------------------------- " );



        Label ticketType = new Label("Ticket Type: " + ticket.getType());
        ticketType.getStyleClass().add("ticket-label");

        Label ticketPrice = new Label("Ticket Price: " + ticket.getPrix() +" DT");
        ticketPrice.getStyleClass().add("ticket-label");

        Label ticketQuantity = new Label("Ticket Quantity: " + ticket.getNbreTicket());
        ticketQuantity.getStyleClass().add("ticket-label");

        ticketCard.add(eventTitle, 0, 0);
        ticketCard.add(eventTi, 0, 1);
        ticketCard.add(eventName, 0, 2);
        ticketCard.add(ticketType, 0, 3);
        ticketCard.add(ticketPrice, 0, 4);
        ticketCard.add(ticketQuantity, 0, 5);
        ticketCard.add(eventTi1, 0, 6);


        // Create a back button with text
        Button backButton = new Button("PARTICIPATE");
        // Add the back button to the ticket card
        ticketCard.add(backButton, 0, 7);

    } else {
        // Handle the case where the ticket is null
        Label noTicketLabel = new Label("No ticket available .");
        noTicketLabel.getStyleClass().add("ticket-label");
        ticketCard.add(noTicketLabel, 0, 0);
    }




    return ticketCard;
}

    private List<Evenement> fetchEventsFromDatabase() throws SQLException {
        EvenementService service = new EvenementService();
        return service.getAllEvents();
    }
}