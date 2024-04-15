package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Services.EvenementService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EvenementBack {

    @FXML
    private GridPane eventContainerBack;

    @FXML
    private ScrollPane scrollPane;

    private EvenementService evenementService;

    @FXML
    private AnchorPane ajouterPane;

    @FXML
    private Button ajouterEvenement;

    @FXML
    private Button hide;

    public void initialize() {
        evenementService = new EvenementService();
        ajouterPane.setVisible(false); // Make ajouterPane not visible
        ajouterEvenement.setOnAction(e -> {
            ajouterPane.setVisible(true);
        });
        hide.setOnAction(e -> {
            ajouterPane.setVisible(false);
        });
        try {
            displayEvents();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void displayEvents() throws SQLException, IOException {
        List<Evenement> events = evenementService.getAllEvents();
        for (int i = 0; i < events.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
fxmlLoader.setLocation(getClass().getResource("/SportHub/EvenementCard.fxml"));            Pane pane = fxmlLoader.load();

            EvenementCard controller = fxmlLoader.getController();
            controller.setData(events.get(i));

            eventContainerBack.add(pane, i % 3, i / 3);
        }
    }
}