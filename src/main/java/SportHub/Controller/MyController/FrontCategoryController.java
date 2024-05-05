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
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrontCategoryController {
    public FlowPane categoryFlowPane;
    @FXML
    private Button showStatsButton;
    private final CoursService coursService;
    private FrontCoursController frontCoursController;
    public Pagination pagination;
    private List<Category> categories;
    private static final int ITEMS_PER_PAGE = 3;
    private AnchorPane achorfront;

    public FrontCategoryController() {
        this.coursService = new CoursService();
    }

    public void initialize() {
        categories = new ArrayList<>();
        loadCategoriesAsCards();
        pagination.setPageFactory(this::createPage);
    }

    private void loadCategoriesAsCards() {
        try {
            categories = coursService.getAllCategories();
            pagination.setPageCount((categories.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, categories.size());
        categoryFlowPane.getChildren().clear();
        for (Category category : categories.subList(fromIndex, toIndex)) {
            CategoryCardController categoryCardController = new CategoryCardController(this);
            categoryCardController.setCategory(category);
            Pane categoryCard = categoryCardController;
            categoryFlowPane.getChildren().add(categoryCard);
        }
        return new VBox(categoryFlowPane);
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



    // ...

    // ...

    public void setAchorfront(AnchorPane achorfront) {
        this.achorfront = achorfront;
    }

    // ...

    public void navigateToCourses(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/FrontCours.fxml"));
            AnchorPane pane = loader.load();

            // Get the controller of the FrontCours.fxml
            FrontCoursController frontCoursController = loader.getController();

            // Initialize the FrontCoursController with the selected category
            frontCoursController.initData(category);

            // Set achorfront to the FrontCoursController
            frontCoursController.setAchorfront(achorfront);

            // Replace the content of achorfront with the new pane
            achorfront.getChildren().setAll(pane);
            showStatsButton.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}