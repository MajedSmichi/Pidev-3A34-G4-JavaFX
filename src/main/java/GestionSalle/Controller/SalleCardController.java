package GestionSalle.Controller;

import GestionSalle.Entity.Salle;
import GestionSalle.Services.SalleService;
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
import java.sql.SQLException;

public class SalleCardController {

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

    // Set mouse click event handler for PaneSalle
    PaneSalle.setOnMouseClicked(event -> {
        // Load detailFrontSalle.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/detailFrontSalle.fxml"));
        Parent detailFrontSalle;
        try {
            detailFrontSalle = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get the controller of detailFrontSalle.fxml
        detailFrontSalleController controller = fxmlLoader.getController();

        // Pass the Salle data to the controller
        controller.setSalle(this.salle);

        // Load Frontview.fxml
        FXMLLoader frontViewLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Frontview.fxml"));
        Parent frontView;
        try {
            frontView = frontViewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get the controller of Frontview.fxml
        FrontViewController frontViewController = frontViewLoader.getController();

        // Set detailFrontSalle as the content of the AnchorPane in Frontview.fxml
        frontViewController.getAnchor().getChildren().setAll(detailFrontSalle);

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a new scene with the loaded Frontview.fxml
        Scene scene = new Scene(frontView, currentStage.getWidth(), currentStage.getHeight());

        // Set the new scene to the current stage
        currentStage.setScene(scene);
    });
}}