package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DetailReclamation {

    @FXML
    private Label date;

    @FXML
    private Button delete;

    @FXML
    private Label description;

    @FXML
    private Button edit;

    @FXML
    private Label email;

    @FXML
    private Label etat;

    @FXML
    private Label fullname;

    @FXML
    private Button reponse;

    @FXML
    private Label sujet;

    @FXML
    private Label tele;

    @FXML
    private AnchorPane editreponse;

    @FXML
    private AnchorPane detailanchpane;

    public void setReclamationController(ReclamationController controller) {
        this.reclamationController = controller;
    }


    private Reclamation rec; // Assuming Reclamation class exists
    ReclamationController reclamationController;


    public void setData(Reclamation rec) {
        this.rec = rec;
        // Populate UI elements with Reclamation data
        User c = rec.getUtilisateur();
        date.setText(String.valueOf(rec.getDate()));
        description.setText(rec.getDescription());
        email.setText(rec.getEmail());
        etat.setText(rec.getEtat());
        fullname.setText(rec.getNom() + "  " + rec.getPrenom());
        sujet.setText(rec.getSujet());
        tele.setText(String.valueOf(rec.getNumTele()));
    }

    @FXML
    private void handleEditButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/EditReclamation.fxml"));
        Parent editReclamationView = loader.load();
        Editreclamation editReclamationController = loader.getController();
        editReclamationController.setDetailAnchorPane(detailanchpane); // Pass reference here

        editReclamationController.setData(rec);

        editreponse.getChildren().setAll(editReclamationView);
    }

    @FXML
    private void handleSuprimerButtonClick() throws IOException {
        ReclamationService s = new ReclamationService();
        if (rec != null) {
            s.supprimerReclamation(rec);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Réclamation supprimée");
            alert.setHeaderText(null);
            alert.setContentText("La réclamation a été supprimée avec succès.");
            alert.showAndWait();

            detailanchpane.getChildren().clear();
            Dash.getInstance().loadRec();

        }
    }

    @FXML
    private void handleReponseButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/AjouterReponse.fxml"));
        Parent ajouterReponseView = loader.load();
        AjouterRepense ajouterRepenseController = loader.getController();
        ajouterRepenseController.setDetailAnchorPane(detailanchpane); // Pass reference here
        ajouterRepenseController.setData(rec);
        editreponse.getChildren().setAll(ajouterReponseView);

    }
}




