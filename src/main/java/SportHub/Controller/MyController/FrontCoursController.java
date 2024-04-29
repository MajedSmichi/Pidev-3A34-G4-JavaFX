package SportHub.Controller.MyController;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class FrontCoursController {
    @FXML
    public Button backToCategoriesButton;
    @FXML
    public Label categoryNameLabel;
    @FXML
    public Label noCoursesLabel;
    @FXML
    public TableView<Cours> coursesTable;
    @FXML
    public TableColumn<Cours, String> nameColumn;
    @FXML
    public TableColumn<Cours, String> descriptionColumn;
    @FXML
    public TableColumn<Cours, String> pdfDownloadColumn;
    @FXML
    public TableColumn<Cours, ImageView> coverColumn;

    private final CoursService coursService;
    private Category selectedCategory;
    private Stage stage;
    @FXML
    private MouseEvent actionEvent;

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

    private ImageView loadImage(String imagePath) {
    try {
        Image image = new Image(new FileInputStream(imagePath));
        return new ImageView(image);
    } catch (IOException e) {
        e.printStackTrace();
        return new ImageView(); // Return an empty ImageView if image loading fails
    }
}

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        coverColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(loadImage(cellData.getValue().getCoverImageData())));

        // Set up the PDF download button column
        pdfDownloadColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Download"));
        pdfDownloadColumn.setCellFactory(column -> {
            Button downloadButton = new Button("Download");
            return new TableCell<Cours, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        downloadButton.setOnAction(event -> {
                            Cours cours = getTableView().getItems().get(getIndex());
                            downloadPDF(cours);
                        });
                        setGraphic(downloadButton);
                    }
                }
            };
        });

        // Load courses on initialization
        loadCourses();
    }
    @FXML
    private void loadCourses() {
        try {
            if (selectedCategory != null) { // Add null check here
                List<Cours> courses = coursService.getCoursesByCategory(selectedCategory.getId());
                if (courses.isEmpty()) {
                    noCoursesLabel.setText("No courses available in this category.");
                } else {
                    coursesTable.getItems().setAll(courses);
                }
            } else {
                noCoursesLabel.setText("No category selected."); // Handle case where category is null
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException if needed
        }
    }

@FXML
private void downloadPDF(Cours course) {
    try {
        // Convert PDF data from base64 to bytes
        byte[] pdfBytes = java.util.Base64.getDecoder().decode(course.getPdfFileData());

        // Choose where to save the PDF file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.setInitialFileName(course.getName().toUpperCase() + ".pdf");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Save the PDF file
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(pdfBytes);
            outputStream.close();
        } else {
            System.out.println("File selection was cancelled.");
        }
    } catch (IllegalArgumentException e) {
        System.out.println("Failed to decode base64 PDF data: " + e.getMessage());
    } catch (FileNotFoundException e) {
        System.out.println("Failed to create output file: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Failed to write to output file: " + e.getMessage());
    }
}

    @FXML
    public void GotoCategories(MouseEvent actionEvent) throws IOException {
        this.actionEvent = actionEvent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/FrontCategory.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));

        // Pass this controller instance to FrontCategoryController
        FrontCategoryController newCategoryController = loader.getController();
        newCategoryController.setFrontCoursController(this);

        newStage.show();
    }
}