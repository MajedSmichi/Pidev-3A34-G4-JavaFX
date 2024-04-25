package SportHub.Controller.MyController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListCoursController {
    @FXML
    private TableView<Cours> coursTableView;

    @FXML
    private TableColumn<Cours, Integer> idColumn;

    @FXML
    private TableColumn<Cours, String> nameColumn;

    @FXML
    private TableColumn<Cours, String> descriptionColumn;

    @FXML
    private Button newCoursButton;

    private final CoursService coursService;
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

    @FXML
    public void handleOpenNewCours(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/NewCours.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
