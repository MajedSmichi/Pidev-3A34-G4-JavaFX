package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Services.EvenementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class DetailsEvenementBack {




    @FXML
    private Label Date;

    @FXML
    private Label Description;

    @FXML
    private Label Lieu;

    @FXML
    private Pane PaneDetail;

    @FXML
    private Button event_clear;

    @FXML
    private DatePicker event_date;

    @FXML
    private TextArea event_description;

    @FXML
    private ImageView event_image;

    @FXML
    private Button event_import;

    @FXML
    private TextField event_lieu;

    @FXML
    private TextField event_nom;

    @FXML
    private Button event_update;

    @FXML
    private Button hide;

    @FXML
    private ImageView imageEvnement;

    @FXML
    private Button modifier;

    @FXML
    private AnchorPane modifierPane;

    @FXML
    private ImageView modifiericon;

    @FXML
    private ImageView modifiericon1;

    @FXML
    private Label name;

    @FXML
    private AnchorPane root2;

    @FXML
    private ImageView suppicon;

    @FXML
    private Button supprimer;

    private TextField nom_image;
    private File file = null;
    private Image image = null;
    private String url_image = null;
    private Evenement event;

    @FXML
    void importImage(MouseEvent event) {

    }

    public void initialize() {
        // Other initialization code...

        // Initially set the modifierPane to not visible
        modifierPane.setVisible(false);

        // Add a click listener to the modifiericon1
        modifiericon1.setOnMouseClicked(e -> {
            modifierPane.setVisible(true);

            // Set the text of the fields in the modifierPane to the text of the corresponding labels in the PaneDetail
            event_nom.setText(name.getText());
            event_lieu.setText(Lieu.getText());
            event_description.setText(Description.getText());

            // Convert the date from the label to a LocalDate and set it in the DatePicker
            try {
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Date.getText());
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                event_date.setValue(localDate);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            // Set the image in the ImageView
            event_image.setImage(imageEvnement.getImage());
        });

        // Add a click listener to the hide button
        hide.setOnMouseClicked(e -> {
            modifierPane.setVisible(false);
        });
    }

public void setEvent(Evenement event) {
    this.event = event; // This line sets the event object in the DetailsEvenementBack class
    name.setText(event.getNom());
    Date.setText(event.getDateEvenement().toString());
    Lieu.setText(event.getLieu());
    Description.setText(event.getDescription());
    // Load the image
    File file = new File(event.getImageEvenement());
    Image image = new Image(file.toURI().toString());
    imageEvnement.setImage(image);
}

    @FXML
    void eventClear() {
        event_nom.setText("");
        event_description.setText("");
        event_lieu.setText("");
        event_date.setValue(null);
        event_image.setImage(null);
    }


@FXML
void eventUpdate1() {
    // Step 1: Get the values from the text fields and the date picker
    String nom = event_nom.getText();
    String description = event_description.getText();
    String lieu = event_lieu.getText();
    LocalDate localDate = event_date.getValue();
    java.util.Date utilDate = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    java.sql.Date date = new java.sql.Date(utilDate.getTime());

    // If no new image is uploaded, use the existing image of the event
    String image = (url_image != null) ? url_image : event.getImageEvenement();

    // Check if the event object is null
    if (event == null) {
        // Initialize the event object
        event = new Evenement();
    }

    // Step 2: Create a new Evenement object
    Evenement updatedEvent = new Evenement(event.getId(), nom, description, lieu, date, image);

    // Step 3: Call the updateEvent method from EvenementService
    EvenementService evenementService = new EvenementService();
    try {
        evenementService.updateEvent(updatedEvent.getId(), updatedEvent.getNom(), updatedEvent.getDescription(), updatedEvent.getLieu(), updatedEvent.getDateEvenement(), updatedEvent.getImageEvenement());
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Step 4: Update the labels in the PaneDetail
    name.setText(updatedEvent.getNom());
    Date.setText(updatedEvent.getDateEvenement().toString());
    Lieu.setText(updatedEvent.getLieu());
    Description.setText(updatedEvent.getDescription());

    // Step 5: Update the imageEvnement ImageView
    if (updatedEvent.getImageEvenement() != null) {
        File file = new File(updatedEvent.getImageEvenement());
        Image newImage = new Image(file.toURI().toString());
        imageEvnement.setImage(newImage);
    } else {
        // Handle the situation where updatedEvent.getImageEvenement() is null
        // For example, you could set imageEvnement to a default image
    }

    // Step 6: Hide the modifier pane
    modifierPane.setVisible(false);
}

@FXML
void importImage1() {
    System.out.println("importImage method called");

    FileChooser open = new FileChooser();
    file = open.showOpenDialog(root2.getScene().getWindow());

    if (file != null) {
        System.out.println("File selected: " + file.getAbsolutePath());

        image = new Image(file.toURI().toString(), 270, 310, false, true);
        url_image = file.getAbsolutePath(); // get the full path of the file
        event_image.setImage(image);
    } else {
        System.out.println("No file selected");
        url_image = null;
    }
}

@FXML
void deleteEvent() {
    // Step 1: Check if the event object is null
    if (event == null) {
        System.out.println("No event selected");
        return;
    }

    // Step 2: Create a confirmation dialog
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Message");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to DELETE Event : " + event.getNom() + "?");

    // Step 3: Show the confirmation dialog and wait for the user's response
    Optional<ButtonType> option = alert.showAndWait();

    // Step 4: If the user clicked OK, delete the event
    if (option.get().equals(ButtonType.OK)) {
        // Call the deleteEvent method from EvenementService
        EvenementService evenementService = new EvenementService();
        try {
            evenementService.deleteEvent(event.getId());

            // Show a success message
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();

            // Step 5: Update the UI
            // Refresh the page to EvenementBack
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/SportHub/EvenementBack.fxml"));
                root2.getChildren().setAll(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
