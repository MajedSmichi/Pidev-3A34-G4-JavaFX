package SportHub.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import SportHub.Entity.Product;
import SportHub.Services.ProductService;
import SportHub.Services.MyListener;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController implements Initializable {

    @FXML
    private GridPane grid;

    @FXML
    private VBox chosenProductCard;

    @FXML
    private Label Product_name;

    @FXML
    private Label Product_price;

    @FXML
    private ImageView Product_image;

    private ProductService productService;

    private void setChosenProduct(Product product) {
        Product_name.setText(product.getName());
        Product_price.setText("$" + product.getPrice());
        Product_image.setImage(new Image(product.getImage()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService = new ProductService();
        List<Product> products = null;
        try {
            products = productService.getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        MyListener myListener = new MyListener() {
            @Override
            public void onClickListener(Product product) {
                setChosenProduct(product);
            }
        };

        int column = 0;
        int row = 1;

        grid.setHgap(10);
        grid.setVgap(10);


        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/SportHub/Item.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setProduct(products.get(i));
                itemController.setMyListener(myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}