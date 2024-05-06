package Gestionreclamation.Controller.Salle;

import Gestionreclamation.Entity.Salle.Activite;
import Gestionreclamation.Entity.Salle.Salle;
import Gestionreclamation.Services.Salle.ActiviteService;
import Gestionreclamation.Services.Salle.SalleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class detailSalleController {

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addresse;

    @FXML
    private TextField capacite;

    @FXML
    private Label capaciteLabel;

    @FXML
    private Button delete;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button event_import;

    @FXML
    private AnchorPane gestion;

    @FXML
    private ScrollPane list;

    @FXML
    private ImageView logoImageView;

    @FXML
    private ImageView logoSalle;

    @FXML
    private Button modifer;

    @FXML
    private Button modifierSalle;

    @FXML
    private TextField name;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nbrClientLabel;

    @FXML
    private TextField nbrclients;

    @FXML
    private Label numTelLabel;

    @FXML
    private TextField numtel;

    @FXML
    private GridPane GridActivite;
    @FXML
    private Button statistique;

    @FXML
    private AnchorPane statique;
    @FXML
    private StackedBarChart<String, Integer> stat;
    private Activite currentActivite;



    private Salle salle;
    public void setData(Salle salle) {
    this.salle = salle;
    nameLabel.setText(salle.getNom());
    addressLabel.setText(salle.getAddresse());
    numTelLabel.setText(String.valueOf(salle.getNum_tel()));
    capaciteLabel.setText(String.valueOf(salle.getCapacite()));
    nbrClientLabel.setText(String.valueOf(salle.getNbr_client()));
    descriptionLabel.setText(salle.getDescription());

    // Set the text fields
    name.setText(salle.getNom());
    addresse.setText(salle.getAddresse());
    numtel.setText(String.valueOf(salle.getNum_tel()));
    capacite.setText(String.valueOf(salle.getCapacite()));
    nbrclients.setText(String.valueOf(salle.getNbr_client()));
    description.setText(salle.getDescription());

    try {
        logoImageView.setImage(new Image(salle.getLogo_salle()));
        logoSalle.setImage(new Image(salle.getLogo_salle()));
    } catch (IllegalArgumentException e) {
        System.out.println("Invalid URL or resource not found: " + salle.getLogo_salle());
    }
    try {
        stat.getStylesheets().add(getClass().getResource("/GestionSalle/Chart.css").toExternalForm());
        List<Activite> activites = getData();
        int row = 0;
        for (Activite activite : activites) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/ActiviteCard.fxml"));
            Pane pane = fxmlLoader.load();

            ActiviteCardController oneActivite = fxmlLoader.getController();
            oneActivite.setData(activite);
            pane.setOnMouseClicked(event -> {
                try {
                    currentActivite = activite;
                    // Load detailActivite.fxml
                    FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/detailActivite.fxml"));
                    Parent detailActivite = detailLoader.load();

                    // Get the controller of detailActivite.fxml
                    detailActiviteController controller = detailLoader.getController();

                    // Pass the Activite data to the controller
                    controller.setData(activite);

                    // Pass the currentActivite to the controller
                    controller.setCurrentActivite(currentActivite);

                    // Get the current scene
                    Scene currentScene = ((Node) event.getSource()).getScene();

                    // Find the AnchorPane in the current scene
                    AnchorPane anchorPane = (AnchorPane) currentScene.lookup("#anchor");

                    // Set detailActivite as the content of the AnchorPane
                    anchorPane.getChildren().setAll(detailActivite);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            GridActivite.add(pane, 0, row); // Always add to the first column
            row++;
            GridPane.setMargin(pane, new Insets(10));
            ActiviteService activiteService = new ActiviteService();

            // Get the number of reservations for the activity
            int nbrReservations = activiteService.getUsersByActiviteId(activite.getId()).size(); // Make sure this method exists in your Activite class
            // Create a new series for each activity
            // Create a new series for each activity
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(activite.getNom(), nbrReservations));
            // Add the series to the BarChart
            stat.getData().add(series);


        }
    } catch (IOException | SQLException e) {
        e.printStackTrace();
    }

}
    private List<Activite> getData() throws SQLException {
        ActiviteService activiteService = new ActiviteService();
        return activiteService.getActiviteBySalle(salle.getId());
    }
    private String imagePath;
    @FXML
    void importimage(ActionEvent event) {
        // Open the file chooser dialog to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            logoSalle.setImage(image);
            imagePath = selectedFile.getPath(); // Store the path of the selected file

        }
    }

    @FXML
    void modifierSalle(ActionEvent event) {


        // If a new image has been imported, use it. Otherwise, keep the current image.
        String newLogoSalle = (imagePath != null) ? imagePath : salle.getLogo_salle();

        if (name.getText().isEmpty() || addresse.getText().isEmpty() || numtel.getText().isEmpty() ||
                capacite.getText().isEmpty() || description.getText().isEmpty() || nbrclients.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Check if description is at least 15 characters
        if (description.getText().length() < 15) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "La description doit contenir au moins 15 caractères.");
            alert.showAndWait();
            return;
        }


        // Check if numtel is a valid number
        try {
            Integer.parseInt(numtel.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer un numéro de téléphone valide.");
            alert.showAndWait();
            return;
        }

        // Check if capacite is a valid integer
        try {
            Integer.parseInt(capacite.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer une capacité valide.");
            alert.showAndWait();
            return;
        }

        // Check if nbrclients is a valid integer
        try {
            Integer.parseInt(nbrclients.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer un nombre valide de clients.");
            alert.showAndWait();
            return;
        }
        // Get the new values from the text fields
        String newName = name.getText();
        String newAddress = addresse.getText();
        int newNumTel = Integer.parseInt(numtel.getText());
        int newCapacite = Integer.parseInt(capacite.getText());
        String newDescription = description.getText();
        int newNbrClients = Integer.parseInt(nbrclients.getText());

        // Update the Salle object
        salle.setNom(newName);
        salle.setAddresse(newAddress);
        salle.setNum_tel(newNumTel);
        salle.setCapacite(newCapacite);
        salle.setDescription(newDescription);
        salle.setNbr_client(newNbrClients);
        salle.setLogo_salle(newLogoSalle);

        // Call the method from SalleService to update the Salle in the database
        SalleService salleService = new SalleService();
        try {
            salleService.updateSalle(salle.getId(), newName, newAddress, newNumTel, newCapacite, newDescription, newNbrClients, newLogoSalle);

            // Load Salle.fxml
            FXMLLoader salleLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/Salle.fxml"));
            Parent salleView = salleLoader.load();

            // Get the instance of DashController
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/Dash.fxml"));
            Parent dash = dashLoader.load();
            DashController dashController = dashLoader.getController();

            // Set Salle.fxml as the content of the AnchorPane in Dash.fxml
            dashController.getAnchor().getChildren().setAll(salleView);

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the loaded Dash.fxml
            Scene scene = new Scene(dash, currentStage.getWidth(), currentStage.getHeight());

            // Set the new scene to the current stage
            currentStage.setScene(scene);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void deleteSalle(ActionEvent event) {
        // Call the method from SalleService to delete the Salle in the database
        SalleService salleService = new SalleService();
        try {
            salleService.deleteSalle(salle.getId());

            // Load Salle.fxml
            FXMLLoader salleLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/Salle.fxml"));
            Parent salleView = salleLoader.load();

            // Get the instance of DashController
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionReclamation/GestionSalle/Dash.fxml"));
            Parent dash = dashLoader.load();
            DashController dashController = dashLoader.getController();

            // Set Salle.fxml as the content of the AnchorPane in Dash.fxml
            dashController.getAnchor().getChildren().setAll(salleView);

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the loaded Dash.fxml
            Scene scene = new Scene(dash, currentStage.getWidth(), currentStage.getHeight());

            // Set the new scene to the current stage
            currentStage.setScene(scene);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void toggleGestionPane(ActionEvent event) {
        boolean isVisible = gestion.isVisible();
        gestion.setVisible(!isVisible);
        boolean isListVisible = list.isVisible();
        list.setVisible(!isListVisible);
        statique.setVisible(false);

    }

    @FXML
    void stat(ActionEvent event) {
        boolean isVisible = statique.isVisible();
        if (!isVisible) {
            list.setVisible(false);
            statique.setVisible(true);
            gestion.setVisible(false);
        } else {
            statique.setVisible(false);
            list.setVisible(true);
        }

        gestion.setVisible(false);

    }
}
