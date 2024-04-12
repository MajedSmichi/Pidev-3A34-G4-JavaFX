package Gestionreclamation.Controller;
import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;

public class OneReclamation {

    @FXML
    private Label Date;

    @FXML
    private Button Detail;

    @FXML
    private Label name;
    @FXML
    private Pane PaneReclamation;

    @FXML
    private Label Sujet;

    private Reclamation rec;
    private ReclamationController reclamationController;




    public void setReclamationController(ReclamationController reclamationController) {
        this.reclamationController = reclamationController;
    }


    public void setData(Reclamation rec) {
        this.rec = rec;
        Sujet.setText(rec.getSujet());
        Date.setText(String.valueOf(rec.getDate())); // Assuming Date is a Label
        name.setText(rec.getNom() + " " + rec.getPrenom()); // Assuming FullName is a Label
    }
    @FXML
    private void handleReclamationClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/DetailReclamation.fxml"));
        Parent detailView = loader.load();
        DetailReclamation detailController = loader.getController();
        detailController.setData(rec);

        // Accessing the BorderPane from the OneReclamationController
        BorderPane borderPane = reclamationController.getBorderPane();
        borderPane.setCenter(null);
        borderPane.setTop(detailView);//
        borderPane.setRight(null);// Add the detail view to the center of BorderPane
        System.out.println(rec);
    }



/*
    private void openDetailPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/DetailReclamation.fxml"));
            Parent root = loader.load();

            // Access the controller and call a method
            DetailReclamation controller = loader.getController();
            controller.setData(rec); // Pass the Reclamation object to the DetailReclamation controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
