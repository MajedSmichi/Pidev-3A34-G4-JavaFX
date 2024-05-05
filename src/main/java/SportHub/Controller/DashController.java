package SportHub.Controller;

import SportHub.Controller.MyController.CategoryListController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
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
    public void loadEvenementCards() {
        try {
            AnchorPane evenementLayout = FXMLLoader.load(getClass().getResource("/SportHub/EvenementBack.fxml"));
            anchor.getChildren().setAll(evenementLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
public void loadCourseLayout() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/ListCat.fxml"));
        AnchorPane courseLayout = loader.load();

        // Pass this DashController instance to CategoryListController
        CategoryListController categoryListController = loader.getController();
        categoryListController.setDashController(this);

        anchor.getChildren().setAll(courseLayout);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public AnchorPane getAnchor() {
        return anchor;
    }
}