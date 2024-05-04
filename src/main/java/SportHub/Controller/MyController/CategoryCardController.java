package SportHub.Controller.MyController;

import SportHub.Entity.MyEntity.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CategoryCardController extends AnchorPane {
    @FXML
    private Label categoryNameLabel;
    @FXML
    private Label categoryDescriptionLabel;
    @FXML
    private ImageView categoryImageView;
    @FXML
    private Button exploreButton;

    private Category category;
    private FrontCategoryController frontCategoryController;

    public CategoryCardController(FrontCategoryController frontCategoryController) {
        this.frontCategoryController = frontCategoryController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/CategoryCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setCategory(Category category) {
        this.category = category;
        categoryNameLabel.setText(category.getType());
        categoryDescriptionLabel.setText(category.getDescription());
        // Assuming you have images stored somewhere, set the image accordingly
        Image image = new Image("https://acpe.edu.au/uploads/2023/08/Course-category-icon_Sport-Coaching.png");
        categoryImageView.setImage(image);
    }

    @FXML
    private void handleExploreButtonAction() {
        frontCategoryController.navigateToCourses(category);
    }
}