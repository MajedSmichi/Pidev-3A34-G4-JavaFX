package SportHub.Controller;

import SportHub.Entity.Categorie_p;
import SportHub.Entity.Product;
import SportHub.Services.ProductService;
import SportHub.Services.Servicecategorie;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ProduitController {
    private ProductService serviceProduit;
    @FXML
    private GridPane produitContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane ajouterPane;

    @FXML
    private Button ajouterProduit;

    @FXML
    private Button hide;

    @FXML
    private Button produit_add;

    @FXML
    private Button produit_clear;

    @FXML
    private TextField produit_nom;

    @FXML
    private TextField produit_prix;

    @FXML
    private TextArea produit_description;

    @FXML
    private TextField produit_quantity;

    @FXML
    private ImageView produit_image;

    @FXML
    private ChoiceBox<Categorie_p> choicebox;

    @FXML
    private AnchorPane root1;


    @FXML
    private Button produit_import;

    private File file = null;
    private String url_image = null;

    private Image image = null;


     private Servicecategorie serviceCategorie;
     private Product product;


    public void initialize() {
         serviceProduit = new ProductService();
         serviceCategorie = new Servicecategorie();
        try {
            Set<Categorie_p> categories = serviceCategorie.getAllC();
            if (categories!=null && !categories.isEmpty()) {
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
                choicebox.setValue(categories.stream().findFirst().orElse(null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

         hide.visibleProperty().bind(ajouterPane.visibleProperty());
         ajouterPane.setVisible(false); // Make ajouterPane not visible
         ajouterProduit.setOnAction(e -> {
            ajouterPane.setVisible(true);
        });
        hide.setOnAction(e -> {
            ajouterPane.setVisible(false);
        });
        try {
            displayProduct();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void displayProduct() throws SQLException, IOException {
       List<Product>products = serviceProduit.getAll();
       for (int i = 0; i < products.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/SportHub/ProductCard.fxml"));
            Pane pane = fxmlLoader.load();
            ProductCard controller = fxmlLoader.getController();
            controller.setData(products.get(i));

            pane.setOnMouseClicked(e -> {
               try {
                   openDetails(controller.getProduct());
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           });
            produitContainer.add(pane, i % 3, i / 3);
        }
    }

    public void produitAdd() {
        try {
            Alert alert;

            if (produit_nom.getText().isEmpty()
                    || produit_description.getText().isEmpty() || produit_quantity.getText().isEmpty()
                    || produit_prix.getText().isEmpty() || choicebox.getValue() == null
            ) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Tous les champs sont obligatoires");
                alert.showAndWait();
            } else {
                String name = produit_nom.getText();
                if (serviceProduit.productExist(name)) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Produit nom déjà existe, merci de le changer");
                    alert.showAndWait();
                } else {

                    Product product = new Product();
                    product.setName(produit_nom.getText());
                    product.setDescription(produit_description.getText());
                    product.setPrice((int) Double.parseDouble(produit_prix.getText()));
                    product.setCategory(choicebox.getValue());
                    product.setQuantite(Integer.parseInt(produit_quantity.getText()));

                    if (file != null) {
                        product.setImage(file.getPath()); // Set the image path
                    }

                    serviceProduit.ajouter(product);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Produit ajouté avec succès!");
                    alert.showAndWait();

                    displayProduct();
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void importImage() {
        FileChooser open = new FileChooser();
        file = open.showOpenDialog(root1.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString(), 270, 310, false, true);
            url_image = file.getName();
            produit_image.setImage(image);
        }
    }
    private void openDetails(Product product) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SportHub/DetailsProductBack.fxml"));
        Parent detailsRoot = fxmlLoader.load();
        DetailsProductBack controller = fxmlLoader.getController();
        controller.setProduct(product);
        root1.getChildren().setAll(detailsRoot);
    }
}
