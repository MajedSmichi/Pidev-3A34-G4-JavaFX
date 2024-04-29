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
import java.util.stream.Collectors;

public class ActiviteController {

    @FXML
    private GridPane GridActivite;

    @FXML
    private Button showModifier;

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
    @FXML
    private Button supprimer;
    @FXML
    private AnchorPane modifier;
    @FXML
    private Button updateActivite;

    @FXML
    private TextField updatecoach;

    @FXML
    private TextField updatedescription;
    @FXML
    private DatePicker updatedate;

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
    private TextField saerch;

    @FXML
    private TableColumn<User, String> userNom;

    @FXML
    private TableColumn<User, String> numTel;

    @FXML
    private AnchorPane tabel;

    @FXML
    private TableView<User> listUser;



    public void initialize() {
        listUser.setPlaceholder(new Label("Aucune réservation n'est trouvé"));
        try {
            // Populate the ComboBox with the names of all Salle objects
            SalleService salleService = new SalleService();
            List<Salle> salles = salleService.getAllSalle();
            for (Salle salle : salles) {
                this.salle.getItems().add(salle.getNom());
                this.updatesalle.getItems().add(salle.getNom());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<Activite> activites = getData();
            populateGrid(activites);
            // Add a listener to the search TextField
            saerch.textProperty().addListener((observable, oldValue, newValue) -> {
                List<Activite> filteredActivites = activites.stream()
                        .filter(activite -> activite.getNom().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());
                try {
                    populateGrid(filteredActivites);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private  void populateGrid(List<Activite> activites) throws IOException {
        GridActivite.getChildren().clear();
        int row = 0;
        for (Activite activite : activites) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneActivite.fxml"));
            Pane pane = fxmlLoader.load();

            uneActiviteController oneActivite = fxmlLoader.getController();
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
       @FXML
    void addActivite(ActionEvent event) {

           // Check if the fields are empty
           if (nom.getText().isEmpty() || coach.getText().isEmpty() || date.getValue() == null ||
                   decription.getText().isEmpty() || nbrmax.getText().isEmpty() || salle.getValue() == null) {
               Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
               alert.showAndWait();
               return;
           }

           // Check if description is at least 15 characters
           if (decription.getText().length() < 15) {
               Alert alert = new Alert(Alert.AlertType.WARNING, "La description doit contenir au moins 15 caractères.");
               alert.showAndWait();
               return;
           }

           // Check if imagePath is not null
           if (imagePath == null) {
               Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une image.");
               alert.showAndWait();
               return;
           }
           // Check if the selected date is in the future
           if (date.getValue().isBefore(LocalDate.now())) {
               Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une date à venir.");
               alert.showAndWait();
               return;
           }

           // Check if nbrmax is a valid integer
           try {
               Integer.parseInt(nbrmax.getText());
           } catch (NumberFormatException e) {
               Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer un nombre valide pour le nombre maximum. des paricipants");
               alert.showAndWait();
               return;
           }

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
            modifier.setVisible(false);


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
                pane.setOnMouseClicked(event -> {
                    currentActivite = activite; // Update the currentActivite
                    displayActiviteDetails(activite); // Display the details of the currentActivite
                });
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
        detail.setVisible(false);
        modifier.setVisible(false);
        tabel.setVisible(false);
    }
    private Activite currentActivite;
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

    // Fetch the users related to the activite and populate the listUser TableView
    ActiviteService activiteService = new ActiviteService();
    try {
        List<User> users = activiteService.getUsersByActiviteId(activite.getId());
        populateUserTable(users);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Show the details AnchorPane
    detail.setVisible(true);
    tabel.setVisible(true);
    ajout.setVisible(false);
    modifier.setVisible(false);

}
    @FXML
    void deleteActivite(ActionEvent event) {
        // Use the currentActivite field instead of selectedActivite
        if (currentActivite != null) {
            ActiviteService activiteService = new ActiviteService();
            try {
                activiteService.deleteActivite(currentActivite.getId());
                refreshGridPane(); // Refresh the GridPane after deleting an Activite
                detail.setVisible(false); // Hide the detail AnchorPane
                currentActivite = null; // Clear the currentActivite field
                modifier.setVisible(false); // Hide the modifier AnchorPane

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
                refreshGridPane(); // Refresh the GridPane after updating the Activite
                modifier.setVisible(false); // Hide the modifier AnchorPane
                displayActiviteDetails(currentActivite); //

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

    private void populateUserTable(List<User> users) {
        // Assuming userNom and numTel are the TableColumn objects in your TableView
        userNom.setCellValueFactory(new PropertyValueFactory<>("nom")); // Replace "nom" with the actual property name in the User class
        numTel.setCellValueFactory(new PropertyValueFactory<>("numTele"));
        // Convert the List to an ObservableList and set it as the items of the TableView
        ObservableList<User> observableList = FXCollections.observableArrayList(users);
        listUser.setItems(observableList);
    }
            @FXML
    void exportToExcel(ActionEvent event) {
        List<User> users = listUser.getItems();
        ExcelExporter excelExporter = new ExcelExporter();

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("users.xlsx");

        // Show save file dialog
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            excelExporter.export(users, file.getAbsolutePath());
            System.out.println("Exported to Excel successfully!");
        }
    }

}