package SportHub.Controller.MyController;

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

import SportHub.Entity.Category;
import SportHub.Services.CategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static SportHub.Services.CategoryService.getAllCategories;

public class CategoryListController {
    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> descriptionColumn;

    @FXML
    private TableColumn<Category, Void> actionsColumn;
    @FXML
    private Button newCatButton;

    private final CategoryService categoryService;
    private ObservableList<Category> categoriesObservableList;

    public CategoryListController() {
        this.categoryService = new CategoryService();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCategories();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    try {
                        openUpdateForm(category);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    deleteCategory(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsContainer = new HBox(editButton, deleteButton);
                    setGraphic(buttonsContainer);
                }
            }
        });
    }

    private void loadCategories() {
        try {
            List<Category> categories = getAllCategories();
            categoriesObservableList = FXCollections.observableArrayList(categories);
            categoryTableView.setItems(categoriesObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    public void refreshCategoryList() {
        loadCategories();
    }

    private void openUpdateForm(Category selectedCategory) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/UpdateCat.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Set the selected category in the UpdateCategoryController
        UpdateCategoryController controller = loader.getController();
        controller.setSelectedCategory(selectedCategory);

        stage.show();
    }

    private void deleteCategory(Category category) {
        try {
            categoryService.deleteCategory(category.getId());
            categoriesObservableList.remove(category);
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Category Deletion");
                alert.setHeaderText(null);
                alert.setContentText("This category cannot be deleted as it has associated data.");
                alert.showAndWait();
            } else {
                e.printStackTrace();
                // Handle other SQLExceptions if needed
            }
        }
    }

    @FXML
    public void handleOpenNewButton(MouseEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/NewCat.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Pass this controller instance to NewCategoryController
        NewCategoryController newCategoryController = loader.getController();
        newCategoryController.setCategoryListController(this);

        stage.show();
    }
}
