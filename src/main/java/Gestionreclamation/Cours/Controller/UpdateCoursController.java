package Gestionreclamation.Cours.Controller;

import Gestionreclamation.Cours.Entity.Cours;
import Gestionreclamation.Cours.Entity.Category;
import Gestionreclamation.Cours.Services.CategoryService;
import Gestionreclamation.Cours.Services.CoursService;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UpdateCoursController {
    @FXML
    public Button addButton1;
    @FXML
    private TextField coursNameField;

    @FXML
    private TextField coursDescriptionField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    @FXML
    private Button updateCoursButton;
    @FXML
    private Button coverUploadButton;
    @FXML
    private Button pdfUploadButton;

    private Cours selectedCours;
    private final CoursService coursService;
    private final CategoryService categoryService;

    private File pdfFile;
    private File coverImageFile;

    public UpdateCoursController() {
        this.coursService = new CoursService();
        this.categoryService = new CategoryService();
    }

    public void setSelectedCours(Cours cours) {
        this.selectedCours = cours;
        coursNameField.setText(cours.getName());
        coursDescriptionField.setText(cours.getDescription());

        // Set other fields as needed
    }

// ...

private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
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
            if (selectedCours != null) {
                categoryChoiceBox.setValue(selectedCours.getCategory());
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void handleSQLException(SQLException e) {
        // Display error message to the user or log the error
        System.err.println("Error loading categories: " + e.getMessage());
    }

    @FXML
public void updateCours(MouseEvent event) {
    String name = coursNameField.getText();
    String description = coursDescriptionField.getText();
    Category selectedCategory = categoryChoiceBox.getValue();

    if (name.isEmpty() || description.isEmpty() || selectedCategory == null) {
        showAlert("Error", "Please fill in all fields and select a category.");
        return;
    }

    try {
        if (coursService.courseExists(name) && !name.equals(selectedCours.getName())) {
            showAlert("Error", "Course with this name already exists.");
            return;
        }

        if (coursService.courseNameExistsInCategory(name, selectedCategory.getId()) && !name.equals(selectedCours.getName())) {
            showAlert("Error", "Course with this name already exists in the selected category.");
            return;
        }

        selectedCours.setName(name);
        selectedCours.setDescription(description);
        selectedCours.setCategory(selectedCategory);

        if (pdfFile != null) {
            byte[] pdfFileData = convertFileToByteArray(pdfFile);
            selectedCours.setPdfFileData(pdfFileData);
        }

        if (coverImageFile != null) {
            byte[] coverImageData = convertFileToByteArray(coverImageFile);
            selectedCours.setCoverImageData(coverImageData);
        }

        coursService.updateCours(selectedCours);
        closeWindow();
    } catch (SQLException e) {
        handleSQLException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    private byte[] convertFileToByteArray(File file) throws IOException {
        byte[] fileData = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(fileData);
        fileInputStream.close();
        return fileData;
    }

    private boolean validateInputs(String name, String description, int categoryId) {
        if (categoryId <= 0 || pdfFile == null || coverImageFile == null) {
            System.out.println("Please select category and upload both PDF and cover image.");
            return false;
        }
        return true;
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
        return fileChooser.showOpenDialog(updateCoursButton.getScene().getWindow());
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
            // Handle FXML loading error
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) updateCoursButton.getScene().getWindow();
        stage.close();
    }
}
