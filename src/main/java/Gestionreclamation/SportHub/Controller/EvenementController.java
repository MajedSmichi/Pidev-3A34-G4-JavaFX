package Gestionreclamation.SportHub.Controller;

import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Services.EvenementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

//import Tools.Statics;
import javafx.scene.Node;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;



public class EvenementController {


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
    private TextField nom_image;

    @FXML
    private AnchorPane root1;


    @FXML
    private TableColumn<Evenement,String> col_event_nom;

    @FXML
    private TableColumn<Evenement,String> col_event_lieu;

    @FXML
    private TableColumn<Evenement,String> col_event_date;

    @FXML
    private TableColumn<Evenement,String> col_event_description;

    @FXML
    private TableView<Evenement> eventTableView;


    //private File selectedFile;

    private File file = null;
    private Image image = null;
    private String url_image = null;

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

                // Refresh the TableView
                showListEvent();

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
            image = new Image(file.toURI().toString(), 218, 130, false, true);
            url_image = file.getName();
            event_image.setImage(image);
        }
    }



  /*  public void showListEvent() throws SQLException {
        EvenementService service = new EvenementService();
        ObservableList<Evenement> ob = FXCollections.observableList(service.getAllEvents());
        col_event_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_event_lieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        col_event_date.setCellValueFactory(new PropertyValueFactory<>("date_evenement"));
        col_event_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        eventTableView.setItems(ob);
    }
*/

    public void showListEvent() throws SQLException {
        EvenementService service = new EvenementService();
        List<Evenement> events = service.getAllEvents();

        ObservableList<Evenement> ob = FXCollections.observableArrayList();
        for (Evenement event : events) {
            ob.add(event);
        }

        col_event_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_event_lieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        col_event_date.setCellValueFactory(new PropertyValueFactory<>("dateEvenement"));
        col_event_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        eventTableView.setItems(ob);
    }


    public void eventDelete() throws SQLException {
        Evenement event=eventTableView.getSelectionModel().getSelectedItem();
        int num = eventTableView.getSelectionModel().getSelectedIndex();
        if((num - 1)<-1){return;}
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cofirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Event : " + event.getNom() + "?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {

            EvenementService service = new EvenementService();
            service.deleteEvent(event.getId());
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();
            showListEvent();



        }
    }

    public void eventUpdate() {
        Evenement event = eventTableView.getSelectionModel().getSelectedItem();
        int num = eventTableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) { return; }

        try {
            Alert alert;

            if (event_titre.getText().isEmpty() || event_description.getText().isEmpty() || event_lieu.getText().isEmpty() || event_date.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Les champs sont obligatoires");
                alert.showAndWait();
            } else {
                EvenementService service = new EvenementService();
                service.updateEvent(event.getId(), event_titre.getText(), event_description.getText(), event_lieu.getText(), Date.valueOf(event_date.getValue()), file != null ? file.getPath() : event.getImageEvenement());
                showListEvent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eventClear() {
        event_titre.setText("");
        event_description.setText("");
        event_lieu.setText("");
        event_date.setValue(null);
        event_image.setImage(null);
    }




    @FXML
    void initialize() throws SQLException {
        if (eventTableView != null) {
            showListEvent();

            // Add a change listener to the TableView's selection model
            eventTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Get the selected event
                    Evenement selectedEvent = newValue;

                    // Set the event details to the update fields
                    event_titre.setText(selectedEvent.getNom());
                    event_description.setText(selectedEvent.getDescription());
                    event_lieu.setText(selectedEvent.getLieu());
                    event_date.setValue(Instant.ofEpochMilli(selectedEvent.getDateEvenement().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
                    if (selectedEvent.getImageEvenement() != null) {
                        File file = new File(selectedEvent.getImageEvenement());
                        Image image = new Image(file.toURI().toString());
                        event_image.setImage(image);
                    }
                }
            });
        }
    }


}