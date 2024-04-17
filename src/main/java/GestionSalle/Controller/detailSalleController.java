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
    private Button delete;

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

    private String imagePath = null;
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
        
    }
    @FXML
    void deleteSalle(ActionEvent event) {
        // Call the method from SalleService to delete the Salle in the database
        SalleService salleService = new SalleService();
        try {
            salleService.deleteSalle(salle.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Navigate to Salle.fxml
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Salle.fxml"));
            Parent salleParent = fxmlLoader.load();
            Scene salleScene = new Scene(salleParent);

            // Get the current stage
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(salleScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
