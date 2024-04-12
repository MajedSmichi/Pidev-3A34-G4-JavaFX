package Gestionreclamation;
import connectionSql.ConnectionSql;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import Gestionreclamation.Services.openIAsevice;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("client.fxml"));
        ScrollPane root = fxmlLoader.load();
        // Bind the size of the root node to the size of the stage

        // Set the preferred size of the BorderPane to match the screen's size

        Scene scene = new Scene(root);


        stage.setTitle("SPORT HUB");
        stage.setScene(scene);
        stage.show();

        ConnectionSql connectionSql = new ConnectionSql();
        Connection connection = connectionSql.getConnection();



    }





    // Méthode pour afficher les réclamations à partir de la base de données


    public static void main(String[] args) {
        launch();


    }

}
