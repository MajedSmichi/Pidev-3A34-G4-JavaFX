package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReclamationController implements Initializable {
    @FXML
    private GridPane GridReclamation;

    @FXML
    private BorderPane borderpanereclamation;
    @FXML
    private TextField searchRec;

    @FXML
    private VBox vboxContainer;

    @FXML
    private ProgressBar recNonTraite;

    @FXML
    private ProgressBar recTraite;

    @FXML
    private Label recTraiteLabel;

    @FXML
    private Label recNonTraiteLabel;

    @FXML
    private Pane paneNonTraite;

    @FXML
    private Pane paneTraite;


    @FXML
    private LineChart<?, ?> rendement;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private MenuButton tous;

    private List<Reclamation> reclamations = new ArrayList<>();
    private Pagination pagination;
    private static final int ITEMS_PER_PAGE = 8;

    public BorderPane getBorderPane() {
        return borderpanereclamation;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ReclamationService reclamationService = new ReclamationService();
            double treatedPercentage = reclamationService.getTreatedReclamationPercentage() / 100;
            double pendingPercentage = reclamationService.getPendingReclamationPercentage() / 100;

            recTraite.setProgress(treatedPercentage);
            recNonTraite.setProgress(pendingPercentage);
            recTraiteLabel.setText(String.format("%.2f%%", reclamationService.getTreatedReclamationPercentage()));
            recNonTraiteLabel.setText(String.format("%.2f%%", reclamationService.getPendingReclamationPercentage()));
            displayWeeklyResponseRate();

            reclamations = getData();
            createPagination();

            populateGrid(reclamations);

            // Add a listener to the search TextField
            searchRec.textProperty().addListener((observable, oldValue, newValue) -> {
                // Filter the reclamations list based on the search text
                List<Reclamation> filteredReclamations = reclamations.stream()
                        .filter(rec -> rec.getNom().toLowerCase().contains(newValue.toLowerCase())
                                ||rec.getPrenom() != null && rec.getPrenom().toLowerCase().contains(newValue.toLowerCase())
                                || rec.getSujet().toLowerCase().contains(newValue.toLowerCase())
                                || rec.getDate().toString().contains(newValue))
                        .collect(Collectors.toList());

                // Update the GridReclamation with the filtered reclamations
                try {
                    populateGrid(filteredReclamations);
                    updatePagination(filteredReclamations); // Update the pagination

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Get the menu items
            MenuItem treatedItem = new MenuItem("Traité");
            MenuItem pendingItem = new MenuItem("En attente");
            MenuItem allItem = new MenuItem("Tous");


            // Add action handlers to the menu items
            treatedItem.setOnAction(event -> {
                try {
                    // Filter the reclamations list for treated reclamations
                    List<Reclamation> treatedReclamations = reclamations.stream()
                            .filter(rec -> rec.getEtat().equals("Traité"))
                            .collect(Collectors.toList());

                    // Update the GridReclamation with the filtered reclamations
                    populateGrid(treatedReclamations);
                    updatePagination(treatedReclamations);
                    tous.setText(treatedItem.getText());


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            pendingItem.setOnAction(event -> {
                try {
                    // Filter the reclamations list for pending reclamations
                    List<Reclamation> pendingReclamations = reclamations.stream()
                            .filter(rec -> rec.getEtat().equals("En attente"))
                            .collect(Collectors.toList());

                    // Update the GridReclamation with the filtered reclamations
                    populateGrid(pendingReclamations);
                    updatePagination(pendingReclamations);
                    tous.setText(pendingItem.getText());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // Add action handlers to the menu items
            allItem.setOnAction(event -> {
                try {
                    // Display all reclamations
                    populateGrid(reclamations);
                    updatePagination(reclamations);
                    tous.setText(allItem.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Add the menu items to the MenuButton
            tous.getItems().setAll(treatedItem, pendingItem,allItem);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createPagination() {
        int totalItems = reclamations.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        pagination = new Pagination(totalPages);
        pagination.setPageFactory(pageIndex -> {
            try {
                int fromIndex = pageIndex * ITEMS_PER_PAGE;
                int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, totalItems);
                List<Reclamation> pageReclamations = reclamations.subList(fromIndex, toIndex);
                populateGrid(pageReclamations);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Return an empty Pane
            return new Pane();
        });

        borderpanereclamation.setBottom(pagination);
    }

    private void updatePagination(List<Reclamation> reclamations) {
        int totalItems = reclamations.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        pagination.setPageCount(totalPages);
        pagination.setPageFactory(pageIndex -> {
            try {
                int fromIndex = pageIndex * ITEMS_PER_PAGE;
                int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, totalItems);
                List<Reclamation> pageReclamations = reclamations.subList(fromIndex, toIndex);
                populateGrid(pageReclamations);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Return an empty Pane
            return new Pane();
        });
    }

    private void populateGrid(List<Reclamation> reclamations) throws IOException {
        GridReclamation.getChildren().clear();
        int columns = 0;
        int row = 1;
        for (Reclamation rec : reclamations) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/OneReclamation.fxml"));
            Pane pane = fxmlLoader.load(); // Charger en tant que Pane
            OneReclamation oneReclamation = fxmlLoader.getController();
            oneReclamation.setReclamationController(this); // Passer une référence de ReclamationController
            oneReclamation.setData(rec);

            GridReclamation.add(pane, columns, row); // Utiliser le Pane chargé
            columns++;
            if (columns > 3) {
                columns = 0;
                row++;
            }
            GridPane.setMargin(pane, new Insets(10));
        }
    }


    private List<Reclamation> getData() throws SQLException {
        List<Reclamation> reclamationList = new ArrayList<>();
        ReclamationService reclamationService = new ReclamationService();

        // Assuming afficherreclamation() returns a List<Reclamation>
        for (Reclamation rec : reclamationService.afficherreclamation()) {
            Reclamation newRec = new Reclamation();
            newRec.setId(rec.getId());
            newRec.setUtilisateur(rec.getUtilisateur());
            newRec.setSujet(rec.getSujet());
            newRec.setNom(rec.getNom());
            newRec.setPrenom(rec.getPrenom());
            newRec.setDate(rec.getDate());
            newRec.setDescription(rec.getDescription());
            newRec.setEmail(rec.getEmail());
            newRec.setNumTele(rec.getNumTele());
            newRec.setEtat(rec.getEtat());
            reclamationList.add(newRec);
        }

        return reclamationList;
    }

    public GridPane getGridPane() {
        return GridReclamation;
    }


    public void displayWeeklyResponseRate() throws SQLException {
        // Create a new series
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Get the weekly response rate
        ReclamationService reclamationService = new ReclamationService();
        Map<Integer, Double> weeklyResponseRate = reclamationService.calculateWeeklyResponseRate();

        // Add data to the series
        for (Map.Entry<Integer, Double> entry : weeklyResponseRate.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue());
            series.getData().add(data);

            // Add a tooltip to each data point
            Tooltip tooltip = new Tooltip("Week " + entry.getKey() + ": " + entry.getValue());
            Tooltip.install(data.getNode(), tooltip);
        }

        // Clear the existing data from the chart
        rendement.getData().clear();

        // Add the series to the chart
        ((LineChart<String, Number>) rendement).getData().add(series);

        // Set the labels for the axes
        x.setLabel("Week");
        y.setLabel("Performance");
    }
}