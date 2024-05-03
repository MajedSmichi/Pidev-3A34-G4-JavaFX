package SportHub.Controller;

import SportHub.Services.Servicecategorie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private Label product_quantity;

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

    @FXML
    private ComboBox<String> categoryFilter;

    private MyListener myListener;
    private List<Product> products;

    private Servicecategorie serviceCategorie;

    private ProductService productService;

    public MarketController() {
        this.productService = new ProductService();
        this.serviceCategorie = new Servicecategorie();
    }
    private void setChosenProduct(Product product) {
        Product_name.setText(product.getName());
        Product_price.setText("$" + product.getPrice());
        Product_image.setImage(new Image(product.getImage()));
        product_quantity.setText(String.valueOf(product.getQuantite()));
        product_quantity.setVisible(true); // Rendre le label visible lorsque un produit est sélectionné
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    // Print the selected category
                    System.out.println("Selected category: " + newValue);

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

                    // Print the filtered products
                    System.out.println("Filtered products: " + filteredProducts);

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
    @FXML
    private void addToCart() {
        // Récupérer la quantité sélectionnée
        int selectedQuantity = quantitySpinner.getValue();

        // Ajouter le produit au panier
        // Vous devrez implémenter la logique pour ajouter le produit et la quantité au panier
        // Par exemple, si vous avez une classe Cart, vous pouvez faire quelque chose comme :
        // Cart.add(Product_name.getText(), selectedQuantity);

        // Mettre à jour l'indicateur du panier
        int currentQuantity = Integer.parseInt(cartIndicator.getText());
        cartIndicator.setText(String.valueOf(currentQuantity + selectedQuantity));
    }
}