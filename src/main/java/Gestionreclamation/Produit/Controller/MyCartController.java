package Gestionreclamation.Produit.Controller;

import Gestionreclamation.Produit.Entity.Product;
import Gestionreclamation.Produit.Services.ProductService;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileOutputStream;
import java.sql.SQLException;


public class MyCartController {

    @FXML
    private TableView<Product> cartTable;
    @FXML
    private Label cartIndicator;


    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, String> imageColumn;

    @FXML
    private TableColumn<Product, Double> totalColumn;

    @FXML
    private Label totalLabel;
    private ObservableList<Product> cartItems = FXCollections.observableArrayList();

    @FXML
    private Button generateInvoiceButton;
    private double total = 0;
    private ProductService productService; // Ajoutez cette ligne

    public MyCartController() {
        this.productService = new ProductService(); // Initialisez productService dans le constructeur
    }

    @FXML
    public void initialize() {
        // Initialize the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        //Set a custom cell factory for the image column
        imageColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(item));
                    imageView.setFitWidth(50);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        // Set a custom cell factory for the quantity column
        quantityColumn.setCellFactory(param -> new TableCell<>() {
            final Spinner<Integer> spinner = new Spinner<>();

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, item));
                    spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                        Product product = getTableView().getItems().get(getIndex());
                        product.setQuantite(newVal);
                        product.setTotal(product.getPrice() * newVal);
                        total += product.getPrice() * (newVal - oldVal);
                        totalLabel.setText("Total: " + total);

                        // Refresh the row item
                        getTableView().getItems().set(getIndex(), product);
                    });
                    setGraphic(spinner);
                }
            }
        });
        cartTable.setItems(cartItems);

    }

    public void addProductToCart(Product product, int quantity, Label cartIndicator) {
        try {
            // Decrease the quantity of the product in the inventory
            productService.decrementQuantity(product, quantity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        product.setQuantite(quantity);
        product.setTotal(product.getPrice() * quantity);
        total += product.getTotal();

        // Check if the product is already in the cart
        boolean isProductInCart = cartTable.getItems().stream()
                .anyMatch(p -> p.getId() == product.getId());

        // If the product is not in the cart, increment cartIndicator
        if (!isProductInCart) {
            int currentQuantity = Integer.parseInt(cartIndicator.getText());
            int newCartIndicatorValue = currentQuantity + 1;
            cartIndicator.setText(String.valueOf(newCartIndicatorValue));
        }

        // Add the product to the cart table
        cartTable.getItems().add(product);

        // Refresh the cart table
        cartTable.refresh();

        // Update the total label
        totalLabel.setText("Total: " + total);
    }

    public void generateInvoice() {
        System.out.println("Generating invoice...");

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, new FileOutputStream("Invoice.pdf"));
            document.open();

            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("Thank you for your visit"));

            PdfPTable table = new PdfPTable(5); // 5 columns.
            table.addCell("Product Name");
            table.addCell("Price");
            table.addCell("Quantity");
            table.addCell("Total");
            table.addCell("Image");

            for (Product product : cartItems) {
                table.addCell(product.getName());
                table.addCell(String.valueOf(product.getPrice()));
                table.addCell(String.valueOf(product.getQuantite()));
                table.addCell(String.valueOf(product.getTotal()));
                table.addCell(product.getImage());
            }

            document.add(table);
            document.add(new Paragraph("Total: " + total));

            document.close();

            // Ajoutez cette partie pour afficher une alerte lorsque le PDF est prêt
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Ready");
            alert.setHeaderText(null);
            alert.setContentText("The PDF has been generated and is ready for download.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}