package Gestionreclamation.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dash {
    private static Dash instance;

    public Dash() {
        instance = this;
    }

    public static Dash getInstance() {
        return instance;
    }

    @FXML
    private AnchorPane anchor;

    @FXML
    public void loadRec() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReclamationView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            anchor.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadRep() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReponseView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            anchor.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
