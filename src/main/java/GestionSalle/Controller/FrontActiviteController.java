package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import GestionSalle.Entity.Salle;
import GestionSalle.Services.ActiviteService;
import GestionSalle.Services.SalleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class FrontActiviteController {

    @FXML
    private GridPane GridActivite;

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
    private ScrollPane list;

    @FXML
    private Label nameact;

    @FXML
    private Label nbrmaxact;

    @FXML
    private Label salleact;
    @FXML
    private TextField search;

    @FXML
    private Button reserver;

    private Activite currentActivite;

    public void initialize() {
        try {
            List<Activite> activites = getData();

            // Add a listener to the search TextField
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                List<Activite> filteredActivites = activites.stream()
                        .filter(activite -> activite.getNom().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());

                try {
                    populateGrid(filteredActivites);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            populateGrid(activites);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    private void populateGrid(List<Activite> activites) throws IOException {
        GridActivite.getChildren().clear();
        int row = 0;
        for (Activite activite : activites) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/ActiviteCard.fxml"));
            Pane pane = fxmlLoader.load();

            ActiviteCardController oneActivite = fxmlLoader.getController();
            oneActivite.setData(activite);
            pane.setOnMouseClicked(event -> displayActiviteDetails(activite));

            GridActivite.add(pane, 0, row); // Always add to the first column
            row++;
            GridPane.setMargin(pane, new Insets(10));
        }
    }

    private List<Activite> getData() throws SQLException {
        ActiviteService activiteService = new ActiviteService();
        return activiteService.getAllActivite();
    }
    private void displayActiviteDetails(Activite activite) {
        nameact.setText(activite.getNom());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact.setText(formattedDate);
        descriptionact.setText(activite.getDescription());
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
        salleact.setText(String.valueOf(activite.getSalle_id()));
        SalleService salleService = new SalleService();
        try {
            Salle salle = salleService.getSalleById(activite.getSalle_id());
            if (salle != null) {
                salleact.setText(salle.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            imageact.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }

        // Show the details AnchorPane
        detail.setVisible(true);
        this.currentActivite = activite;

    }
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
