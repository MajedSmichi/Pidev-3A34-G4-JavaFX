package GestionSalle.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class FrontViewController {
    @FXML
    private AnchorPane achorfront;
    public AnchorPane getAnchor() {
        return achorfront;
    }


    public void loadSalleCards() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionSalle/FrontSalle.fxml"));
            BorderPane salleGrid = loader.load();
            achorfront.getChildren().setAll(salleGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadActivite() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionSalle/FrontActivite.fxml"));
            AnchorPane activiteGrid = loader.load();
            achorfront.getChildren().setAll(activiteGrid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}