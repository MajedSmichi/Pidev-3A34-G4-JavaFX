package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Services.EvenementService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class EvenementBack {

    @FXML
    private GridPane eventContainerBack;

    @FXML
    private Button ajouterEvenement;


    public void initialize() {

        ajouterEvenement.setOnAction(e -> {
            showAddEventDialog();
        });

        try {
            List<Evenement> events = fetchEventsFromDatabase();

            int column = 0;
            int row = 0;

            for (Evenement event : events) {
                GridPane eventCard = createEventCard(event);
                eventContainerBack.add(eventCard, column, row);

                column++;
                if (column > 2) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private GridPane createEventCard(Evenement event) {
        GridPane eventCard = new GridPane();
        eventCard.getStyleClass().add("card"); // Add the style class

        // Create an ImageView and load the image from the file path
        ImageView eventImage = new ImageView();
        Image image = new Image("file:" + event.getImageEvenement());
        eventImage.setImage(image);
        eventImage.setFitWidth(190);  // Set the width of the ImageView
        eventImage.setFitHeight(220);
        eventImage.setPreserveRatio(true);  // Preserve the aspect ratio
        eventCard.add(eventImage, 0, 0);

        Label eventName = new Label(event.getNom());
        eventName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        eventCard.add(eventName, 0, 1);

        Label eventDate = new Label(event.getDateEvenement().toString());
        eventDate.setStyle("-fx-font-size: 14;");
        eventCard.add(eventDate, 0, 2);

        // Add an event handler to the card
        eventCard.setOnMouseClicked(e -> {
            // Create and display the detailed card
            GridPane detailedCard = createDetailedCard(event);
            eventContainerBack.getChildren().clear(); // Clear the event container
            eventContainerBack.add(detailedCard, 0, 0); // Add the detailed card to the event container
        });

        return eventCard;
    }

    private GridPane createDetailedCard(Evenement event) {
        GridPane detailedCard = new GridPane();
        detailedCard.getStyleClass().add("detailed-card"); // Add the style class

        // Create an ImageView and load the image from the file path
        ImageView eventImage = new ImageView();
        Image image = new Image("file:" + event.getImageEvenement());
        eventImage.setImage(image);
        eventImage.setFitWidth(300);  // Set the width of the ImageView
        eventImage.setFitHeight(450);
        eventImage.setPreserveRatio(true);  // Preserve the aspect ratio
        detailedCard.add(eventImage, 0, 0);

        Label eventName = new Label("Event Name: " + event.getNom());
        eventName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        detailedCard.add(eventName, 0, 1);

        Label eventDate = new Label("Event Date: " + event.getDateEvenement().toString());
        eventDate.setStyle("-fx-font-size: 14;");
        detailedCard.add(eventDate, 0, 2);

        Label eventLieu = new Label("Event Lieu: " + event.getLieu());
        eventDate.setStyle("-fx-font-size: 14;");
        detailedCard.add(eventLieu, 0, 3);

        Label eventDescription = new Label("Event Description: " + event.getDescription());
        eventDescription.setStyle("-fx-font-size: 14;");
        detailedCard.add(eventDescription, 0, 4);




        // Create a back button with text
        Button backButton = new Button("Back");

        // Add an event handler to the back button
        backButton.setOnAction(e -> {
            try {
                // Clear the event container
                eventContainerBack.getChildren().clear();

                // Repopulate the event container with the event cards
                List<Evenement> events = fetchEventsFromDatabase();
                int column = 0;
                int row = 0;
                for (Evenement ev : events) {
                    GridPane eventCard = createEventCard(ev);
                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                    eventContainerBack.add(eventCard, column++, row);
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        });

        // Add the back button to the detailed card
        detailedCard.add(backButton, 1, 6);

        return detailedCard;
    }





private void showAddEventDialog() {
    // Create a custom dialog.
    Dialog<Evenement> dialog = new Dialog<>();
    dialog.setTitle("Add Event");
    dialog.getDialogPane().setPrefSize(600, 600); // Set the preferred size of the dialog
    dialog.getDialogPane().getStylesheets().add(getClass().getResource("/SportHub/Back.css").toExternalForm()); // Add the CSS file
    // Set the button types.
    ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

    // Create the labels and fields.
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setAlignment(Pos.CENTER); // Center the content

    grid.getStyleClass().add("white-bg"); // Add the style class from dashboardDesign.css

    TextField eventName = new TextField();
    eventName.getStyleClass().add("textfield"); // Add the style class from dashboardDesign.css
    TextArea eventDescription = new TextArea();
    eventDescription.getStyleClass().add("textfield"); // Add the style class from dashboardDesign.css
    TextField eventLieu = new TextField();
    eventLieu.getStyleClass().add("textfield"); // Add the style class from dashboardDesign.css
    DatePicker eventDate = new DatePicker();
    eventDate.getStyleClass().add("textfield"); // Add the style class from dashboardDesign.css
    Button eventImageButton = new Button("Choose Image");
    eventImageButton.getStyleClass().add("add-btn"); // Add the style class from dashboardDesign.css
    Label eventImageLabel = new Label();
    ImageView eventImageView = new ImageView();
    eventImageView.setFitWidth(180);  // Set the width of the ImageView
    eventImageView.setFitHeight(220); // Set the height of the ImageView
    eventImageView.setPreserveRatio(true);  // Preserve the aspect ratio


    grid.add(new Label("Image:"), 0, 0);
    grid.add(eventImageButton, 1, 0);
    grid.add(eventImageLabel, 2, 0);
    grid.add(eventImageView, 1, 1); // Add the ImageView to the grid
    grid.add(new Label("Name:"), 0, 2);
    grid.add(eventName, 1, 2);
    grid.add(new Label("Lieu:"), 0, 3);
    grid.add(eventLieu, 1, 3);
    grid.add(new Label("Date:"), 0, 4);
    grid.add(eventDate, 1, 4);
    grid.add(new Label("Description:"), 0, 5);
    grid.add(eventDescription, 1, 5);

    // Create a FileChooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

    // Set an action for the eventImageButton
    eventImageButton.setOnAction(e -> {
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
           // eventImageLabel.setText(selectedFile.getAbsolutePath());
            Image image = new Image("file:" + selectedFile.getAbsolutePath());
            eventImageView.setImage(image); // Update the ImageView with the selected image
        }
    });

    dialog.getDialogPane().setContent(grid);

    // Convert the result to an Evenement object when the Add button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == loginButtonType) {
            return new Evenement(eventName.getText(), eventDescription.getText(), eventLieu.getText(), java.sql.Date.valueOf(eventDate.getValue()), eventImageLabel.getText());
        }
        return null;
    });

    // Show the dialog and handle the result
    dialog.showAndWait().ifPresent(event -> {
        try {
            new EvenementService().addEvent(event);
            // Refresh the event container
            eventContainerBack.getChildren().clear();
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}


    private List<Evenement> fetchEventsFromDatabase() throws SQLException {
        EvenementService service = new EvenementService();
        return service.getAllEvents();
    }

}