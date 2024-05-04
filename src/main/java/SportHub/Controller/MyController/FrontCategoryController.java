package SportHub.Controller.MyController;

import SportHub.Entity.MyEntity.Category;
import SportHub.Services.MyServices.CoursService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FrontCategoryController {
    public FlowPane categoryFlowPane;
    @FXML
    private Button showStatsButton;
    private final CoursService coursService;
    private FrontCoursController frontCoursController;

    public FrontCategoryController() {
        this.coursService = new CoursService();
    }

    public void initialize() {
        loadCategoriesAsCards();
    }

    private void loadCategoriesAsCards() {
        try {
            List<Category> categories = coursService.getAllCategories();
            for (Category category : categories) {
                CategoryCardController categoryCardController = new CategoryCardController(this);
                categoryCardController.setCategory(category);
                Pane categoryCard = categoryCardController;

                categoryFlowPane.getChildren().add(categoryCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStatsButton(ActionEvent event) {
        try {
            Map<String, Integer> categoryStats = coursService.getTop5Categories();

            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            barChart.getData().add(series);

            Stage stage = new Stage();
            Scene scene = new Scene(barChart, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFrontCoursController(FrontCoursController frontCoursController) {
        this.frontCoursController = frontCoursController;
    }

    public void navigateToCourses(Category category) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/FrontCours.fxml"));
        AnchorPane pane = loader.load();

        // Get the controller of the FrontCours.fxml
        FrontCoursController frontCoursController = loader.getController();

        // Initialize the FrontCoursController with the selected category
        frontCoursController.initData(category);

        // Replace the content of the current scene with the new pane
        categoryFlowPane.getChildren().setAll(pane);
        showStatsButton.setVisible(false);

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
