package SportHub.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashController {

    @FXML
    private AnchorPane anchor;

    @FXML
    public void loadEvenementLayout() {
        try {
            AnchorPane evenementLayout = FXMLLoader.load(getClass().getResource("/SportHub/Evenement.fxml"));
            anchor.getChildren().setAll(evenementLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
public void loadTicketLayout() {
    try {
        AnchorPane ticketLayout = FXMLLoader.load(getClass().getResource("/SportHub/Ticket.fxml"));
        anchor.getChildren().setAll(ticketLayout);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    public void loadGestionProduitsLayout() {
        try {
            AnchorPane gestionProduitsLayout = FXMLLoader.load(getClass().getResource("/SportHub/Product.fxml"));
            anchor.getChildren().setAll(gestionProduitsLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}