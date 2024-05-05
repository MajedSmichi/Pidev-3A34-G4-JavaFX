package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Services.EvenementService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class EvenementBack {

    @FXML
    private GridPane eventContainerBack;

    @FXML
    private ScrollPane scrollPane;

    private EvenementService evenementService;

    @FXML
    private AnchorPane ajouterPane;

    @FXML
    private Button ajouterEvenement;

    @FXML
    private Button hide;


    @FXML
    private Button event_add;

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
    private TextField event_titre;
    @FXML
    private AnchorPane root1;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Pagination pagination;

    //private static final int ITEMS_PER_PAGE = 6;


    private TextField nom_image;
    private File file = null;
    private Image image = null;
    private String url_image = null;


    public void initialize() {
        evenementService = new EvenementService();

        // Bind the visibility of the hide button to the visibility of ajouterPane
        hide.visibleProperty().bind(ajouterPane.visibleProperty());

        ajouterPane.setVisible(false); // Make ajouterPane not visible

        ajouterEvenement.setOnAction(e -> {
            ajouterPane.setVisible(true);
        });
        hide.setOnAction(e -> {
            ajouterPane.setVisible(false);
        });
        try {
            List<Evenement> events = evenementService.getAllEvents();
            displayEvents(events);        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchEvents();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });



        sortComboBox.getItems().addAll("Sort by Name", "Sort by Lieu");
        sortComboBox.setPromptText("Sort");

        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Sort by Name":
                    try {
                        sortEventsByName();
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "Sort by Lieu":
                    try {
                        sortEventsByLieu();
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        });


    }

    @FXML
    private void sortEventsByName() throws SQLException, IOException {
        List<Evenement> events = evenementService.getAllEvents();
        events.sort(Comparator.comparing(Evenement::getNom)); // Sort events by name
        displayEvents(events);
    }

    @FXML
    private void sortEventsByLieu() throws SQLException, IOException {
        List<Evenement> events = evenementService.getAllEvents();
        events.sort(Comparator.comparing(Evenement::getLieu)); // Sort events by lieu
        displayEvents(events);
    }

@FXML
private void searchEvents() throws SQLException, IOException {
    String searchTerm = searchField.getText();
    List<Evenement> events = evenementService.searchEvents(searchTerm);
    eventContainerBack.getChildren().clear(); // Clear the GridPane
    if (events.isEmpty()) {
        Label label = new Label("Aucun événement ne correspond à votre recherche");
        label.getStyleClass().add("error-message"); // Add a style class to style the error message
        eventContainerBack.add(label, 0, 0);
    } else {
        displayEvents(events);
    }
}

private void displayEvents(List<Evenement> events) throws SQLException, IOException {
    int itemsPerPage = 6; // Set the number of items per page
    int totalItems = events.size(); // Get the total number of items
    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage); // Calculate the total number of pages

    pagination.setPageCount(totalPages); // Set the total number of pages

    pagination.setPageFactory((pageIndex) -> {
        int fromIndex = pageIndex * itemsPerPage; // Calculate the index of the first item on the current page
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems); // Calculate the index of the last item on the current page

        GridPane gridPane = new GridPane(); // Create a new GridPane
        gridPane.setHgap(10); // Set horizontal gap
        gridPane.setVgap(10); // Set vertical gap

        for (int i = fromIndex; i < toIndex; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Gestionreclamation/SportHub/EvenementCard.fxml"));
                Pane pane = fxmlLoader.load();

                EvenementCard controller = fxmlLoader.getController();
                controller.setData(events.get(i));

                pane.setOnMouseClicked(e -> {
                    try {
                        openDetails(controller.getEvent());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                gridPane.add(pane, i % 3, i / 3); // Add the event to the GridPane
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Remove all children of the eventContainerBack GridPane that are not the pagination control
        eventContainerBack.getChildren().removeIf(node -> node != pagination);

        // Add the created GridPane to the eventContainerBack GridPane
        eventContainerBack.getChildren().add(0, gridPane); // Add the gridPane at the first position

        return new ScrollPane(gridPane); // Return the GridPane wrapped in a ScrollPane as the page content
    });
}


public void eventAdd() {
        try {
            Alert alert;

            if (event_titre.getText().isEmpty()
                    || event_description.getText().isEmpty() || event_lieu.getText().isEmpty()
                    || event_date.getValue() == null
            ) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Les champs sont obligatoires");
                alert.showAndWait();
            } else if (event_date.getValue().isBefore(LocalDate.now())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Date doit être supérieure à la date actuelle");
                alert.showAndWait();
            } else if (evenementService.eventExists(event_titre.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Evènement nom déjà existe, merci de le changer");
                alert.showAndWait();
            } else {

                Evenement event = new Evenement();
                event.setNom(event_titre.getText());
                event.setDescription(event_description.getText());
                event.setLieu(event_lieu.getText());
                event.setDateEvenement(Date.valueOf(event_date.getValue()));

                if (file != null) {
                    event.setImageEvenement(file.getPath()); // Set the image path
                }

                EvenementService service = new EvenementService();
                service.addEvent(event);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                List<Evenement> events = evenementService.getAllEvents();
                displayEvents(events);        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void importImage() {
        FileChooser open = new FileChooser();
        file = open.showOpenDialog(root1.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString(), 270, 310, false, true);
            url_image = file.getName();
            event_image.setImage(image);
        }
    }

    private void openDetails(Evenement event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/SportHub/DetailsEvenementBack.fxml"));
        Parent detailsRoot = fxmlLoader.load();
        DetailsEvenementBack controller = fxmlLoader.getController();
        controller.setEvent(event);
        root1.getChildren().setAll(detailsRoot);
    }


}