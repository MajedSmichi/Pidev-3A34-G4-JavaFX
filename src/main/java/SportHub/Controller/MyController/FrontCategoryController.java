package SportHub.Controller.MyController;

import SportHub.Entity.Category;
import SportHub.Services.CoursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

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

    public void setFrontCoursController(FrontCoursController frontCoursController) {
        this.frontCoursController = frontCoursController;
    }

    public void setNavigationButton(Button navigationButton) {
        this.backToCategoriesButton = navigationButton;
    }
}
