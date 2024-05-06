package Gestionreclamation.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dash {
    private static Dash instance;

    public Dash() {
        instance = this;
    }

    public static Dash getInstance() {
        return instance;
    }

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
            Parent salleView = FXMLLoader.load(getClass().getResource("/GestionReclamation/GestionSalle/Salle.fxml"));
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
            Parent salleView = FXMLLoader.load(getClass().getResource("/GestionReclamation/GestionSalle/Salle.fxml"));
            anchor.getChildren().setAll(salleView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadRec() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReclamationView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            anchor.getChildren().setAll(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadRep() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReponseView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            anchor.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void loadGestionProduitsLayout() {
        try {
            AnchorPane gestionProduitsLayout = FXMLLoader.load(getClass().getResource("/SportHub/Product.fxml"));
            anchor.getChildren().setAll(gestionProduitsLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void loadGestionCategroiesLayout() {
        try {
            AnchorPane gestionProduitsLayout = FXMLLoader.load(getClass().getResource("/SportHub/Categorie.fxml"));
            anchor.getChildren().setAll(gestionProduitsLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
