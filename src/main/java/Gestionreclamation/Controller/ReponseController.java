package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Services.ReponseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReponseController implements Initializable {
    @FXML
    private GridPane GridReponse;

    @FXML
    private BorderPane borderpaneReponse;
    private List<Reponse> reponses = new ArrayList<>();
    public BorderPane getBorderPane() {
        return borderpaneReponse;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            reponses = getData();
            int columns = 0;
            int row = 1;
            for (Reponse rep : reponses) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/OneReponse.fxml"));
                Pane pane = fxmlLoader.load(); // Load as Pane
                OneReponse oneReponse = fxmlLoader.getController();
                oneReponse.setData(rep);
                oneReponse.setReponseController(this); // Set the ReponseController instance

                GridReponse.add(pane, columns, row); // Use the loaded Pane
                columns++;
                if (columns > 2) {
                    columns = 0;
                    row++;
                }
                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }    private List<Reponse> getData() throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        ReponseService reponseService = new ReponseService();
        for (Reponse rep : reponseService.afficherReponse()) {
            Reponse newRep = new Reponse();
            newRep.setId(rep.getId());
            newRep.setUtilisateur(rep.getUtilisateur());
            newRep.setReponse(rep.getReponse());
            newRep.setDate(rep.getDate());
            newRep.setidReclamation(rep.getidReclamation());
            reponseList.add(newRep);
        }
        return reponseList;
    }
}