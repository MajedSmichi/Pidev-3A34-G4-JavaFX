package SportHub.Controller.MyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import SportHub.Services.CategoryService;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class NewCoursController {
    @FXML
    private TextField coursNameField;

    @FXML
    private TextField coursDescriptionField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private Button addCoursButton;

    private final CoursService coursService;

    public NewCoursController() {
        this.coursService = new CoursService();
    }

    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Category> categories = CategoryService.getAllCategories();
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
            categoryChoiceBox.setItems(categoryList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception if needed
        }
    }

    @FXML
    public void addCours(MouseEvent event) {
        String name = coursNameField.getText();
        String description = coursDescriptionField.getText();
        Category selectedCategory = categoryChoiceBox.getValue();

        try {
            if (selectedCategory != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Upload PDF File");
                File pdfFile = fileChooser.showOpenDialog(addCoursButton.getScene().getWindow());

                fileChooser.setTitle("Upload Cover Image");
                File coverImageFile = fileChooser.showOpenDialog(addCoursButton.getScene().getWindow());

                String pdfFilePath = pdfFile != null ? pdfFile.getAbsolutePath() : null;
                String coverImageFilePath = coverImageFile != null ? coverImageFile.getAbsolutePath() : null;

                Cours newCours = new Cours(name, description, pdfFilePath, coverImageFilePath, selectedCategory);
                coursService.addCours(newCours);
                closeWindow();
            } else {
                // Handle case where no category is selected
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addCoursButton.getScene().getWindow();
        stage.close();
    }
}
