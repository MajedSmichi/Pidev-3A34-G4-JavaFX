package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class OneReponse {
    @FXML
    private Label Date;

    @FXML
    private Pane PaneReclamation;

    @FXML
    private Label name;

    @FXML
    private Label reponse;
    private Reponse rep;
    private ReponseController reponseController;

    public void setReponseController(ReponseController reponseController) {
        this.reponseController = reponseController;
    }

    public void setData(Reponse id) {
        this.rep = id;
        reponse.setText(rep.getReponse());
        Date.setText(String.valueOf(rep.getDate())); // Assuming Date is a Label
        Reclamation rec = rep.getidReclamation();
        if (rec != null) {
            name.setText(rec.getNom() + " " + rec.getPrenom()); // Assuming FullName is a Label
        } else {
            name.setText("Reclamation is null. Please ensure that the reclamation is set correctly.");
        }
    }


    @FXML
    private void handleReponseClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/DetailReponse.fxml"));
        Parent detailView = loader.load();
        DetailReponse detailController = loader.getController();
        detailController.setData(rep);
        BorderPane borderPane = reponseController.getBorderPane();
        borderPane.setCenter(detailView);
        borderPane.setTop(null);

    }
}