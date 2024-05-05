package Gestionreclamation.SportHub;
import Gestionreclamation.SportHub.Controller.CalendarViewController;
import Gestionreclamation.SportHub.Entity.Evenement;
import Gestionreclamation.SportHub.Entity.Ticket;
import Gestionreclamation.SportHub.Services.EvenementService;
import Gestionreclamation.SportHub.Services.TicketService;
import connectionSql.ConnectionSql;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public class HelloApplication extends Application {
    @Override
public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Dash.fxml"));

    Parent root = fxmlLoader.load();

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
