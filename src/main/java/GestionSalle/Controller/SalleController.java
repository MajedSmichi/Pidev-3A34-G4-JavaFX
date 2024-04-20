package GestionSalle.Controller;

import GestionSalle.Entity.Salle;
import GestionSalle.Services.SalleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SalleController implements Initializable {

    @FXML
    private GridPane GridSalle;


    @FXML
    private TextField addresse;

    @FXML
    private Button ajoutSalle;

    @FXML
    private TextField capacite;

    @FXML
    private TextArea description;

    @FXML
    private Button event_import;

    @FXML
    private AnchorPane gestion;

    @FXML
    private Button hideButton;

    @FXML
    private ScrollPane list;

    @FXML
    private ImageView logoSalle;

    @FXML
    private TextField name;

    @FXML
    private TextField nbrclients;

    @FXML
    private TextField numtel;

    @FXML
    private AnchorPane root1;

    @FXML
    private Button showButton;


    private List<Salle> salles = new ArrayList<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            salles = getData();
            System.out.println(salles);
            int columns = 0;
            int row = 1;
            for (Salle salle : salles) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneSalle.fxml"));
                Pane pane = fxmlLoader.load(); // Load as Pane
                uneSalleController oneSalle = fxmlLoader.getController();
                oneSalle.setData(salle);
                GridSalle.add(pane, columns, row); // Use the loaded Pane
                columns++;
                if (columns > 2) {
                    columns = 0;
                    row++;
                }
                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    private List<Salle> getData() throws SQLException {
        List<Salle> salleList = new ArrayList<>();
        SalleService salleService = new SalleService();

        // Assuming getAllSalle() returns a List<Salle>
        for (Salle salle : salleService.getAllSalle()) {
            Salle newSalle = new Salle();
            newSalle.setNom(salle.getNom());
            newSalle.setAddresse(salle.getAddresse());
            newSalle.setNum_tel(salle.getNum_tel());
            newSalle.setLogo_salle(salle.getLogo_salle());
            newSalle.setCapacite(salle.getCapacite());
            newSalle.setDescription(salle.getDescription());
            newSalle.setNbr_client(salle.getNbr_client());
            newSalle.setId(salle.getId());
            // Add more properties if available

            salleList.add(newSalle);
        }

        return salleList;
    }
    @FXML
    private void showGestionPane() {
        gestion.setVisible(true);
    }

    @FXML
    private void hideGestionPane() {
        gestion.setVisible(false);
    }

    @FXML
    void ajouterSalle(ActionEvent event) {
        Salle newSalle = new Salle();
        newSalle.setNom(name.getText());
        newSalle.setAddresse(addresse.getText());
        newSalle.setNum_tel(Integer.parseInt(numtel.getText()));
        newSalle.setCapacite(Integer.parseInt(capacite.getText()));
        newSalle.setDescription(description.getText());
        newSalle.setNbr_client(Integer.parseInt(nbrclients.getText()));
        // Assuming logoSalle is a String of image path
        newSalle.setLogo_salle(imagePath);
        SalleService salleService = new SalleService();
        try {
            salleService.addEvent(newSalle);
            hideGestionPane(); // Hide the AnchorPane gestion
            refreshGridPane(); // Refresh the GridPane after adding a new Salle
            // Get the instance of DashController
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Dash.fxml"));
            Parent dash = dashLoader.load();
            DashController dashController = dashLoader.getController();
            dashController.refreshSalle();


            // Clear the fields
            name.clear();
            addresse.clear();
            numtel.clear();
            capacite.clear();
            description.clear();
            nbrclients.clear();
            logoSalle.setImage(null); // Clear the image

        } catch (SQLException |IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshGridPane() throws SQLException {
        GridSalle.getChildren().clear(); // Clear the GridPane
        salles = getData(); // Get the updated data
        int columns = 0;
        int row = 1;
        for (Salle salle : salles) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneSalle.fxml"));
            try {
                Pane pane = fxmlLoader.load(); // Load as Pane
                uneSalleController oneSalle = fxmlLoader.getController();
                oneSalle.setData(salle);
                GridSalle.add(pane, columns, row); // Use the loaded Pane
                columns++;
                if (columns > 2) {
                    columns = 0;
                    row++;
                }
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
            logoSalle.setImage(image);
            imagePath = selectedFile.getPath(); // Store the path of the selected file

        }
    }

}