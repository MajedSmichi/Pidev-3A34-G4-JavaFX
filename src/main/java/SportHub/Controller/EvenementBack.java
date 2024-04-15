package SportHub.Controller;

import SportHub.Entity.Evenement;
import SportHub.Services.EvenementService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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

    private TextField nom_image;
    private File file = null;
    private Image image = null;
    private String url_image = null;


    public void initialize() {
        evenementService = new EvenementService();
        ajouterPane.setVisible(false); // Make ajouterPane not visible
        ajouterEvenement.setOnAction(e -> {
            ajouterPane.setVisible(true);
        });
        hide.setOnAction(e -> {
            ajouterPane.setVisible(false);
        });
        try {
            displayEvents();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void displayEvents() throws SQLException, IOException {
        List<Evenement> events = evenementService.getAllEvents();
        for (int i = 0; i < events.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
fxmlLoader.setLocation(getClass().getResource("/SportHub/EvenementCard.fxml"));            Pane pane = fxmlLoader.load();

            EvenementCard controller = fxmlLoader.getController();
            controller.setData(events.get(i));

            eventContainerBack.add(pane, i % 3, i / 3);
        }
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


            displayEvents();
/*
                Parent fmxlLoader = FXMLLoader.load(getClass().getResource("/GUI/Back/evenement.fxml"));
                root1.getChildren().removeAll();
                root1.getChildren().setAll(fmxlLoader);
*/
            }
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

}