package SportHub.Controller.MyController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class NewCoursController {
    @FXML
    private Button addButton1;
    @FXML
    private Button coverUploadButton;
    @FXML
    private Button pdfUploadButton;

    @FXML
    private TextField coursNameField;

    @FXML
    private TextField coursDescriptionField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private Button addCoursButton;

    private final CoursService coursService;
    private final CategoryService categoryService;
    private ListCoursController listCoursController;


    private File pdfFile;
    private File coverImageFile;

    public NewCoursController() {
        this.coursService = new CoursService();
        this.categoryService = new CategoryService();
    }
    public void setListCoursController(ListCoursController listCoursController) {
        this.listCoursController = listCoursController;
    }

    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            categoryChoiceBox.setItems(FXCollections.observableArrayList(categories));
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void handleSQLException(SQLException e) {
        System.err.println("Error loading categories: " + e.getMessage());
    }

    private byte[] convertFileToByteArray(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
            fis.close();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @FXML
    public void addCours(MouseEvent event) {
        String name = coursNameField.getText().trim();
        String description = coursDescriptionField.getText().trim();
        Category selectedCategory = categoryChoiceBox.getValue();

        if (name.isEmpty() || description.isEmpty() || selectedCategory == null) {
            showAlert("Error", "Please fill in all fields and select a category.");
            return;
        }

        try {
            if (coursService.courseExists(name)) {
                showAlert("Error", "Course with this name already exists.");
                return;
            }

            if (coursService.courseNameExistsInCategory(name, selectedCategory.getId())) {
                showAlert("Error", "Course with this name already exists in the selected category.");
                return;
            }
            byte[] pdfFileData = pdfFile != null ? convertFileToByteArray(pdfFile) : new byte[0];
            byte[] coverImageData = coverImageFile != null ? convertFileToByteArray(coverImageFile) : new byte[0];

            Cours newCours = new Cours(name, description, pdfFileData, coverImageData, selectedCategory, selectedCategory.getId());

            coursService.addCours(newCours);
            closeWindow();

            if (listCoursController != null) {
                listCoursController.refreshCoursList(); // Refresh the list of courses after adding
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void refreshCoursList() {
        List<Cours> coursList;
        try {
            coursList = coursService.getAllCours();
            // You can update the UI with the refreshed course list here
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @FXML
    public void handlePdfUpload(MouseEvent event) {
        pdfFile = chooseFile("Upload PDF File");
    }

    @FXML
    public void handleCoverUpload(MouseEvent event) {
        coverImageFile = chooseFile("Upload Cover Image");
    }

    private File chooseFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(addCoursButton.getScene().getWindow());
    }

    @FXML
    public void handleOpenListButton(MouseEvent actionEvent) {
        loadFXML("/SportHub/MyFxml/ListCours.fxml");
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addCoursButton.getScene().getWindow();
        stage.close();
        refreshCoursList(); // Refresh the list before closing the window
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
