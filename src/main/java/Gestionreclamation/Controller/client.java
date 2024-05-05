package Gestionreclamation.Controller;

import Gestionreclamation.Entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class client {


    @FXML
    private AnchorPane achorfront;
    @FXML
    private ImageView logoutButton;


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




    @FXML
    void logout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }



    @FXML
    public void loadEvenementFront() {
        try {
            AnchorPane evenementFront = FXMLLoader.load(getClass().getResource("/Gestionreclamation/client.fxml"));
            evenementFront.getStyleClass().add("center-content"); // Add the CSS class
            achorfront.getChildren().setAll(evenementFront);
            System.out.println("Evenement front view loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the evenement front view");
        }
    }

    @FXML
    public void loadUpdateClient() {
        try {
            System.out.println("current user email: " + SessionManager.getInstance().getUserEmail());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/UserCrud/updateClient.fxml"));
            AnchorPane updateClientView = loader.load();

            // Get the UserController and call handleUpdateAction1
            UserController userController = loader.getController();
            //userController.handleUpdateAction1();

            // Get the current user and show the user's data in the form
            User currentUser = SessionManager.getInstance().getCurrentUser();
            userController.showEditUser1(currentUser);

            achorfront.getChildren().setAll(updateClientView);
            System.out.println("Update client view loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the update client view");
        }
    }


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

    private AfficherReclamtion afficherReclamtion;

    // ...

    public void listeReclamtion() {
        afficherReclamtion = new AfficherReclamtion();
        afficherReclamtion.createCards();
        achorfront.getChildren().setAll(afficherReclamtion.getVbox());
    }
}
