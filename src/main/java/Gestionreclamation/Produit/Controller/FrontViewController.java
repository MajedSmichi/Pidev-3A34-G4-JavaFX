package Gestionreclamation.Produit.Controller;

import Gestionreclamation.Produit.Services.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class FrontViewController {

    @FXML
    private AnchorPane achorfront;

    @FXML
    private FlowPane productFlowPane;

    @FXML
    private ListView<String> categoryListView;


    private ProductService productService;

    @FXML
    public void loadMarket() {
        try {
            // Load the new FXML file
            AnchorPane marketPane = FXMLLoader.load(getClass().getResource("/SportHub/market.fxml"));

            // Clear the existing content and add the new pane
            achorfront.getChildren().clear();
            achorfront.getChildren().add(marketPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








}