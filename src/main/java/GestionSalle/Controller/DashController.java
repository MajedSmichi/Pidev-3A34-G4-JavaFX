package GestionSalle.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashController {

    @FXML
    private AnchorPane anchor;

    @FXML
    private Button gestionActivite;

    @FXML
    private Button gestionSalle;

    @FXML
    void openActivite(ActionEvent event) {
        try {
            // Load the FXML file
            Parent activiteView = FXMLLoader.load(getClass().getResource("/GestionSalle/Activite.fxml"));

            // Clear the existing content of the anchor pane and add the loaded FXML
            anchor.getChildren().setAll(activiteView);
        } catch (IOException e) {
            // Handle the exception (e.g., the file is not found)
            e.printStackTrace();
        }
    }


    @FXML
    void openSalle(ActionEvent event) {
        try {
            Parent salleView = FXMLLoader.load(getClass().getResource("/GestionSalle/Salle.fxml"));
            anchor.getChildren().setAll(salleView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AnchorPane getAnchor() {
        return anchor;
    }
    public void refreshSalle() {
        try {
            Parent salleView = FXMLLoader.load(getClass().getResource("/GestionSalle/Salle.fxml"));
            anchor.getChildren().setAll(salleView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
