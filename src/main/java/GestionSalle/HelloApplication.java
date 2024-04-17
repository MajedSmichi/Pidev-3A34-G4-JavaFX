package GestionSalle;
import GestionSalle.Entity.Activite;
import GestionSalle.Services.ActiviteService;
import GestionSalle.Services.SalleService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


    }


    public static void main(String[] args) {
        ActiviteService activiteService = new ActiviteService();
        Activite activiteToUpdate = new Activite();
        activiteToUpdate.setId(1); // Set the id to the id of the Activite you want to update
        activiteToUpdate.setSalle_id(1); // Set the salle_id to an existing salle id
        activiteToUpdate.setNom("Updated Activite");
        activiteToUpdate.setDate(new java.util.Date()); // Set the date to the current date
        activiteToUpdate.setNbr_max(100);
        activiteToUpdate.setDescription("This is an updated activite");
        activiteToUpdate.setImage("path_to_updated_image"); // Set the image to the path of the updated image
        activiteToUpdate.setCoach("Updated Coach Name"); // Set the updated coach name
        try {
            activiteService.updateActivite(activiteToUpdate);
            System.out.println("Activite updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
