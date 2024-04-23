package SportHub.Controller;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CategoryService;
import SportHub.Services.CoursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

public class CoursController {
    private final CoursService coursService;
    private final CategoryService categoryService;

    @FXML
    private ChoiceBox<Category> cours_category;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtDescription;

    private File pdfFile;
    private File coverFile;
    public CoursController() {
        this.coursService = new CoursService();
        this.categoryService = new CategoryService();
    }

    @FXML
    public void initialize() {
        populateCategories();
    }

    private void populateCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
            cours_category.setItems(categoryList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleUploadPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose PDF File");
        pdfFile = fileChooser.showOpenDialog(new Stage());
        if (pdfFile != null) {
            // You can update a label or display the file name somewhere in your UI if needed
            System.out.println("Selected PDF file: " + pdfFile.getName());
        }
    }

    @FXML
    public void handleUploadCover() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Cover Image File");
        coverFile = fileChooser.showOpenDialog(new Stage());
        if (coverFile != null) {
            // You can update a label or display the file name somewhere in your UI if needed
            System.out.println("Selected cover image file: " + coverFile.getName());
        }
    }

    @FXML
    public void addCours() {
        String name = txtName.getText();
        String description = txtDescription.getText();
        if (name.isEmpty() || description.isEmpty() || pdfFile == null || coverFile == null || cours_category.getValue() == null) {
            System.out.println("Please fill in all fields and select files");
            return;
        }

        try {
            byte[] pdfFileData = Files.readAllBytes(pdfFile.toPath());
            byte[] coverImageData = Files.readAllBytes(coverFile.toPath());
            Category category = cours_category.getValue();
            Cours cours = new Cours(name, description, pdfFileData, coverImageData, category);
            coursService.addCours(cours);
            System.out.println("Course added successfully!");
            // You can reset fields or display a success message in your UI
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCours(int id, String name, String description, java.lang.Byte[] pdfFileData, java.lang.Byte[] coverImageData, Category category) {
        try {
            Cours cours = new Cours(id, name, description, pdfFileData, coverImageData, category);
            coursService.updateCours(cours);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCours(int courseId) {
        try {
            coursService.deleteCours(courseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cours> getAllCours() {
        try {
            return coursService.getAllCours();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
