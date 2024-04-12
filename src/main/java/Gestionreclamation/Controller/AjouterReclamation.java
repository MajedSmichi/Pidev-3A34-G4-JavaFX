package Gestionreclamation.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import Gestionreclamation.Services.openIAsevice;
import javafx.stage.FileChooser;

import java.io.File;


public class AjouterReclamation {
    @FXML
    private Button Switch_to_speech;

    @FXML
    private DatePicker date;

    @FXML
    private TextField description;

    @FXML
    private ComboBox<?> sujet;

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
    }

