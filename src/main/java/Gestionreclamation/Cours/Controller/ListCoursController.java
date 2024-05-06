package Gestionreclamation.Cours.Controller;

import Gestionreclamation.Cours.Entity.Cours;
import Gestionreclamation.Cours.Services.CoursService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ListCoursController {
    private final CoursService coursService;
    @FXML
    public TableColumn actionColumn;
    @FXML
    private TableView<Cours> coursTableView;

    @FXML
    private TableColumn<Cours, Integer> idColumn;

    @FXML
    private TableColumn<Cours, String> nameColumn;

    @FXML
    private TableColumn<Cours, String> descriptionColumn;
    @FXML
    TableColumn<Cours, String> coverColumn;
    @FXML
    TableColumn<Cours, String> pdfColumn;
    @FXML
    TableColumn<Cours, String> categoryColumn;


    @FXML
    private Button newCoursButton;

    private ObservableList<Cours> coursObservableList;

    public ListCoursController() {
        this.coursService = new CoursService();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCours();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        coverColumn.setCellValueFactory(cellData -> {
            String coverStatus = cellData.getValue().getCoverImageData() != null ? "Available" : "Not Available";
            return new SimpleStringProperty(coverStatus);
        });

        pdfColumn.setCellValueFactory(cellData -> {
            String pdfStatus = cellData.getValue().getPdfFileData() != null ? "Available" : "Not Available";
            return new SimpleStringProperty(pdfStatus);
        });

        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty().getValue().typeProperty());
        setupActionsColumn();
    }
    public void refreshCoursList() {
        try {
            List<Cours> updatedCoursList = coursService.getAllCours();
            coursTableView.getItems().clear(); // Clear the existing items
            coursTableView.getItems().addAll(updatedCoursList); // Add the updated items
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    private String byteArrayToString(byte[] byteArray) {
        return byteArray != null ? new String(byteArray) : "";
    }


    private void loadCours() {
        try {
            List<Cours> coursList = coursService.getAllCours();
            coursObservableList = FXCollections.observableArrayList(coursList);
            coursTableView.setItems(coursObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    private void setupActionsColumn() {
        actionColumn.setCellFactory(param -> new TableCell<Cours, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    try {
                        openUpdateForm(cours);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    deleteCours(cours);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(editButton, deleteButton));
                }
            }
        });
    }

    private void deleteCours(Cours cours) {
        try {
            coursService.deleteCours(cours.getId());
            coursObservableList.remove(cours);
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Course Deletion");
                alert.setHeaderText(null);
                alert.setContentText("This Course cannot be deleted as it has associated data.");
                alert.showAndWait();
            } else {
                e.printStackTrace();
                // Handle other SQLExceptions if needed
            }
        }
    }

    private void openUpdateForm(Cours selectedCours) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/UpdateCours.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Set the selected cours in the UpdateCourController
        UpdateCoursController controller = loader.getController();
        controller.setSelectedCours(selectedCours);

        stage.show();
    }

    @FXML
    public void handleOpenNewCours(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/NewCours.fxml"));
        Parent root = loader.load();

        // Set this controller instance in NewCoursController
        NewCoursController newCoursController = loader.getController();
        newCoursController.setListCoursController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}


