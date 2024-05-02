package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Entity.Ticket;
import SportHub.Services.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import SportHub.Services.WeatherService;
import javafx.scene.layout.VBox;
import kong.unirest.json.JSONObject;




public class EvenementFront {

    @FXML
    private GridPane eventContainer; // This is the container where you will add the event cards

    private WeatherService weatherService;
    private TwilioService twilioService = new TwilioService();


    public void initialize() {
        try {
            weatherService = new WeatherService();

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

    eventCard.setVgap(10); // Set the amount of vertical space you want

    // Fetch the weather data for the city
    WeatherService weatherService = new WeatherService();
    JSONObject weatherData = weatherService.getWeatherByCity(event.getLieu());

    String weatherInfo = "Weather data could not be retrieved.";
    if (weatherData != null) {
        // Extract the weather information from the JSON object
        String weatherDescription = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
        double temperatureInKelvin = weatherData.getJSONObject("main").getDouble("temp");

        // Convert the temperature to Celsius
        double temperatureInCelsius = temperatureInKelvin - 273.15;

        weatherInfo = " " + weatherDescription + ",  " + String.format("%.2f", temperatureInCelsius) + "°C";
    }

    Label weatherLabel = new Label(weatherInfo);
    eventCard.add(weatherLabel, 0, 3); // Add the weather label to the top of the card

    // Create an ImageView and load the image from the file path
    ImageView eventImage = new ImageView();
    Image image = new Image("file:" + event.getImageEvenement());
    eventImage.setImage(image);
    eventImage.setFitWidth(190);  // Set the width of the ImageView
    eventImage.setFitHeight(220);
    eventImage.setPreserveRatio(true);  // Preserve the aspect ratio
    eventCard.add(eventImage, 0, 1);

    Label eventName = new Label(event.getNom());
    eventName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
    eventCard.add(eventName, 0, 0);

    GridPane.setHalignment(eventName, javafx.geometry.HPos.CENTER);

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


        // Fetch the weather data for the city
        WeatherService weatherService = new WeatherService();
        JSONObject weatherData = weatherService.getWeatherByCity(event.getLieu());

        String weatherInfo = "Weather data could not be retrieved.";
        if (weatherData != null) {
            // Extract the weather information from the JSON object
            String weatherDescription = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperatureInKelvin = weatherData.getJSONObject("main").getDouble("temp");

            // Convert the temperature to Celsius
            double temperatureInCelsius = temperatureInKelvin - 273.15;

            weatherInfo = "Weather: " + weatherDescription + ", Temperature: " + String.format("%.2f", temperatureInCelsius) + "°C";
        }

        Label weatherLabel = new Label(weatherInfo);
        detailedCard.add(weatherLabel, 0, 5); // Add the weather label to the card



        // Set vertical gap
        detailedCard.setVgap(10); // Set the amount of vertical space you want

        // Create an ImageView and load the image from the file path
        ImageView eventImage = new ImageView();
        Image image = new Image("file:" + event.getImageEvenement());
        eventImage.setImage(image);
        eventImage.setFitWidth(300);  // Set the width of the ImageView
        eventImage.setFitHeight(450);
        eventImage.setPreserveRatio(true);  // Preserve the aspect ratio
        detailedCard.add(eventImage, 0, 1);



        Label eventName = new Label( event.getNom());
        eventName.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");
        detailedCard.add(eventName, 0, 0);


        // Center the eventName in its area of the GridPane
        GridPane.setHalignment(eventName, javafx.geometry.HPos.CENTER);

        Label eventDate = new Label("Event Date: " + event.getDateEvenement().toString());
        //eventDate.setStyle("-fx-font-size: 14;");
        detailedCard.add(eventDate, 0, 2);

        Label eventLieu = new Label("Event Lieu: " + event.getLieu());
        //eventDate.setStyle("-fx-font-size: 14;");
        detailedCard.add(eventLieu, 0, 4);

        Label eventDescription = new Label("Event Description: " + event.getDescription());
       // eventDescription.setStyle("-fx-font-size: 14;");
        detailedCard.add(eventDescription, 0, 7);


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
        detailedCard.add(getTicketButton, 0, 8); // Add the button to the detailed card


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
        detailedCard.add(backButton, 1, 8);

        // Calculate the number of days left for the event
        LocalDate currentDate = LocalDate.now();
        LocalDate evenementDate = event.getDateEvenement().toLocalDate();
        long daysLeft = ChronoUnit.DAYS.between(currentDate, evenementDate);

        // Create a label to display the number of days left
        Label daysLeftLabel = new Label("Days left for the event: " + daysLeft);
        detailedCard.add(daysLeftLabel, 0, 3); // Adjust the index as per your layout

        return detailedCard;
    }


private GridPane createTicketCard(Evenement event) throws SQLException {
    GridPane ticketCard = new GridPane();
    ticketCard.getStyleClass().add("ticket-card"); // Add the style class

    // Create a TicketService instance to fetch the ticket details
    TicketService ticketService = new TicketService();
    Ticket ticket = ticketService.getTicketByEventId(event.getId());

    // Check if the ticket is not null before accessing it
    if (ticket != null && ticket.getNbreTicket() > 0) {
        // Add the ticket details to the card
        Label eventName = new Label("Event Name: " + event.getNom());
        eventName.getStyleClass().add("ticket-label");

        Label eventTitle = new Label("     VOTRE TICKET ");
        Label eventTi = new Label("------------------------------- ");
        Label eventTi1 = new Label("------------------------------- ");

        Label ticketType = new Label("Ticket Type: " + ticket.getType());
        ticketType.getStyleClass().add("ticket-label");

        Label ticketPrice = new Label("Ticket Price: " + ticket.getPrix() + " DT");
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
        Button participateButton = new Button("PARTICIPATE");

        // Add an action to the "PARTICIPATE" button
        participateButton.setOnAction(e -> {
            // Decrease the ticket quantity by one
            ticket.setNbreTicket(ticket.getNbreTicket() - 1);



            // Extract the parameters from the Ticket object
            int id = ticket.getId();
            Integer evenement_id = ticket.getEvenementId();
            Integer prix = ticket.getPrix();
            String type = ticket.getType();
            Integer nbre_ticket = ticket.getNbreTicket();

            // Update the ticket in the database
            try {
                ticketService.updateTicket(id, evenement_id, prix, type, nbre_ticket);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            // Refresh the ticket card
            eventContainer.getChildren().remove(ticketCard);
            try {
                GridPane newTicketCard = createTicketCard(event);
                eventContainer.add(newTicketCard, 1, 0);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }



            // Send a confirmation message to the user
            // Assuming you have an instance of EvenementFront
            EvenementFront evenementFront = new EvenementFront();

            // Create a PaymentConfirmation instance
            PaymentConfirmation paymentConfirmation = new PaymentConfirmation();

            // Create a StripeService instance
            StripeService stripeService = new StripeService();

            try {
                // Create a payment intent and get the client secret
                String clientSecret = stripeService.createPaymentIntent(ticket.getPrix());

                // Open the payment form
                paymentConfirmation.confirmPayment(clientSecret, evenementFront);
            } catch (StripeException stripeException) {
                stripeException.printStackTrace();
            }

        });

        // Add the button to the ticket card
        ticketCard.add(participateButton, 0, 7);

    } else {
        // Handle the case where the ticket is null or sold out
        Label noTicketLabel = new Label("Ticket épuisé.");
        noTicketLabel.getStyleClass().add("ticket-label");
        ticketCard.add(noTicketLabel, 0, 0);
    }

    return ticketCard;
}



    public void showSuccessMessage() {
        // Clear the event container
        eventContainer.getChildren().clear();

        // Create a success message label
        Label successLabel = new Label("Payment Successful!");
        successLabel.getStyleClass().add("success-label"); // Add a CSS class for styling

        // Add the success message label to the event container
        eventContainer.add(successLabel, 0, 0);
    }

    private List<Evenement> fetchEventsFromDatabase() throws SQLException {
        EvenementService service = new EvenementService();
        return service.getAllEvents();
    }
}