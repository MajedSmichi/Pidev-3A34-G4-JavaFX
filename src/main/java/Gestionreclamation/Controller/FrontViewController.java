package Gestionreclamation.Controller;

import Gestionreclamation.Entity.User;
import Gestionreclamation.Cours.Controller.FrontCategoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontViewController {


    public Button logoutButton;
    @FXML
    private AnchorPane achorfront;

    @FXML
    private AnchorPane achorfront1;

    public void initialize() {
        ;
        logoutButton.setOnAction(event -> logout());


    }

    @FXML
    void logout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
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
            AnchorPane evenementFront = FXMLLoader.load(getClass().getResource("/SportHub/EvenementFront.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCrud/updateClient.fxml"));
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



}