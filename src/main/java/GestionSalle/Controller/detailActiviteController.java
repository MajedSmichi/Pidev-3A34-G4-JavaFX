package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import GestionSalle.Entity.Salle;
import GestionSalle.Entity.User;
import GestionSalle.Services.ActiviteService;
import GestionSalle.Services.SalleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class detailActiviteController {

    @FXML
    private Label coachact;

    @FXML
    private Label dateact;

    @FXML
    private Label descriptionact;

    @FXML
    private AnchorPane detail;

    @FXML
    private ImageView imageact;

    @FXML
    private AnchorPane modifier;

    @FXML
    private Label nameact;

    @FXML
    private Label nbrmaxact;

    @FXML
    private Label salleact;

    @FXML
    private Button showModifier;

    @FXML
    private Button supprimer;

    @FXML
    private Button updateActivite;

    @FXML
    private TextField updatecoach;

    @FXML
    private DatePicker updatedate;

    @FXML
    private TextField updatedescription;

    @FXML
    private Button updateimage;

    @FXML
    private TextField updatenbrmax;

    @FXML
    private TextField updatenom;

    @FXML
    private ComboBox<String> updatesalle;

    @FXML
    private ImageView updimage;

    @FXML
    private TableColumn<User, String> userNom;

    @FXML
    private TableColumn<User, String> numTel;

    @FXML
    private AnchorPane tabel;

    @FXML
    private TableView<User> listUser;
    private Activite activite;

    private Activite currentActivite;
    public void setCurrentActivite(Activite activite) {
    this.currentActivite = activite;
}

    @FXML
    void deleteActivite(ActionEvent event) {
        if (currentActivite != null) {
            ActiviteService activiteService = new ActiviteService();
            try {
                activiteService.deleteActivite(currentActivite.getId());

                // Load Salle.fxml
                FXMLLoader salleLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Salle.fxml"));
                Parent salleView = salleLoader.load();

                // Get the current scene
                Scene currentScene = ((Node) event.getSource()).getScene();

                // Find the AnchorPane in the current scene
                AnchorPane anchorPane = (AnchorPane) currentScene.lookup("#anchor");

                // Set Salle.fxml as the content of the AnchorPane
                anchorPane.getChildren().setAll(salleView);

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case where no Activite is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No Activite selected!");
            alert.showAndWait();
        }
    }
    @FXML
    void showUpdate(ActionEvent event) {
        // Assuming 'modifier' is the AnchorPane you want to show
        boolean isVisible = modifier.isVisible();
        modifier.setVisible(!isVisible);
        boolean is = tabel.isVisible();
        tabel.setVisible(!is);

        // If the modifier AnchorPane is being shown, set the fields to the values of the currentActivite
        if (!isVisible && currentActivite != null) {
            updatenom.setText(currentActivite.getNom());
            updatecoach.setText(currentActivite.getCoach());
            updatedescription.setText(currentActivite.getDescription());
            updatenbrmax.setText(String.valueOf(currentActivite.getNbr_max()));
            updatedate.setValue(currentActivite.getDate().toLocalDateTime().toLocalDate()); // Set the DatePicker value

            // Assuming 'updatesalle' is a ComboBox that should be set to the name of the Salle of the currentActivite
            SalleService salleService = new SalleService();
            try {
                Salle salle = salleService.getSalleById(currentActivite.getSalle_id());
                if (salle != null) {
                    updatesalle.setValue(salle.getNom());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Assuming 'updimage' is an ImageView that should be set to the image of the currentActivite
            try {
                updimage.setImage(new Image(currentActivite.getImage()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid URL or resource not found: " + currentActivite.getImage());
            }
        }
    }

    @FXML
    void updateActivite(ActionEvent event) {

        if (currentActivite != null) {
            // Check if the fields are empty
            if (updatenom.getText().isEmpty() || updatecoach.getText().isEmpty() || updatedate.getValue() == null ||
                    updatedescription.getText().isEmpty() || updatenbrmax.getText().isEmpty() || updatesalle.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            // Check if description is at least 15 characters
            if (updatedescription.getText().length() < 15) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "La description doit contenir au moins 15 caractères.");
                alert.showAndWait();
                return;
            }


            // Check if the selected date is in the future
            if (updatedate.getValue().isBefore(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une date à venir.");
                alert.showAndWait();
                return;
            }

            // Check if nbrmax is a valid integer
            try {
                Integer.parseInt(updatenbrmax.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer un nombre valide pour le nombre maximum des paricipants");
                alert.showAndWait();
                return;
            }
            // Update the currentActivite with the values from the modifier AnchorPane
            currentActivite.setNom(updatenom.getText());
            currentActivite.setCoach(updatecoach.getText());
            currentActivite.setDescription(updatedescription.getText());
            currentActivite.setNbr_max(Integer.parseInt(updatenbrmax.getText()));
            currentActivite.setDate(java.sql.Timestamp.valueOf(updatedate.getValue().atStartOfDay()));
            if (imagePath != null) {
                currentActivite.setImage(imagePath);
            }

            // Assuming 'updatesalle' is a ComboBox that should be set to the name of the Salle of the currentActivite
            SalleService salleService = new SalleService();
            try {
                Salle salle = salleService.getSalleByName(updatesalle.getValue());
                if (salle != null) {
                    currentActivite.setSalle_id(salle.getId());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Update the currentActivite in the database
            ActiviteService activiteService = new ActiviteService();
            try {
                activiteService.updateActivite(currentActivite);

                modifier.setVisible(false); // Hide the modifier AnchorPane
                setData(currentActivite); // Update the detail AnchorPane with the new values


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case where no Activite is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("No Activite selected!");
            alert.showAndWait();
        }
    }


    @FXML
    void updateimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            updimage.setImage(image); // Set the ImageView in the modifier AnchorPane
            imagePath = selectedFile.getPath(); // Store the image path
        }
    }

    public void setData(Activite activite) {
        nameact.setText(activite.getNom());
        descriptionact.setText(activite.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        dateact.setText(formattedDate);
        nbrmaxact.setText(String.valueOf(activite.getNbr_max()));
        coachact.setText(activite.getCoach());
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
        listUser.setPlaceholder(new Label("Aucun utilisateur trouvé"));


        // Récupérer la liste des utilisateurs associés à l'activité
        ActiviteService activiteService = new ActiviteService();
        try {
            List<User> users = activiteService.getUsersByActiviteId(activite.getId());

            // Peupler le TableView avec la liste des utilisateurs
            populateUserTable(users);
        } catch (SQLException e) {
            e.printStackTrace();
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
            this.updimage.setImage(image); // Assuming 'image' is the ImageView in your ActiviteController
            imagePath = selectedFile.getPath(); // Assuming 'imagePath' is a field in your ActiviteController
        }
    }

    private void populateUserTable(List<User> users) {
        // Assuming userNom and numTel are the TableColumn objects in your TableView
        userNom.setCellValueFactory(new PropertyValueFactory<>("nom")); // Replace "nom" with the actual property name in the User class
        numTel.setCellValueFactory(new PropertyValueFactory<>("numTele"));
        // Convert the List to an ObservableList and set it as the items of the TableView
        ObservableList<User> observableList = FXCollections.observableArrayList(users);
        listUser.setItems(observableList);
    }

}
