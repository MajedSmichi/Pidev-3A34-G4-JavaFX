package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import GestionSalle.Entity.Salle;
import GestionSalle.Services.ActiviteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class detailFrontSalleController {

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addresse;

    @FXML
    private TextField capacite;

    @FXML
    private Label capaciteLabel;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private TextField name;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nbrclients;

    @FXML
    private Label nbrClientLabel;

    @FXML
    private TextField numtel;

    @FXML
    private Label numTelLabel;

    private Salle salle;

    @FXML
    ScrollPane salleACTIVITE;
    @FXML
    private GridPane GridActivite;



    private Activite currentActivite;
    public void setCurrentActivite(Activite activite) {
        this.currentActivite = activite;
    }
    public void setSalle(Salle salle) {
        this.salle = salle;
        nameLabel.setText(salle.getNom());
        addressLabel.setText(salle.getAddresse());
        numTelLabel.setText(String.valueOf(salle.getNum_tel()));
        capaciteLabel.setText(String.valueOf(salle.getCapacite()));
        nbrClientLabel.setText(String.valueOf(salle.getNbr_client()));
        descriptionLabel.setText(salle.getDescription());

        try {
            logoImageView.setImage(new Image(salle.getLogo_salle()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + salle.getLogo_salle());
        }

        try {
            List<Activite> activites = getData();
            int row = 0;
            for (Activite activite : activites) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/ActiviteCard.fxml"));
                Pane pane = fxmlLoader.load();

                ActiviteCardController oneActivite = fxmlLoader.getController();
                oneActivite.setData(activite);
                pane.setOnMouseClicked(event -> {
    try {
        currentActivite = activite;
        // Load detailFrontActivite.fxml
        FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/GestionSalle/detailFrontActivite.fxml"));
        Parent detailFrontActivite = detailLoader.load();

        // Get the controller of detailFrontActivite.fxml
        detailFrontActiviteController controller = detailLoader.getController();

        // Pass the Activite data to the controller
        controller.setData(activite);

        // Get the current scene
        Scene currentScene = ((Node) event.getSource()).getScene();

        // Find the AnchorPane achorfront in Frontview.fxml
        AnchorPane anchorPane = (AnchorPane) currentScene.lookup("#achorfront");

        // Set detailFrontActivite as the content of the AnchorPane achorfront
        anchorPane.getChildren().setAll(detailFrontActivite);

    } catch (IOException e) {
        e.printStackTrace();
    }
});

                GridActivite.add(pane, 0, row); // Always add to the first column
                row++;
                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    private List<Activite> getData() throws SQLException {
        ActiviteService activiteService = new ActiviteService();
        return activiteService.getActiviteBySalle(salle.getId());
    }

}