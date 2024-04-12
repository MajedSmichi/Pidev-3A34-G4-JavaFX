package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Services.ReponseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EditReponse {
    @FXML
    private TextArea reponseedit;
    private AnchorPane editreponse; // Declare detailanchpane here
  private AnchorPane detailanchpane; // Declare detailanchpane here
    private Reponse rep;
    private ReponseService s = new ReponseService();
    private ReponseController reponseController;

    public void setReponseController(ReponseController reponseController) {
        this.reponseController = reponseController;
    }
    // Initialize the service here

    public void setDetailReponseAnchorPane(AnchorPane editreponse) {
        this.editreponse = editreponse;
    }
    public void setDetailAnchorPane(AnchorPane detailanchpane) {
        this.detailanchpane = detailanchpane;
    }
    public void setData(Reponse reponse) {
        this.rep = reponse;
        reponseedit.setText(reponse.getReponse());
    }

    @FXML
    public void handleEditButtonClick() throws IOException {
        if (s == null || rep == null) {
            System.out.println(rep);
            System.err.println("Service or Reponse is null");
            return;
        }

        rep.setReponse(reponseedit.getText());
        s.modifierReponse(rep);

        Alert a = new Alert(Alert.AlertType.INFORMATION, "votre reponse est modifier ");
        a.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/DetailReponse.fxml"));
        Parent detailReeponseView = loader.load();
        DetailReponse detailReponse = loader.getController();
        detailReponse.setData(rep); // Passer la réclamation modifiée

        // Remplacer le contenu de detailanchpane avec la vue de détail actualisée
        detailanchpane.getChildren().setAll(detailReeponseView);



    }
}