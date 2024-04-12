package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class AjouterRepense {
    @FXML
    private DatePicker datereponse;

    @FXML
    private TextArea textReponse;
    private Reclamation rec; // Assuming Reclamation class exists

    private AnchorPane detailanchpane; // Declare detailanchpane here

    public void setDetailAnchorPane(AnchorPane detailanchpane) {
        this.detailanchpane = detailanchpane;
    }
    public void setData(Reclamation rec) {
        this.rec = rec;
    }



    @FXML
    private void handleAjouterReponseButtonClick() throws Exception {
        // Create a new Reponse object
        Reponse newReponse = new Reponse();

        // Set the response text from the TextArea
        newReponse.setReponse(textReponse.getText());

        // Set the response date from the DatePicker
        newReponse.setDate(datereponse.getValue().atStartOfDay());

        // Set the reclamation for this response
        newReponse.setidReclamation(rec);
        User currentUser = rec.getUtilisateur(); // Get the user from the reclamation
        if(currentUser == null) {
            throw new Exception("User is null");
        }
        newReponse.setUtilisateur(currentUser);

        ReponseService reponseService = new ReponseService();
        reponseService.ajouterReponse(newReponse);

        // Show a confirmation message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Response Added");
        alert.setHeaderText(null);
        alert.setContentText("The response has been added successfully.");
        alert.showAndWait();
    }
}
