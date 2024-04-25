package SportHub.Controller.MyController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import SportHub.Entity.Category;
import SportHub.Services.CategoryService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

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

            {
                editButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    try {
                        openUpdateForm(category);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    private void loadCategories() {
        try {
            List<Category> categories = CategoryService.getAllCategories();
            categoriesObservableList = FXCollections.observableArrayList(categories);
            categoryTableView.setItems(categoriesObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    @FXML
    public void updateCategory(MouseEvent event) {
        Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                // Use reflection to access the private 'type' field
                Field typeField = Category.class.getDeclaredField("type");
                typeField.setAccessible(true);
                String typeValue = (String) typeField.get(selectedCategory);
                selectedCategory.setType(typeValue);

                // Open the update form with the selected category's details
                openUpdateForm(selectedCategory);
            } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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

    @FXML
    public void handleOpenNewButton(MouseEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/NewCat.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
