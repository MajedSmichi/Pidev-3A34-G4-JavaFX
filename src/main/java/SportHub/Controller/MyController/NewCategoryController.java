package SportHub.Controller.MyController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import SportHub.Entity.MyEntity.Category;
import SportHub.Services.MyServices.CategoryService;

import java.io.IOException;
import java.sql.SQLException;

public class NewCategoryController {
    @FXML
    private Button addButton;
    @FXML
    private TextField categoryNameField;
    @FXML
    private TextField categoryDescriptionField;

    private final CategoryService categoryService;
    private CategoryListController categoryListController;

    public void setCategoryListController(CategoryListController categoryListController) {
        this.categoryListController = categoryListController;
    }

    public NewCategoryController() {
        this.categoryService = new CategoryService();
    }

    @FXML
    public void addCategory(MouseEvent event) {
        String name = categoryNameField.getText().trim();
        String description = categoryDescriptionField.getText().trim();

        if (name.isEmpty() || description.isEmpty()) {
            showAlert("Error", "Please enter both name and description.");
            return;
        }

        try {
            if (categoryService.categoryExists(name)) {
                showAlert("Error", "Category with this name already exists.");
                return;
            }

            Category category = new Category(name, description);
            categoryService.addCategory(category);
            if (categoryListController != null) {
                categoryListController.refreshCategoryList();
            }
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleOpenListButton(MouseEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SportHub.MyFxml.ListCat.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
