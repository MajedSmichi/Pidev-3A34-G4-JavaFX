package SportHub.Controller;

/*import SportHub.Entity.Evenement;
import SportHub.Entity.Ticket;
import SportHub.Services.EvenementService;
import SportHub.Services.TicketService;*/
import SportHub.Controller.MyController.FrontCategoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    public void loadEvenementFront() {
        try {
            AnchorPane evenementFront = FXMLLoader.load(getClass().getResource("/SportHub/EvenementFront.fxml"));
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
    /*public void loadUserTickets() {
        try {
            // Assuming the user ID is "1" for testing purposes
            String userId = "1f04f48b-5c95-4f98-97a8-0f9302114b0a";

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
    }*/



    @FXML
    public void loadEvenementAndMostPopularEvents() {
        loadEvenementFront();
        // loadMostPopularEvents();
    }


    @FXML
    public void loadCoursesFront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/FrontCategory.fxml"));
            AnchorPane coursesFront = loader.load();

            // Get the controller of the FrontCategory.fxml
            FrontCategoryController frontCategoryController = loader.getController();

            // Set achorfront to the FrontCategoryController
            frontCategoryController.setAchorfront(achorfront);

            achorfront.getChildren().setAll(coursesFront);
            System.out.println("Courses front view loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the courses front view");
        }
    }
}




