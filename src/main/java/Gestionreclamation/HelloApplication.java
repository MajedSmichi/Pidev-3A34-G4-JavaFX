package Gestionreclamation;
import Gestionreclamation.Services.AssemblyAIService;
import Gestionreclamation.Services.GrammarCheck;
import Gestionreclamation.Services.sms77io;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dash.fxml"));
        Pane  root = fxmlLoader.load();
        Scene scene = new Scene(root);
        AssemblyAIService assemblyAIService = new AssemblyAIService();
        assemblyAIService.assembly();


        stage.setTitle("SPORT HUB");
        stage.setScene(scene);
        stage.show();

        ConnectionSql connectionSql = new ConnectionSql();
        Connection connection = connectionSql.getConnection();


    }


    public static void main(String[] args) {
        launch();


    }

}
