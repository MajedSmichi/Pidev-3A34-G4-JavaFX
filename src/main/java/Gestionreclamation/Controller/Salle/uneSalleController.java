package Gestionreclamation.Controller.Salle;

import Gestionreclamation.Controller.SampleController;
import Gestionreclamation.Entity.Salle.Salle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class uneSalleController {

    @FXML
    private Label Addresse;

    @FXML
    private Label NumTel;

    @FXML
    private Pane PaneSalle;

    @FXML
    private Label capacite;

    @FXML
    private Label description;

    @FXML
    private ImageView logo;

    @FXML
    private Label name;

    @FXML
    private Label nbrclient;

    private Salle salle;
    public void setData(Salle salle) {
        this.salle = salle;
        name.setText(salle.getNom());
        Addresse.setText(salle.getAddresse());
        NumTel.setText(String.valueOf(salle.getNum_tel()));



        try {
            logo.setImage(new Image(salle.getLogo_salle()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + salle.getLogo_salle());
        }

       //Set mouse click event handler for PaneSalle
        PaneSalle.setOnMouseClicked(event -> {
            // Load detailSalle.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/detailSalle.fxml"));
            Parent detailSalle ;
            try {
                detailSalle = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Get the controller of detailSalle.fxml
            detailSalleController controller = fxmlLoader.getController();

            // Pass the Salle data to the controller
            controller.setData(this.salle);

            // Load Dash.fxml
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/Dash.fxml"));
            Parent dash;
            try {
                dash = dashLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Get the controller of Dash.fxml
            SampleController dashController = dashLoader.getController();

            // Set detailSalle as the content of the AnchorPane in Dash.fxml
            dashController.getAnchor().getChildren().setAll(detailSalle);

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the loaded Dash.fxml
            Scene scene = new Scene(dash, currentStage.getWidth(), currentStage.getHeight());

            // Set the new scene to the current stage
            currentStage.setScene(scene);
        });
    }
}