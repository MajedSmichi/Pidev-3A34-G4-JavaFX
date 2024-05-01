package SportHub.Controller.MyController;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

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

    private final CoursService coursService;
    private Category selectedCategory;

    public FrontCoursController() {
        this.coursService = new CoursService();
    }

    @FXML
    public void initData(Category category) {
        this.selectedCategory = category;
        displayCategoryName();
        loadCourses();
    }

    @FXML
    private void displayCategoryName() {
        if (selectedCategory != null) {
            categoryNameLabel.setText(selectedCategory.getType().toUpperCase() + " Courses");
        }
    }

    @FXML
    private void loadCourses() {
     try {
        if (selectedCategory != null) {
            List<Cours> courses = coursService.getCoursesByCategory(selectedCategory.getId());
            if (courses.isEmpty()) {
                noCoursesLabel.setText("No courses available in this category.");
            } else {
                for (Cours course : courses) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/CoursCard.fxml"));
                    AnchorPane card = loader.load();
                    CoursCardController coursCardController = loader.getController();
                    coursCardController.setCours(course);
                    coursesFlowPane.getChildren().add(card);
                }
            }
        } else {
            noCoursesLabel.setText("No category selected.");
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}
}