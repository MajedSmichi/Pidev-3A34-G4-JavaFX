package GestionSalle.Controller;

import GestionSalle.Entity.Activite;
import GestionSalle.Services.SalleService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ActiviteCardController {

    @FXML
    private Label Date;

    @FXML
    private Pane PaneActivite;

    @FXML
    private Label Salle;

    @FXML
    private ImageView logo;

    @FXML
    private Label name;
    private Activite activite;

    public void setData(Activite activite) {
        this.activite = activite;
        name.setText(activite.getNom());
        SalleService salleService = new SalleService();
        try {
            GestionSalle.Entity.Salle salle = salleService.getSalleById(activite.getSalle_id());
            if (salle != null) {
                Salle.setText(salle.getNom());
            } else {
                Salle.setText("Salle not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(activite.getDate());
        Date.setText(formattedDate);

        try {
            logo.setImage(new Image(activite.getImage()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL or resource not found: " + activite.getImage());
        }
    }

}
