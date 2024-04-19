package SportHub.Controller;

import SportHub.Entity.Product;
import SportHub.Services .ProductService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class ProductCard {

    @FXML
    private Label price;

    @FXML
    private Pane PaneProduit;

    @FXML
    private Label name;

    @FXML
    private ImageView imageProduit;

    @FXML
    private Button supprimer;

    private Product produit;
    private ProductService produittService = new ProductService();

    public void setData(Product produit) {
        this.produit = produit;
        price.setText(String.valueOf(produit.getPrice())); // Assuming Date is a Label
        name.setText(produit.getName()); // Assuming FullName is a Label

        // Set the image
        String imageUrl = produit.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageProduit.setImage(image);
        } else {
            // Handle the case when imageUrl is null or empty
            // For example, set a default image
        }


    }

    public Product getProduct() {
        return this.produit;
    }

}