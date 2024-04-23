package GestionSalle;
import GestionSalle.Entity.Activite;
import GestionSalle.Services.ActiviteService;
import GestionSalle.Services.SalleService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Frontview.fxml"));
        //BorderPane root = fxmlLoader.load();
        //AnchorPane root = fxmlLoader.load();
        ScrollPane root = fxmlLoader.load();

        // Bind the size of the root node to the size of the stage
        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty());

        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }


    public static void main(String[] args) {
        launch();
        }
    }




