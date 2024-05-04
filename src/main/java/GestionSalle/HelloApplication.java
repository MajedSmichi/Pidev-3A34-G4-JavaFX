package GestionSalle;
import GestionSalle.Entity.Activite;
import GestionSalle.Services.ActiviteService;
import GestionSalle.Services.SalleService;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class HelloApplication extends Application {
    private static HelloApplication instance;

    public HelloApplication() {
        instance = this;
    }

    public static HelloApplication getInstance() {
        return instance;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Dash.fxml"));
        Parent root = fxmlLoader.load();

        if (root instanceof Region) {
            Region region = (Region) root;
            // Bind the size of the root node to the size of the stage
            region.prefWidthProperty().bind(stage.widthProperty());
            region.prefHeightProperty().bind(stage.heightProperty());
        }

        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }


    public static void main(String[] args) {
        launch();
        }

    public HostServices getHostService() {
        return getHostServices();
    }
}




