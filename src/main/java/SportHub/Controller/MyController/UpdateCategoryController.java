package SportHub.Controller.MyController;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import SportHub.Entity.Category;
import SportHub.Services.CategoryService;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateCategoryController {
    public Button listButton;
    @FXML
    private TextField categoryNameField;

    @FXML
    private TextField categoryDescriptionField;

    @FXML
    private Button updateButton;

    private Category selectedCategory;
    private final CategoryService categoryService;

    public UpdateCategoryController() {
        this.categoryService = new CategoryService();
    }

    public void setSelectedCategory(Category category) {
        this.selectedCategory = category;
        categoryNameField.setText(category.getType());
        categoryDescriptionField.setText(category.getDescription());
    }

    @FXML
    public void updateCategory(MouseEvent event) {
        String name = categoryNameField.getText();
        String description = categoryDescriptionField.getText();

        if (selectedCategory != null) {
            try {
                selectedCategory.setType(name);
                selectedCategory.setDescription(description);
                categoryService.updateCategory(selectedCategory);
                closeWindow();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQLException if needed
            }
        }
    }
    @FXML
    public void handleOpenListButton(MouseEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/ListCat.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void closeWindow() {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}

