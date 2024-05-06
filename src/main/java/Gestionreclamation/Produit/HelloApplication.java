package Gestionreclamation.Produit;

import connectionSql.ConnectionSql;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Frontview.fxml"));
        //AnchorPane root = fxmlLoader.load();
        ScrollPane root = fxmlLoader.load();


        // Bind the size of the root node to the size of the stage
        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty());

        Scene scene = new Scene(root);
        stage.setTitle("SPORTHUB");
        stage.setScene(scene);
        stage.show();

       ConnectionSql connectionSql = new ConnectionSql();
        Connection connection = connectionSql.getConnection();

    }


    public static void main(String[] args) {
        launch();



    }

}
