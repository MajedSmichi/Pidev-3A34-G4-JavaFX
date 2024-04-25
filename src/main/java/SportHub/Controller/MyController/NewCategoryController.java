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

public class NewCategoryController {
    @FXML
    public Button addButton1;
    @FXML
    private TextField categoryNameField;

    @FXML
    private TextField categoryDescriptionField;

    @FXML
    private Button addButton;

    private final CategoryService categoryService;

    public NewCategoryController() {
        this.categoryService = new CategoryService();
    }

    @FXML
    public void addCategory(MouseEvent event) {
        String name = categoryNameField.getText();
        String description = categoryDescriptionField.getText();

        try {
            Category category = new Category(name, description);
            categoryService.addCategory(category);
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
    @FXML
    public void handleOpenListButton(MouseEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SportHub.MyFxml.ListCat.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

