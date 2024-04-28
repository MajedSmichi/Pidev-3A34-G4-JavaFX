package SportHub;
import SportHub.Controller.CalendarViewController;
import SportHub.Entity.Evenement;
import SportHub.Entity.Ticket;
import SportHub.Services.EvenementService;
import SportHub.Services.TicketService;
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

        //VBox root = (VBox) FXMLLoader.load(getClass().getResource("CalendarView.fxml"));

        AnchorPane root = fxmlLoader.load();
       // ScrollPane root = fxmlLoader.load();


       // FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarView.fxml"));
//Parent root = loader.load();
//CalendarViewController controller = loader.getController();


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
