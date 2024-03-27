package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login/login.fxml"));
        Parent root = fxmlLoader.load(); // No need to set a new root here.
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login Screen"); // Optional: Set a title for the window
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }
}