package Gestionreclamation.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class client {


    @FXML
    private AnchorPane achorfront;


    @FXML
    public void reclamation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/AjouterReclamation.fxml"));
            Pane reclamtion = fxmlLoader.load(); // Load as Pane
            achorfront.getChildren().setAll(reclamtion);
            System.out.println("reclmation front view loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the evenement front view");
        }
    }

    public void listeReclamtion() {
        AfficherReclamtion afficherReclamtion = new AfficherReclamtion();
        afficherReclamtion.createCards();
        achorfront.getChildren().setAll(afficherReclamtion.getVbox());
    }

}