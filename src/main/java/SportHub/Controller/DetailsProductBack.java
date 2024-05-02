package SportHub.Controller;

import SportHub.Entity.Categorie_p;
import SportHub.Entity.Product;
import SportHub.Services.ProductService;
import SportHub.Services.Servicecategorie;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DetailsProductBack {
    @FXML
    private Label Price;

    @FXML
    private Label Description;

    @FXML
    private Label Quantity;

    @FXML
    private Pane PaneDetail;

    @FXML
    private Button product_clear;

    @FXML
    private TextField product_price;

    @FXML
    private TextArea product_description;

    @FXML
    private ImageView product_image;

    @FXML
    private Button product_import;

    @FXML
    private TextField product_quantity;

    @FXML
    private TextField product_name;

    @FXML
    private ChoiceBox<Categorie_p> choicebox;

    @FXML
    private Button product_update;

    @FXML
    private Button hide;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Button modifier;

    @FXML
    private AnchorPane modifierPane;

    @FXML
    private ImageView modifiericon;

    @FXML
    private ImageView modifiericon1;

    @FXML
    private Label name1;

    @FXML
    private AnchorPane root2;

    @FXML
    private ImageView suppicon;

    @FXML
    private Button supprimer;


    private TextField nom_image;
    private File file = null;
    private Image image = null;
    private String url_image = null;
    private Product product;

    Servicecategorie servicecategorie = new Servicecategorie();

    // Initialize method
    public void initialize() {
        modifierPane.setVisible(false);
        // Initially set the modifierPane to not visible

        // Add a click listener to the modifiericon1
        modifiericon1.setOnMouseClicked(e -> {
            modifierPane.setVisible(true);

            // Set the text of the fields in the modifierPane to the text of the corresponding labels in the PaneDetail
            product_name.setText(name1.getText());
            product_quantity.setText(Quantity.getText());
            product_description.setText(Description.getText());
            product_price.setText(Price.getText());
            // Set the image in the ImageView
            product_image.setImage(imageProduct.getImage());

            Set<Categorie_p> categories = servicecategorie.getAllC();
            choicebox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Categorie_p objet) {
                    // Retourne la représentation en String de l'objet que vous voulez afficher
                    return objet.getName();
                }

                @Override
                public Categorie_p fromString(String s) {
                    return null;
                }
            });
            choicebox.setItems(FXCollections.observableArrayList(categories));
            choicebox.setValue(product.getCategory());        });
        // Add a click listener to the hide button
        hide.setOnMouseClicked(e -> {
            modifierPane.setVisible(false);
        });
    }

    public void setProduct(Product product) {
        this.product = product; // Set the product object in the DetailsProduitBack class
        name1.setText(product.getName());
        Price.setText(String.valueOf(product.getPrice()));
        Quantity.setText(String.valueOf(product.getQuantite()));
        Description.setText(product.getDescription());
        // Load the image
        File file = new File(product.getImage());
        Image image = new Image(file.toURI().toString());
        imageProduct.setImage(image);
        choicebox.setValue(product.getCategory());

    }
    @FXML
    void productUpdate()   {
        Alert alert;

        // Vérifiez si les champs sont vides
        if (product_name.getText().isEmpty() || product_description.getText().isEmpty()
                || product_price.getText().isEmpty() || product_quantity.getText().isEmpty()
                || choicebox.getValue() == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs sont obligatoires");
            alert.showAndWait();
            return;
        }
        // Step 1: Get the values from the text fields, choice box, and other input fields
        String name = product_name.getText();
        int price =  Integer.parseInt(product_price.getText());
        int quantite = Integer.parseInt(product_quantity.getText());
        String description = product_description.getText();
        String image = (url_image != null) ? url_image : product.getImage();
        Categorie_p categorie = choicebox.getValue();
    try {
            price = Integer.parseInt(product_price.getText());

            if (price < 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Le prix doit être une valeur numérique positive");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            // Si le prix ne peut pas être converti en entier, affichez une erreur
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Le prix doit être une valeur numérique valide");
            alert.showAndWait();
            return;
        }

        // Vérifiez si la quantité est numérique et qu'elle ne dépasse pas 10
        try {
            if (product_quantity.getText().isEmpty()) {
                // La quantité est laissée vide, utilisez null
                product.setQuantite(0);
            } else {
                int quantity = Integer.parseInt(product_quantity.getText());

                if (quantity < 0 || quantity > 10) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("La quantité doit être une valeur numérique entre 0 et 10");
                    alert.showAndWait();
                    return;
                }

                product.setQuantite(quantity);
            }
        } catch (NumberFormatException e) {
            // Si la quantité ne peut pas être convertie en entier, affichez une erreur
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("La quantité doit être une valeur numérique valide");
            alert.showAndWait();
            return;
        }


        if (product == null) {
            // Initialize the event object
            product = new Product();
        }


        Product updatedProduct = new Product(product.getId(), name, quantite, price, categorie, description, image);

        // Step 3: Call the updateProduct method from ProductService
        ProductService productService = new ProductService();
        // Vérifiez si le prix est numérique et non négatif

        try {
            if (productService.productExist(name) && !name.equals(product.getName())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Un produit avec le même nom existe déjà. Veuillez saisir un autre nom.");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception selon vos besoins
        }
        productService.modifier(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getDescription(), updatedProduct.getQuantite(), updatedProduct.getPrice(), updatedProduct.getImage(), updatedProduct.getCategory());
        name1.setText(updatedProduct.getName());
        Price.setText(String.valueOf(updatedProduct.getPrice()));
        Description.setText(updatedProduct.getDescription());
        Quantity.setText(String.valueOf(updatedProduct.getQuantite()));
        choicebox.setValue(updatedProduct.getCategory());
        // Step 5: Update the imageEvnement ImageView
        if (updatedProduct.getImage() != null) {
            File file = new File(updatedProduct.getImage());
            Image newImage = new Image(file.toURI().toString());
            imageProduct.setImage(newImage);
        } else {
            // Handle the situation where updatedEvent.getImageEvenement() is null
            // For example, you could set imageEvnement to a default image
        }
        modifierPane.setVisible(false);
    }


    @FXML
    void importImage1() {
        System.out.println("importImage method called");

        FileChooser open = new FileChooser();
        file = open.showOpenDialog(root2.getScene().getWindow());

        if (file != null) {
            System.out.println("File selected: " + file.getAbsolutePath());

            image = new Image(file.toURI().toString(), 270, 310, false, true);
            url_image = file.getAbsolutePath(); // get the full path of the file
            product_image.setImage(image);
        } else {
            System.out.println("No file selected");
            url_image = null;
        }
    }
    @FXML
    void deleteProduct() {
        // Step 1: Check if the event object is null
        if (product == null) {
            System.out.println("No event selected");
            return;
        }

        // Step 2: Create a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Event : " + product.getName() + "?");

        // Step 3: Show the confirmation dialog and wait for the user's response
        Optional<ButtonType> option = alert.showAndWait();

        // Step 4: If the user clicked OK, delete the event
        if (option.get().equals(ButtonType.OK)) {
            ProductService productService = new ProductService();
            try {
                productService.delete(product.getId());
                // Show a success message
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted!");
                alert.showAndWait();

                // Step 5: Update the UI
                // Refresh the page to EvenementBack
                try {
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("/SportHub/Product.fxml"));
                    root2.getChildren().setAll(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
