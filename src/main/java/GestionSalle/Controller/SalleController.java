package GestionSalle.Controller;

import GestionSalle.Entity.Salle;
import GestionSalle.Services.SalleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.scene.control.Pagination;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SalleController implements Initializable {

    @FXML
    private GridPane GridSalle;


    @FXML
    private TextField addresse;

    @FXML
    private Button ajoutSalle;

    @FXML
    private TextField capacite;

    @FXML
    private TextArea description;

    @FXML
    private Button event_import;

    @FXML
    private AnchorPane gestion;

    @FXML
    private Button hideButton;

    @FXML
    private BorderPane list;

    @FXML
    private ImageView logoSalle;

    @FXML
    private TextField name;

    @FXML
    private TextField nbrclients;

    @FXML
    private TextField numtel;

    @FXML
    private AnchorPane root1;

    @FXML
    private Button showButton;
    @FXML
    private TextField search;

    private Pagination pagination;

    @FXML
    private ComboBox<String> filter;

    private static final int ITEMS_PER_PAGE = 6;


    private List<Salle> salles = new ArrayList<>();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            salles = getData();
            createPagination();
            populateGrid(salles);

            // Add sorting options to the filter ComboBox
            // Add sorting options to the filter ComboBox
            filter.getItems().addAll("Trier par nombre de client (ascendant)", "Trier par nombre de client (descendant)", "Par défaut");

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
                    } else {
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

    private void populateGrid(List<Salle> salles) throws IOException {
            GridSalle.getChildren().clear();
            int columns = 0;
            int row = 0; // Start from row 0 for each new page
            for (Salle salle : salles) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneSalle.fxml"));
                Pane pane = fxmlLoader.load(); // Load as Pane
                uneSalleController oneSalle = fxmlLoader.getController();
                oneSalle.setData(salle);
                GridSalle.add(pane, columns, row); // Use the loaded Pane
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

    private List<Salle> getData() throws SQLException {
            List<Salle> salleList = new ArrayList<>();
            SalleService salleService = new SalleService();

            // Assuming getAllSalle() returns a List<Salle>
            for (Salle salle : salleService.getAllSalle()) {
                Salle newSalle = new Salle();
                newSalle.setNom(salle.getNom());
                newSalle.setAddresse(salle.getAddresse());
                newSalle.setNum_tel(salle.getNum_tel());
                newSalle.setLogo_salle(salle.getLogo_salle());
                newSalle.setCapacite(salle.getCapacite());
                newSalle.setDescription(salle.getDescription());
                newSalle.setNbr_client(salle.getNbr_client());
                newSalle.setId(salle.getId());
                // Add more properties if available

                salleList.add(newSalle);
            }

            return salleList;
        }
    @FXML
    private void showGestionPane() {
        gestion.setVisible(true);
    }

    @FXML
    private void hideGestionPane() {
        gestion.setVisible(false);
    }

    @FXML
    void ajouterSalle(ActionEvent event) {
        // Check if the fields are empty
        if (name.getText().isEmpty() || addresse.getText().isEmpty() || numtel.getText().isEmpty() ||
                capacite.getText().isEmpty() || description.getText().isEmpty() || nbrclients.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }
        // Check if description is at least 15 characters
        if (description.getText().length() < 15) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "La description doit contenir au moins 15 caractères.");
            alert.showAndWait();
            return;
        }
        // Check if imagePath is not null
        if (imagePath == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une image.");
            alert.showAndWait();
            return;
        }

        // Check if numtel is a valid number
        try {
            Integer.parseInt(numtel.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer un numéro de téléphone valide.");
            alert.showAndWait();
            return;
        }

        // Check if capacite is a valid integer
        try {
            Integer.parseInt(capacite.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer une capacité valide.");
            alert.showAndWait();
            return;
        }

        // Check if nbrclients is a valid integer
        try {
            Integer.parseInt(nbrclients.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez entrer un nombre valide de clients.");
            alert.showAndWait();
            return;
        }
        SalleService salleService = new SalleService();

        if (!salleService.isUnique(name.getText(), addresse.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Une salle avec le même nom et adresse existe déjà.");
            alert.showAndWait();
            return;
        }


        // If all checks pass, proceed with the rest of the method
        Salle newSalle = new Salle();
        newSalle.setId(1);
        newSalle.setNom(name.getText());
        newSalle.setAddresse(addresse.getText());
        newSalle.setNum_tel(Integer.parseInt(numtel.getText()));
        newSalle.setCapacite(Integer.parseInt(capacite.getText()));
        newSalle.setDescription(description.getText());
        newSalle.setNbr_client(Integer.parseInt(nbrclients.getText()));
        newSalle.setLogo_salle(imagePath);


        try {
            salleService.addEvent(newSalle);
            hideGestionPane();
            refreshGridPane();
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/GestionSalle/Dash.fxml"));
            Parent dash = dashLoader.load();
            DashController dashController = dashLoader.getController();
            dashController.refreshSalle();

            // Clear the fields
            name.clear();
            addresse.clear();
            numtel.clear();
            capacite.clear();
            description.clear();
            nbrclients.clear();
            logoSalle.setImage(null);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshGridPane() throws SQLException {
        GridSalle.getChildren().clear(); // Clear the GridPane
        salles = getData(); // Get the updated data
        int columns = 0;
        int row = 1;
        for (Salle salle : salles) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GestionSalle/uneSalle.fxml"));
            try {
                Pane pane = fxmlLoader.load(); // Load as Pane
                uneSalleController oneSalle = fxmlLoader.getController();
                oneSalle.setData(salle);
                GridSalle.add(pane, columns, row); // Use the loaded Pane
                columns++;
                if (columns > 2) {
                    columns = 0;
                    row++;
                }
                GridPane.setMargin(pane, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String imagePath = null;

    @FXML
    void importimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            logoSalle.setImage(image);
            imagePath = selectedFile.getPath(); // Store the path of the selected file

        }
    }

    private void updatePagination(List<Salle> salles) {
    // Clear the existing pagination
    list.setBottom(null);


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

    }

    private void trierSalle(String option) throws IOException, SQLException {
        if (option.equals("Trier par nom (ascendant)")) {
            // Trier les salles en ordre ascendant
            salles.sort(Comparator.comparing(Salle::getNom));
        } else if (option.equals("Trier par nom (descendant)")) {
            // Trier les salles en ordre descendant
            salles.sort(Comparator.comparing(Salle::getNom).reversed());
        }

        // Mettre à jour la grille avec les salles triées
        populateGrid(salles);
    }


}