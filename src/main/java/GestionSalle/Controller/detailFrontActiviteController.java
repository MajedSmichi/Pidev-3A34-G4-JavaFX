package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import GestionSalle.Entity.Salle;
import GestionSalle.Services.ActiviteService;
import GestionSalle.Services.SalleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class detailFrontActiviteController {

    @FXML
    private Label coachact;

    @FXML
    private Label dateact;

    @FXML
    private Label descriptionact;

    @FXML
    private Pane detail;

    @FXML
    private ImageView imageact;

    @FXML
    private Label nameact;

    @FXML
    private Label nbrmaxact;

    @FXML
    private Label salleact;
    @FXML
    private Button reserver;


    public void setData(Activite activite) {
        this.currentActivite = activite;
        nameact.setText(activite.getNom());
        descriptionact.setText(activite.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact.setText(formattedDate);
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
        salleact.setText(String.valueOf(activite.getSalle_id()));

        SalleService salleService = new SalleService();
        try {
            imageact.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }
        try {
            Salle salle = salleService.getSalleById(activite.getSalle_id());
            if (salle != null) {
                salleact.setText(salle.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Activite currentActivite;
    @FXML
    void reserverActivite(ActionEvent event) {
        ActiviteService activiteUserService = new ActiviteService();
        try {
            if (currentActivite.getNbr_max() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Désolé, cette activité est complète.");
                alert.showAndWait();
                return;
            }
            int userId = 1; // Replace this with actual method to get logged in user id
            activiteUserService.reserverActivite(currentActivite.getId(), userId);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Activité réservée avec succès.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Vous avez déjà réservé cette activité.");
            alert.showAndWait();
        }
    }

}
