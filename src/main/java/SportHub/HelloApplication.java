package SportHub;
import SportHub.Entity.Product;
import SportHub.Entity.Categorie_p;
import SportHub.Services.ProductService;
import SportHub.Services.Servicecategorie;
import connectionSql.ConnectionSql;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Market.fxml"));
        AnchorPane root = fxmlLoader.load();
        //ScrollPane root = fxmlLoader.load();


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
