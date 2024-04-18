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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ActiviteController {

    @FXML
    private GridPane GridActivite;

    @FXML
    private Button addActivite;

    @FXML
    private AnchorPane ajout;

    @FXML
    private Button ajouter;

    @FXML
    private TextField coach;

    @FXML
    private Label coachact;

    @FXML
    private DatePicker date;

    @FXML
    private Label dateact;

    @FXML
    private TextField decription;

    @FXML
    private Label descriptionact;

    @FXML
    private AnchorPane detail;

    @FXML
    private ImageView image;

    @FXML
    private ImageView imageact;

    @FXML
    private Button importimage;

    @FXML
    private ScrollPane list;

    @FXML
    private Label nameact;

    @FXML
    private TextField nbrmax;

    @FXML
    private Label nbrmaxact;

    @FXML
    private TextField nom;

    @FXML
    private ComboBox<String> salle;

    @FXML
    private Label salleact;




    public void initialize() {


        try {
            // Populate the ComboBox with the names of all Salle objects
            SalleService salleService = new SalleService();
            List<Salle> salles = salleService.getAllSalle();
            for (Salle salle : salles) {
                this.salle.getItems().add(salle.getNom());
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<Activite> activites = getData();
            int row = 0;
            for (Activite activite : activites) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneActivite.fxml"));
                Pane pane = fxmlLoader.load();
                uneActiviteController oneActivite = fxmlLoader.getController();
                oneActivite.setData(activite);
                pane.setOnMouseClicked(event -> displayActiviteDetails(activite)); // Add this line

                GridActivite.add(pane, 0, row); // Always add to the first column
                row++;
                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Activite> getData() throws SQLException {
        ActiviteService activiteService = new ActiviteService();
        return activiteService.getAllActivite();
    }
   @FXML
void addActivite(ActionEvent event) {
    Activite newActivite = new Activite();
    newActivite.setNom(nom.getText());
    newActivite.setCoach(coach.getText());
    newActivite.setDate(java.sql.Timestamp.valueOf(date.getValue().atStartOfDay()));
    newActivite.setDescription(decription.getText());
    newActivite.setNbr_max(Integer.parseInt(nbrmax.getText()));
    newActivite.setImage(imagePath);

    SalleService salleService = new SalleService();
    try {
        Salle salle = salleService.getSalleByName(this.salle.getValue());
        if (salle != null) {
            newActivite.setSalle_id(salle.getId());
        } else {
            // Handle the case where no Salle with the selected name is found
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No Salle with the selected name is found!");
            alert.showAndWait();
            return;
        }

        ActiviteService activiteService = new ActiviteService();
        activiteService.addActivite(newActivite);
        refreshGridPane(); // Refresh the GridPane after adding a new Activite

        // Clear the fields
        nom.clear();
        coach.clear();
        date.setValue(null);
        decription.clear();
        nbrmax.clear();
        this.salle.setValue(null);
        image.setImage(null); // Clear the image
        imagePath = null; // Clear the image path
        ajout.setVisible(false);


    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void refreshGridPane() throws SQLException {
        GridActivite.getChildren().clear(); // Clear the GridPane
        List<Activite> activites = getData(); // Get the updated data
        int row = 0;
        for (Activite activite : activites) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneActivite.fxml"));
            try {
                Pane pane = fxmlLoader.load(); // Load as Pane
                uneActiviteController oneActivite = fxmlLoader.getController();
                oneActivite.setData(activite);
                GridActivite.add(pane, 0, row); // Always add to the first column
                row++;
                GridPane.setMargin(pane, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String imagePath = null;
    @FXML
    void importimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            this.image.setImage(image); // Assuming 'image' is the ImageView in your ActiviteController
            imagePath = selectedFile.getPath(); // Assuming 'imagePath' is a field in your ActiviteController
        }
    }
        @FXML
    void visible(ActionEvent event) {
        // Toggle the visibility of the AnchorPane
        boolean isVisible = ajout.isVisible();
        ajout.setVisible(!isVisible);
    }
    private void displayActiviteDetails(Activite activite) {
        nameact.setText(activite.getNom());
        dateact.setText(activite.getDate().toString());
        descriptionact.setText(activite.getDescription());
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
        salleact.setText(String.valueOf(activite.getSalle_id()));

        try {
            imageact.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }

        // Show the details AnchorPane
        detail.setVisible(true);
    }


}