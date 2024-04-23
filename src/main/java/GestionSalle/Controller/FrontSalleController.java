package GestionSalle.Controller;

import GestionSalle.Entity.Salle;
import GestionSalle.Services.SalleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontSalleController {
    @FXML
    private GridPane GridSalleFront;

    public void initialize() {

        try {
            displaySalles();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void displaySalles() throws SQLException, IOException {
        SalleService salleService = new SalleService();
        List<Salle> salles = salleService.getAllSalle();

        int row = 1;
        int column = 0;

        for (Salle salle : salles) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionSalle/SalleCard.fxml"));
            Pane salleCard = loader.load();
            SalleCardController controller = loader.getController();
            controller.setData(salle);
            GridSalleFront.add(salleCard, column++, row); // add to gridpane
            if (column > 2) {
                column = 0;
                row++;
            }
        }
    }
}