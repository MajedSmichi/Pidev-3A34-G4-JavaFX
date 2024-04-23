package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.text.SimpleDateFormat;

public class detailFrontActiviteController {

    @FXML
    private Label coachact;

    @FXML
    private Label dateact;

    @FXML
    private Label descriptionact;

    @FXML
    private Pane detail;

    @FXML
    private ImageView imageact;

    @FXML
    private Label nameact;

    @FXML
    private Label nbrmaxact;

    @FXML
    private Label salleact;
    public void setData(Activite activite) {
        nameact.setText(activite.getNom());
        descriptionact.setText(activite.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact.setText(formattedDate);
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
        salleact.setText(String.valueOf(activite.getSalle_id()));

        try {
            imageact.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }
    }

}
