package SportHub.Controller;

import SportHub.Entity.Product;
import SportHub.Services.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontViewController {

    @FXML
    private AnchorPane achorfront;

    @FXML
    private FlowPane productFlowPane;

    @FXML
    private ListView<String> categoryListView;


    private ProductService productService;

    public FrontViewController() {
        productService = new ProductService();
    }









}