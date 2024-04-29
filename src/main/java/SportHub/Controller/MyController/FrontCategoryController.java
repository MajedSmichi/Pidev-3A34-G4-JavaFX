package SportHub.Controller.MyController;

import SportHub.Entity.Category;
import SportHub.Services.CoursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class FrontCategoryController {
    public Button backToCategoriesButton;
    public TableView<Category> categoryTableView;
    public TableColumn<Category, String> categoryNameColumn;
    public TableColumn<Category, String> categoryDescriptionColumn;
    public TableColumn<Category, Void> exploreColumn; // Added Explore column

    private final CoursService coursService;
    private FrontCoursController frontCoursController;

    public FrontCategoryController() {
        this.coursService = new CoursService();
    }

    public void initialize() {
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Set up the Explore column
        exploreColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button exploreButton = new Button("Explore Category!");

            {
                exploreButton.setOnAction((ActionEvent event) -> {
                    Category category = getTableView().getItems().get(getIndex());
                    if (frontCoursController != null) {
                        frontCoursController.initData(category);
                        Stage stage = (Stage) backToCategoriesButton.getScene().getWindow();
                        stage.close();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(exploreButton);
                }
            }
        });

        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Category> categories = coursService.getAllCategories();
            ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categories);
            categoryTableView.setItems(categoryObservableList);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
    @FXML
    private void handleStatsButton(ActionEvent event) {
    try {
        // Fetch the data from the database
        Map<String, Integer> categoryStats = coursService.getTop5Categories();

        // Create a dataset for the bar chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        // Create a new stage and scene to display the bar chart
        Stage stage = new Stage();
        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        // Handle exception as needed
    }
}

    public void setFrontCoursController(FrontCoursController frontCoursController) {
        this.frontCoursController = frontCoursController;
    }

    public void setNavigationButton(Button navigationButton) {
        this.backToCategoriesButton = navigationButton;
    }
}
