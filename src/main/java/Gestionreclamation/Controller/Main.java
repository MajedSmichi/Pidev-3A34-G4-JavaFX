package Gestionreclamation.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
       //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Gestionreclamation/Login/login.fxml"));
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Gestionreclamation/Dash.fxml"));
      //  FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Gestionreclamation/client.fxml"));
        Parent root = fxmlLoader.load(); // No need to set a new root here.
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}