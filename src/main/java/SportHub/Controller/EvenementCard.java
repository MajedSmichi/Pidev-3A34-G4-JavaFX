package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Services .EvenementService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class EvenementCard {

    @FXML
    private Label Date;

    @FXML
    private Pane PaneEvenement;

    @FXML
    private Label name;

    @FXML
    private ImageView imageEvnement;

    @FXML
    private Button supprimer;

    private Evenement event;
    private EvenementService evenementService = new EvenementService();

    public void setData(Evenement event) {
        this.event = event;
        Date.setText(String.valueOf(event.getDateEvenement())); // Assuming Date is a Label
        name.setText(event.getNom()); // Assuming FullName is a Label

        // Set the image
        String imageUrl = event.getImageEvenement();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageEvnement.setImage(image);
        } else {
            // Handle the case when imageUrl is null or empty
            // For example, set a default image
        }


    }

}