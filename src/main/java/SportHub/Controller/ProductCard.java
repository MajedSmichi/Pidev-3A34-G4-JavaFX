package SportHub.Controller;

import SportHub.Entity.Product;
import SportHub.Services.ProductService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.sql.SQLException;

public class ProductCard {

    @FXML
    private Label price;

    @FXML
    private Pane PaneProduct;

    @FXML
    private Label name;

    @FXML
    private ImageView imageProduit;

    @FXML
    private Button supprimer;

    private Product product;
    private ProductService produittService = new ProductService();

    public void setData(Product product) {
        this.product = product;
        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));

        // Load the image
        try {
            if (product.getImage() != null) {
                File file = new File(product.getImage());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageProduit.setImage(image);
                } else {
                    System.err.println("Image file does not exist: " + product.getImage());
                }
            } else {
                System.err.println("Image path is null");
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public Product getProduct() {
        return this.product;
    }

}