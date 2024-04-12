package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Services.ReclamationService;
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

public class ReclamationController implements Initializable {
    @FXML
    private GridPane GridReclamation;

    public GridPane getGridReclamation() {
        return GridReclamation;
    }

    @FXML
    private BorderPane borderpanereclamation;
    private List<Reclamation> reclamations = new ArrayList<>();
    private Reclamation selectedReclamation;
    public BorderPane getBorderPane() {
        return borderpanereclamation;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            reclamations = getData();
            int columns = 0;
            int row = 1;
            for (Reclamation rec : reclamations) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/OneReclamation.fxml"));
                Pane pane = fxmlLoader.load(); // Charger en tant que Pane
                OneReclamation oneReclamation = fxmlLoader.getController();
                oneReclamation.setReclamationController(this); // Passer une référence de ReclamationController
                oneReclamation.setData(rec);

                GridReclamation.add(pane, columns, row); // Utiliser le Pane chargé
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
    }


    private List<Reclamation> getData() throws SQLException {
        List<Reclamation> reclamationList = new ArrayList<>();
        ReclamationService reclamationService = new ReclamationService();

        // Assuming afficherreclamation() returns a List<Reclamation>
        for (Reclamation rec : reclamationService.afficherreclamation()) {
            Reclamation newRec = new Reclamation();
            newRec.setId(rec.getId());
            newRec.setUtilisateur(rec.getUtilisateur());
            newRec.setSujet(rec.getSujet());
            newRec.setNom(rec.getNom());
            newRec.setPrenom(rec.getPrenom());
            newRec.setDate(rec.getDate());
            newRec.setDescription(rec.getDescription());
            newRec.setEmail(rec.getEmail());
            newRec.setNumTele(rec.getNumTele());
            newRec.setEtat(rec.getEtat());
            reclamationList.add(newRec);
        }

        return reclamationList;
    }
}

