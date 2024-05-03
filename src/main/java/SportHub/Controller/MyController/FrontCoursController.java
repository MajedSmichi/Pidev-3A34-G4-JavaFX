package SportHub.Controller.MyController;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontCoursController {
    @FXML
    public Label categoryNameLabel;
    @FXML
    public Label noCoursesLabel;
    @FXML
    public FlowPane coursesFlowPane;
    @FXML
    public ScrollPane coursesScrollPane;

    private final CoursService coursService;
    private Category selectedCategory;
    private Stage stage;

    public FrontCoursController() {
        this.coursService = new CoursService();
    }

    public void initData(Category category) {
        this.selectedCategory = category;
        displayCategoryName();
        loadCourses();

    }

    private void displayCategoryName() {
        if (selectedCategory != null) {
            categoryNameLabel.setText(selectedCategory.getType().toUpperCase() + " Courses");
        }
    }

    private void loadCourses() {
        try {
            if (selectedCategory != null) {
                List<Cours> courses = coursService.getCoursesByCategory(selectedCategory.getId());
                if (courses.isEmpty()) {
                    noCoursesLabel.setText("No courses available in this category.");
                } else {
                    displayCourses(courses);
                }
            } else {
                noCoursesLabel.setText("No category selected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCourses(List<Cours> courses) {
    coursesFlowPane.getChildren().clear();
    try {
        for (Cours course : courses) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/CoursCard.fxml"));
            CoursCardController cardController = new CoursCardController();
            loader.setController(cardController);
            Parent card = loader.load();
            cardController.initialize(course, this::downloadPDF);
            coursesFlowPane.getChildren().add(card);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void downloadPDF(Cours course) {
    System.out.println("Download PDF callback triggered");
    byte[] pdfData = course.getPdfFileData();
    if (pdfData != null && pdfData.length > 0) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        // Set the initial file name to the course name plus ".pdf"
        fileChooser.setInitialFileName(course.getName().toUpperCase() + ".pdf");
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
                fos.write(pdfData);
                fos.flush();
            } catch (IOException e) {
                // Show an error message to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to download PDF");
                alert.setContentText("An error occurred while trying to download the PDF. Please try again.");
                alert.showAndWait();
            }
        }
    }
}
    @FXML
    public void goToCategories() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/FrontCategory.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));

        FrontCategoryController newCategoryController = loader.getController();
        newCategoryController.setFrontCoursController(this);

        newStage.show();
    }
}
