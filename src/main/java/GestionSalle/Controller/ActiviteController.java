package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import GestionSalle.Services.ActiviteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ActiviteController {

    @FXML
    private GridPane GridActivite;

    public void initialize() {
        try {
            List<Activite> activites = getData();
            int row = 0;
            for (Activite activite : activites) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneActivite.fxml"));
                Pane pane = fxmlLoader.load();
                uneActiviteController oneActivite = fxmlLoader.getController();
                oneActivite.setData(activite);
                GridActivite.add(pane, 0, row); // Always add to the first column
                row++;
                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Activite> getData() throws SQLException {
        ActiviteService activiteService = new ActiviteService();
        return activiteService.getAllActivite();
    }
}