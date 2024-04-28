package GestionSalle.Controller;

import GestionSalle.Entity.Salle;
import GestionSalle.Services.SalleService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FrontSalleController {
    @FXML
    private GridPane GridSalleFront;
    @FXML
    private BorderPane list;

    @FXML
    private TextField search;
    @FXML
    private ComboBox<String> filter;

    private Pagination pagination;
    private List<Salle> salles = new ArrayList<>();

    private static final int ITEMS_PER_PAGE = 6;

public void initialize() {
    try {
        salles = getData();
        createPagination();
        populateGrid(salles);

        // Add sorting options to the filter ComboBox
        filter.getItems().addAll("Trier par nombre de client (ascendant)", "Trier par nombre de client (descendant)");

        // Add a listener to the filter ComboBox
        filter.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Salle> sortedSalles;
                if (newValue.equals("Trier par nombre de client (ascendant)")) {
                    sortedSalles = salles.stream()
                            .sorted(Comparator.comparing(Salle::getNbr_client))
                            .collect(Collectors.toList());
                } else if (newValue.equals("Trier par nombre de client (descendant)")) {
                    sortedSalles = salles.stream()
                            .sorted(Comparator.comparing(Salle::getNbr_client).reversed())
                            .collect(Collectors.toList());
                } else  {
                    return;
                }
                updatePagination(sortedSalles);
                populateGrid(sortedSalles);
            } catch (IOException  e) {
                e.printStackTrace();
            }
        });

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Salle> filteredSalles = salles.stream()
                        .filter(salle -> salle.getNom().toLowerCase().contains(newValue.toLowerCase()))
                        .collect(Collectors.toList());

                updatePagination(filteredSalles);
                populateGrid(filteredSalles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}


    private List<Salle> getData() throws SQLException {
        SalleService salleService = new SalleService();
        return salleService.getAllSalle();
    }

    private void populateGrid(List<Salle> salles) throws IOException {
        GridSalleFront.getChildren().clear();
        int columns = 0;
        int row = 0; // Start from row 0 for each new page
        for (Salle salle : salles) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/SalleCard.fxml"));            Pane pane = fxmlLoader.load(); // Load as Pane
            SalleCardController oneSalle = fxmlLoader.getController();            oneSalle.setData(salle);
            GridSalleFront.add(pane, columns, row); // Use the loaded Pane
            columns++;
            if (columns > 2) {
                columns = 0;
                row++;
            }
            GridPane.setMargin(pane, new Insets(10));
        }
    }
    private void createPagination() {
        int totalItems = salles.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        pagination = new Pagination(totalPages);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ITEMS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, totalItems);
            List<Salle> pageSalles = salles.subList(fromIndex, toIndex);

            try {
                populateGrid(pageSalles);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return new  Pane();
        });

        list.setBottom(pagination);
    }
    private void updatePagination(List<Salle> salles) {
        // Clear the existing pagination
        list.setBottom(null);

        if (!salles.isEmpty()) {
            // Create a new pagination
            int totalItems = salles.size();
            int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

            pagination = new Pagination(totalPages);
            pagination.setPageFactory(pageIndex -> {
                int fromIndex = pageIndex * ITEMS_PER_PAGE;
                int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, totalItems);
                List<Salle> pageSalles = salles.subList(fromIndex, toIndex);

                try {
                    populateGrid(pageSalles);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return new Pane();
            });

            // Set the new pagination to the list
            list.setBottom(pagination);
        } else {
            // Display a message to the user
            Label message = new Label("No results found");
            list.setBottom(message);
        }
    }}