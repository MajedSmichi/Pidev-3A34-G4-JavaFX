package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.ReclamationService;
import Gestionreclamation.Services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AjouterRepense {
    @FXML
    private DatePicker datereponse;

    @FXML
    private TextArea textReponse;

    @FXML
    private Label DateError;

    @FXML
    private Label ReponseError;
    private Reclamation rec; // Assuming Reclamation class exists

    private AnchorPane detailanchpane; // Declare detailanchpane here

    public void setDetailAnchorPane(AnchorPane detailanchpane) {
        this.detailanchpane = detailanchpane;
    }
    public void setData(Reclamation rec) {
        this.rec = rec;
    }

    @FXML
    private void handleAjouterReponseButtonClick() {
        boolean isValidInput = true;

        // Create a new Reponse object
        Reponse newReponse = new Reponse();
        String descriptionValue = textReponse.getText();

        LocalDateTime dateValue = null;
        if (datereponse.getValue() != null) {
            if (datereponse.getValue().isBefore(LocalDate.now())) {
                DateError.setText("Date cannot be before the current date");
                isValidInput = false;
            } else {
                dateValue = datereponse.getValue().atStartOfDay();
                DateError.setText("");
                newReponse.setDate(dateValue);
            }
        } else {
            DateError.setText("Date cannot be null");
            isValidInput = false;
        }

        if (textReponse == null || textReponse.getText().trim().isEmpty()) {
            ReponseError.setText("Description cannot be empty");
            isValidInput = false;
        } else {
            ReponseError.setText("");
            newReponse.setReponse(descriptionValue);
        }

        if (isValidInput) {

            // Set the response text from the TextArea
            newReponse.setReponse(textReponse.getText());

            // Set the response date from the DatePicker
            newReponse.setDate(datereponse.getValue().atStartOfDay());

            // Set the reclamation for this response
            newReponse.setidReclamation(rec);
            User currentUser = rec.getUtilisateur(); // Get the user from the reclamation
            if(currentUser == null) {
                try {
                    throw new Exception("User is null");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            newReponse.setUtilisateur(currentUser);

            ReponseService reponseService = new ReponseService();
            reponseService.ajouterReponse(newReponse);

            rec.setEtat("Traité");
            ReclamationService reclamationService = new ReclamationService();
            reclamationService.modifierReclamation(rec);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Response Added");
            alert.setHeaderText(null);
            alert.setContentText("The response has been added successfully.");
            alert.showAndWait();
            try {
                Dash.getInstance().loadRep();
            } catch (IOException e) {
                e.printStackTrace();
            }
            detailanchpane.getChildren().clear();
        }
    }
}