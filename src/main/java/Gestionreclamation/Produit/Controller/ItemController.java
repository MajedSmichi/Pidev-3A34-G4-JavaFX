package Gestionreclamation.Produit.Controller;

import Gestionreclamation.Produit.Entity.Product;
import Gestionreclamation.Produit.Services.FavoriteListener;
import Gestionreclamation.Produit.Services.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;


public class ItemController {

    @FXML
    private Label nameLabel;
    private MyListener myListener;

    @FXML
    private AnchorPane itemPane; // Assurez-vous que c'est l'élément sur lequel vous voulez écouter les clics

    private Product product;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView starIcon;

    private String starIconUrl; // Add this line

    @FXML
    private ImageView img;
    private FavoriteListener favoriteListener;


    @FXML
    private ImageView soldOutImage; // Assurez-vous que cette variable correspond à l'ID de votre ImageView dans le fichier FXML
    private List<Product> favorites = new ArrayList<>();


    public void setProduct(Product product) {
        this.product = product;
        nameLabel.setText(product.getName());
        priceLabel.setText("$" + product.getPrice());
        if (product.getImage() != null) {
            img.setImage(new Image(product.getImage()));
        } else {
            // Handle the case where the image is null
            // For example, you can set a default image or leave the ImageView empty
        }
       updateSoldOutImageVisibility(product);
        starIconUrl = "/SportHub/images/star-removebg-preview.png";
        starIcon.setImage(new Image(getClass().getResourceAsStream(starIconUrl)));


    }
    private void updateSoldOutImageVisibility(Product product) {
        System.out.println("Product quantity: " + product.getQuantite()); // Print the product quantity for debugging
        if (soldOutImage != null) {
            if (product.getQuantite() <= 0) {
                soldOutImage.setVisible(true);
            } else {
                soldOutImage.setVisible(false);
            }
        } else {
            System.out.println("soldOutImage is not initialized");
        }
    }
    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }
    @FXML
    public void click() {
        myListener.onClickListener(product);
    }
    public void setFavoriteListener(FavoriteListener favoriteListener) {
        this.favoriteListener = favoriteListener;
    }
    @FXML
    private void toggleFavorite() {
        try {
            if (starIconUrl.endsWith("star-removebg-preview.png")) {
                starIconUrl = "/SportHub/images/star_orangeee-removebg-preview.png";
                starIcon.setImage(new Image(getClass().getResourceAsStream(starIconUrl)));
                favorites.add(product);
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteToggle(product, true);
                }
            } else {
                starIconUrl = "/SportHub/images/star-removebg-preview.png";
                starIcon.setImage(new Image(getClass().getResourceAsStream(starIconUrl)));
                favorites.remove(product);
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteToggle(product, false);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
    public void hideStar() {
        starIcon.setVisible(false);
    }

    public List<Product> getFavorites() {
        return favorites;
    }

}