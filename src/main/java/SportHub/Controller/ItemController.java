package SportHub.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import SportHub.Entity.Product;
import SportHub.Services.MyListener;
import javafx.scene.layout.AnchorPane;


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
    private ImageView img;

    public void setProduct(Product product) {
        this.product = product;
        nameLabel.setText(product.getName());
        priceLabel.setText("$" + product.getPrice());
        img.setImage(new Image(product.getImage()));
    }
    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }
    @FXML
    public void click() {
        myListener.onClickListener(product);
    }

}