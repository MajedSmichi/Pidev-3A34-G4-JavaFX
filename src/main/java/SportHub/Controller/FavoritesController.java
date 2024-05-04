package SportHub.Controller;

import SportHub.Entity.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class FavoritesController {

    @FXML
    private GridPane favoritesGrid;

    private List<Product> favorites;

    public void setFavorites(List<Product> favorites) throws IOException {
        this.favorites = favorites;
        updateFavoritesGrid();
    }

    private void updateFavoritesGrid() throws IOException {
        // Clear the grid
        favoritesGrid.getChildren().clear();

        // Add the favorite products to the grid
        for (int i = 0; i < favorites.size(); i++) {
            Product product = favorites.get(i);

            // Load the product card
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/Item.fxml"));
            Parent productCard = loader.load();

            // Get the controller of the product card
            ItemController controller = loader.getController();

            // Set the product of the product card
            controller.setProduct(product);

            // Hide the star
            controller.hideStar();

            // Add the product card to the grid
            favoritesGrid.add(productCard, i % 3, i / 3);
        }
    }
}