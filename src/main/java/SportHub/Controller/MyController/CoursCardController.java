package SportHub.Controller.MyController;

import SportHub.Entity.Cours;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.function.Consumer;

public class CoursCardController {
    @FXML
    private ImageView coverImage;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button downloadButton;

    private Cours course;
    private Consumer<Cours> downloadPDFCallback;

    public CoursCardController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/CoursCard.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize(Cours course, Consumer<Cours> downloadPDFCallback) {
        this.course = course;
        this.downloadPDFCallback = downloadPDFCallback;
        courseNameLabel.setText(course.getName());
        descriptionLabel.setText(course.getDescription());
        setCoverImageFromFilePath(course.getCoverImageData());
    }

    private void setCoverImageFromFilePath(String filePath) {
        try {
            Image image = new Image(filePath);
            coverImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void downloadPDF() {
        if (downloadPDFCallback != null && course != null) {
            downloadPDFCallback.accept(course);
        }
    }
}