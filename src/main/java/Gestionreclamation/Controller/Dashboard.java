package Gestionreclamation.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class Dashboard implements Initializable {

    public Pane getPaneviewreclamation() {
        return paneviewreclamation;
    }

    @FXML
    private Pane paneviewreclamation;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load ReclamationView.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReponseView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane

            // Set the loaded content as the children of paneviewreclamation
            paneviewreclamation.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

