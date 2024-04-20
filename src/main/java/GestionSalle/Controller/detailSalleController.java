package GestionSalle.Controller;

import GestionSalle.Entity.Salle;
import GestionSalle.Services.SalleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
    private TextArea description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button event_import;

    @FXML
    private AnchorPane gestion;

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

    private Salle salle;
    public void setData(Salle salle) {
        this.salle=salle;
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
        // Get the new values from the text fields
        String newName = name.getText();
        String newAddress = addresse.getText();
        int newNumTel = Integer.parseInt(numtel.getText());
        int newCapacite = Integer.parseInt(capacite.getText());
        String newDescription = description.getText();
        int newNbrClients = Integer.parseInt(nbrclients.getText());

        // If a new image has been imported, use it. Otherwise, keep the current image.
        String newLogoSalle = (imagePath != null) ? imagePath : salle.getLogo_salle();

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
            FXMLLoader salleLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Salle.fxml"));
            Parent salleView = salleLoader.load();

            // Get the instance of DashController
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Dash.fxml"));
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
            FXMLLoader salleLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Salle.fxml"));
            Parent salleView = salleLoader.load();

            // Get the instance of DashController
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Dash.fxml"));
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
    }
}
