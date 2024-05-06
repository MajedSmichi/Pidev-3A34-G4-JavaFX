package Gestionreclamation.Controller.Salle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class FrontViewController {
    @FXML
    private AnchorPane achorfront;
    public AnchorPane getAnchor() {
        return achorfront;
    }


    public void loadSalleCards() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/FrontSalle.fxml"));
            BorderPane salleGrid = loader.load();
            achorfront.getChildren().setAll(salleGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadActivite() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/FrontActivite.fxml"));
            AnchorPane activiteGrid = loader.load();
            achorfront.getChildren().setAll(activiteGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}