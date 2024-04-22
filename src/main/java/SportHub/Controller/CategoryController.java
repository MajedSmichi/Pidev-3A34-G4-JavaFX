package SportHub.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import SportHub.Entity.Category;
import SportHub.Services.CategoryService;

import java.sql.SQLException;
import java.util.List;

public class CategoryController {
    @FXML
    private Button category_add;

    @FXML
    private TextField category_name;

    @FXML
    private TextField category_description;

    private final CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService();
    }

    @FXML
    public void initialize() {
        // Initialize any UI components or data here
    }

    @FXML
    public void addCategory(MouseEvent event) {
        String name = category_name.getText();
        String description = category_description.getText();

        try {
            Category category = new Category(name, description);
            categoryService.addCategory(category);
            // Optionally, you can update UI or perform any other actions after adding the category
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    public void updateCategory(int id, String name, String description) {
        try {
            Category category = new Category(id, name, description);
            categoryService.updateCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Category> getAllCategories() {
        try {
            return categoryService.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
