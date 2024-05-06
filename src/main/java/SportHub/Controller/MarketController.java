package SportHub.Controller;

import SportHub.Services.Servicecategorie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import SportHub.Entity.Product;
import SportHub.Services.ProductService;
import SportHub.Services.MyListener;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import SportHub.Services.FavoriteListener;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MarketController implements Initializable,FavoriteListener {

    @FXML
    private GridPane grid;

    @FXML
    private VBox chosenProductCard;

    @FXML
    private Label Product_name;

    @FXML
    private Label Product_price;

    @FXML
    private Label product_quantity;

    @FXML
    private ImageView soldOutImage; // Assurez-vous que cette variable correspond à l'ID de votre ImageView dans le fichier FXML


    @FXML
    private Label cartIndicator;

    @FXML
    private Spinner<Integer> quantitySpinner;
    @FXML
    private Slider mySlider;
    @FXML
    private SpinnerValueFactory<Integer> spinnerValueFactory;

    @FXML
    private Label sliderValueLabel; // New Label to display the slider value


    @FXML
    private ImageView Product_image;

    @FXML
    private TextField searchField;
    private boolean firstClick = true;

    @FXML
    private ComboBox<String> categoryFilter;

    private MyListener myListener;
    private List<Product> products;

    private Product chosenProduct;
    private List<Product> favorites = new ArrayList<>(); // Add this line

    private Servicecategorie serviceCategorie;
    private MyCartController myCartController;
    @FXML
    private ImageView cartImageView; // Assurez-vous que cet ImageView correspond à l'image de la carte dans votre fichier FXML

    private ProductService productService;
    private Stage cartStage = null; // Add this line


    public MarketController() {
        this.productService = new ProductService();
        this.serviceCategorie = new Servicecategorie();
    }

    private void setChosenProduct(Product product) {
        try {
            // Fetch the latest product details from the database
            Product latestProduct = productService.getProductById(product.getId());

            chosenProduct = latestProduct; // Update the chosenProduct when a product is selected
            Product_name.setText(latestProduct.getName());
            Product_price.setText("$" + latestProduct.getPrice());
            Product_image.setImage(new Image(latestProduct.getImage()));
            product_quantity.setText(String.valueOf(latestProduct.getQuantite()));
            product_quantity.setVisible(true); // Rendre le label visible lorsque un produit est sélectionné
            updateSoldOutImageVisibility(latestProduct);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        soldOutImage = new ImageView();
        MyListener myListener = new MyListener() {
            @Override
            public void onClickListener(Product product) {
                setChosenProduct(product);
            }
        };
        spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantitySpinner.setValueFactory(spinnerValueFactory);
        product_quantity.setVisible(false);// Rendre le label invisible au démarrage
        mySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Convert the Slider value to a double
            double selectedPrice = newValue.doubleValue();

            // Update the label text
            sliderValueLabel.setText(String.format("%.2f", selectedPrice));

            // Filter the products by price
            List<Product> filteredProducts = products.stream()
                    .filter(product -> product.getPrice() <= selectedPrice)
                    .collect(Collectors.toList());

            // Update the product list in the grid
            updateProductList(filteredProducts);
        });
        try {
            // Fill the category filter with categories from the database
            List<String> categories = productService.getCategories();
            categories.add(0, "All"); // Add "All" at the beginning of the list
            categoryFilter.getItems().addAll(categories);

            // Add a listener to the category filter
            categoryFilter.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                try {
                    // Filter the products by the selected category
                    List<Product> filteredProducts;

                    // Check if "All" is selected
                    if ("All".equals(newValue)) {
                        // If "All" is selected, show all products
                        filteredProducts = productService.getAll();
                    } else {
                        // If a specific category is selected, filter the products by the selected category
                        filteredProducts = productService.getByCategory(newValue);
                    }
                    // Update the product list in the grid
                    updateProductList(filteredProducts);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            products = productService.getAll(); // Ici, nous affectons la valeur à la variable d'instance `products`
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



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

                itemController.setFavoriteListener(this);


                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchProduct());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyCart.fxml"));
            Parent root = loader.load();
            myCartController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void searchProduct() {
        String searchText = searchField.getText().trim();

        // Search for the product in the list of products
        Optional<Product> productOpt = products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(searchText))
                .findFirst();

        // If the product is found, update the `chosenProductCard`
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            // Update the product details in the `chosenProductCard`
            Product_name.setText(product.getName());
            Product_price.setText(String.valueOf(product.getPrice()));
            // Make sure you have a method to get the image of the product
            Product_image.setImage(new Image(product.getImage()));
            product_quantity.setText(String.valueOf(product.getQuantite()));
        } else {
            // If the product is not found, you can clear the `chosenProductCard` or display an error message
            Product_name.setText("");
            Product_price.setText("");
            Product_image.setImage(null);
            product_quantity.setText("");
        }
    }
    private void updateProductList(List<Product> products) {
        MyListener myListener = new MyListener() {
            @Override
            public void onClickListener(Product product) {
                setChosenProduct(product);
            }
        };
        // Clear the grid
        grid.getChildren().clear();

        // Add the filtered products to the grid
        int column = 0;
        int row = 1;
        for (Product product : products) {
            try {
                // Load the product card
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/SportHub/Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                // Set the product on the controller
                ItemController itemController = fxmlLoader.getController();
                itemController.setProduct(product);
                itemController.setMyListener(myListener); // Set myListener here

                // Add the product card to the grid
                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    @FXML
    private void addToCart() {

        int selectedQuantity = quantitySpinner.getValue();
        int currentProductQuantity = Integer.parseInt(product_quantity.getText());

        if (currentProductQuantity <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The stock for this product is exhausted.");
            alert.showAndWait();
        } else {
            if (cartStage != null && !cartStage.isShowing()) {
                openCart(); // Ouvrir le panier
            }
            // Ajoutez le produit au panier
            myCartController.addProductToCart(chosenProduct, selectedQuantity, cartIndicator);

            // Vérifiez si la fenêtre du panier est déjà ouverte, sinon ouvrez-la
            System.out.println("Product added to cart.");
            setChosenProduct(chosenProduct);
        }
    }
    @FXML
    private void openMyFavors() {
        try {
            // Load the FXML file for the favorites window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/favorites.fxml"));
            Parent root = loader.load();

            // Get the controller of the favorites window
            FavoritesController favoritesController = loader.getController();

            // Pass the list of favorite products to the favorites controller
            favoritesController.setFavorites(favorites);

            // Create a new stage for the favorites window
            Stage favoritesStage = new Stage();
            favoritesStage.setTitle("My Favorites");
            favoritesStage.setScene(new Scene(root));

            // Show the favorites window
            favoritesStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture de la fenêtre des favoris : " + e.getMessage());
        }
    }
    @Override
    public void onFavoriteToggle(Product product, boolean isFavorite) {
        if (isFavorite) {
            favorites.add(product);
        } else {
            favorites.remove(product);
        }
    }
    public void openCart() {
        try {
            // Check if cartStage is already initialized
            if (cartStage == null) {
                // Load the FXML file for the cart window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyCart.fxml"));
                Parent root = loader.load();

                // Initialize myCartController here
                myCartController = loader.getController();

                // Create a new stage for the cart window
                cartStage = new Stage();
                cartStage.setTitle("My Cart");
                cartStage.setScene(new Scene(root));
            }

            // Show the cart window
            cartStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture de la fenêtre du panier : " + e.getMessage());
        }
    }
}