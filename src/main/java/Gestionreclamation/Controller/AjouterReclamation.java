package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.ReclamationService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import Gestionreclamation.Services.openIAsevice;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class AjouterReclamation implements Initializable {
    @FXML
    private Button Switch_to_speech;

    @FXML
    private DatePicker date;

    @FXML
    private TextField description;

    @FXML
    private ComboBox<String> sujet;

    @FXML
    private Button switch_to_text;
    @FXML
    private AnchorPane form_ajouter_speech;

    @FXML
    private AnchorPane form_ajouter_text;

    @FXML
    private AnchorPane form_switch_speech;
    @FXML
    private TextArea descriptionvoice;

    @FXML
    private TextField file;

    @FXML
    private Label sujetError;
    @FXML
    private Label dateError;

    @FXML
    private Label descriptionError;
    @FXML
    private Label fileError;
    @FXML
    private Label descriptionVoiceError;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        // Initialize the ComboBox items
        sujet.getItems().addAll("Salle", "Service");

        // Make descriptionvoice TextArea read-only
        descriptionvoice.setEditable(false);

        // Set the visibility of the descriptionVoiceError label to true
        descriptionVoiceError.setVisible(true);

        // Add a listener to the focusedProperty of the descriptionvoice TextArea
        descriptionvoice.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                descriptionVoiceError.setText("");

            } else {
                descriptionVoiceError.setText("You can only read");

            }
        });
    }


    @FXML
    public void sweechForm (ActionEvent event){
        TranslateTransition t = new TranslateTransition();
        if (event.getSource()== Switch_to_speech){
            t.setNode(form_switch_speech);
            t.setToX(400);
            t.setDuration(javafx.util.Duration.seconds(.3));
            form_ajouter_text.setVisible(false);
            form_ajouter_speech.setVisible(true);
            t.play();
            switch_to_text.setVisible(true);
            Switch_to_speech.setVisible(false);
            description.clear();
            date.getEditor().clear();
            sujet.setValue(null);
            dateError.setText("");
            sujetError.setText("");
            descriptionError.setText("");

        }
        else if (event.getSource()== switch_to_text){
            t.setNode(form_switch_speech);
            t.setToX(0);
            t.setDuration(javafx.util.Duration.seconds(.3));
            form_ajouter_speech.setVisible(false);
            form_ajouter_text.setVisible(true);
            t.play();
            switch_to_text.setVisible(false);
            Switch_to_speech.setVisible(true);
            descriptionvoice.clear();
            file.clear();
            descriptionVoiceError.setText("");
            fileError.setText("");
        }
    }
    @FXML
    void ajouterText(ActionEvent event) {
        boolean isValidInput = true;

        User user = new User();
        user.setId(1);
        user.setNom("iyed");
        user.setPrenom("wederni");
        user.setEmail("wed@gmail.com");
        user.setNumTele(12345678);
        user.setRole("admin");
        user.setMotDePass("12345678");
        user.setAdresse("tunis");

        Reclamation NewReclamation = new Reclamation();
        String descriptionValue = description.getText();
        String sujetValue = sujet.getValue();

        LocalDateTime dateValue = null;
        if (date.getValue() != null) {
            if (date.getValue().isBefore(LocalDate.now())) {
                dateError.setText("Date cannot be before the current date");
                isValidInput = false;
            } else {
                dateValue = date.getValue().atStartOfDay();
                dateError.setText("");
                NewReclamation.setDate(dateValue);
            }
        } else {
            dateError.setText("Date cannot be null");
            isValidInput = false;
        }

        if (descriptionValue == null || descriptionValue.trim().isEmpty()) {
            descriptionError.setText("Description cannot be empty");
            isValidInput = false;
        } else {
            descriptionError.setText("");
            NewReclamation.setDescription(descriptionValue);
        }

        if (sujetValue == null || sujetValue.trim().isEmpty()) {
            sujetError.setText("Sujet cannot be empty");
            isValidInput = false;
        } else {
            sujetError.setText("");
            NewReclamation.setSujet(sujetValue);
        }

        if (isValidInput) {
            NewReclamation.setEtat("En attente");
            NewReclamation.setNom(user.getNom());
            NewReclamation.setPrenom(user.getPrenom());
            NewReclamation.setEmail(user.getEmail());
            NewReclamation.setNumTele(user.getNumTele());
            NewReclamation.setUtilisateur(user);

            ReclamationService reclamationService = new ReclamationService();
            reclamationService.ajouterReclamation(NewReclamation);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Response Added");
            alert.setHeaderText(null);
            alert.setContentText("The response has been added successfully.");
            alert.showAndWait();

            description.clear();
            date.getEditor().clear();
            sujet.setValue(null);
        }
    }


    private openIAsevice openIAServiceInstance = new openIAsevice();

    @FXML
    void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            file.setText(selectedFile.getAbsolutePath());
            String transcription = openIAServiceInstance.executeService(selectedFile.getAbsolutePath());
                descriptionvoice.setText(transcription);
            }
        }
    @FXML
    void ajouterVoice(ActionEvent event) {
        boolean isValidInput = true;
        String fileValue = file.getText();

        if (fileValue == null || fileValue.trim().isEmpty()) {
            fileError.setText("File cannot be empty");
            isValidInput = false;
        } else {
            fileError.setText("");
        }

        if (isValidInput) {
            User user = new User();
            user.setId(1);
            user.setNom("iyed");
            user.setPrenom("wederni");
            user.setEmail("wed@gmail.com");
            user.setNumTele(12345678);
            user.setRole("admin");
            user.setMotDePass("12345678");
            user.setAdresse("tunis");

            Reclamation NewReclamation = new Reclamation();
            NewReclamation.setDate(LocalDateTime.now());
            NewReclamation.setDescription(descriptionvoice.getText());
            NewReclamation.setSujet("voix");
            NewReclamation.setEtat("En attente");
            NewReclamation.setNom(user.getNom());
            NewReclamation.setPrenom(user.getPrenom());
            NewReclamation.setEmail(user.getEmail());
            NewReclamation.setNumTele(user.getNumTele());
            NewReclamation.setUtilisateur(user);

            ReclamationService reclamationService = new ReclamationService();
            reclamationService.ajouterReclamation(NewReclamation);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("reclamation Added");
            alert.setHeaderText(null);
            alert.setContentText("The reclamation has been added successfully.");
            alert.showAndWait();

            descriptionvoice.clear();
            file.clear();
        }
    }

}

