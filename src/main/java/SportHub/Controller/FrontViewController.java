package SportHub.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FrontViewController {



    @FXML
    private AnchorPane achorfront;

    @FXML
    private AnchorPane achorfront1;

    @FXML
    private Button calendar;




    @FXML
    public void loadEvenementFront() {
        try {
            AnchorPane evenementFront = FXMLLoader.load(getClass().getResource("/SportHub/EvenementFront.fxml"));
            evenementFront.getStyleClass().add("center-content"); // Add the CSS class
            achorfront.getChildren().setAll(evenementFront);
            System.out.println("Evenement front view loaded successfully");
            calendar.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while loading the evenement front view");
        }
    }

}