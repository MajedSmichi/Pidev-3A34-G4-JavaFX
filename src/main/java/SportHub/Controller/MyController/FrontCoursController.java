package SportHub.Controller.MyController;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
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
        try {
            File pdfFile = new File(course.getPdfFileData());
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
