package Gestionreclamation.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class client implements Initializable {


    @FXML
    private AnchorPane archofront;

    @FXML
    private ScrollPane s;


    public void initialize(URL location, ResourceBundle resources) {
        s.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        s.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        // rest of your code...
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/AjouterReclamation.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            archofront.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Charger en tant que Pane

    }
}