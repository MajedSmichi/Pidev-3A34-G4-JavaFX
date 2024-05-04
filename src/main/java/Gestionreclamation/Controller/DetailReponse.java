package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Services.ReponseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DetailReponse {
    @FXML
    private Label date;

    @FXML
    private Label description;

    @FXML
    private AnchorPane detailanchpane;

    @FXML
    private AnchorPane editreponse;

    @FXML
    private Label email;

    @FXML
    private Label etat;

    @FXML
    private Label fullname;

    @FXML
    private Label reponse;

    @FXML
    private Label sujet;

    @FXML
    private Label tele;
    private Reponse rep;

    public void setData(Reponse rep) {
        this.rep = rep;
        reponse.setText(rep.getReponse());
        Reclamation rec = rep.getidReclamation();
        fullname.setText(rec.getNom() + " " + rec.getPrenom()); // Assuming FullName is a Label
        email.setText(rec.getEmail());
        tele.setText(String.valueOf(rec.getNumTele()));
        etat.setText(rec.getEtat());
        sujet.setText(rec.getSujet());
        description.setText(rec.getDescription());
        date.setText(String.valueOf(rec.getDate()));

    }

    @FXML
    private void handleEditButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/EditReponse.fxml"));
        Parent editReponseView = loader.load();
        EditReponse editReponseController = loader.getController();
        editReponseController.setDetailReponseAnchorPane(editreponse);
        editReponseController.setData(rep);

        // Pass the detailanchpane instance to EditReponse controller
        editReponseController.setDetailAnchorPane(detailanchpane);

        editreponse.getChildren().setAll(editReponseView);
    }

    @FXML
    private void handleDeleteButtonClick() throws IOException {
        ReponseService reponseService = new ReponseService();
        reponseService.supprimerReponse(rep);
        detailanchpane.getChildren().clear();
        SampleController.getInstance().loadRep();

    }

}
