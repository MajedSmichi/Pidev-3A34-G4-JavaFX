package SportHub.Controller.MyController;

import SportHub.Entity.Cours;
import SportHub.Services.CoursService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
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
    private CoursService coursService; // Add this line


    @FXML
    private Button sendEmailButton;

    //@FXML
    /*private void handleSendEmailButtonAction() {
    System.out.println("Send to Email button clicked");
    if (course != null) {
        // Show a dialog to the user to enter the email address
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Send PDF to Email");
        dialog.setHeaderText("Enter the email address to send the PDF to:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            try {
                // Send the PDF to the entered email address
                coursService.sendPdfToEmail(course, email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    }*/
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
        setCoverImage(course.getCoverImageData());
    }

    private void setCoverImage(byte[] imageData) {
        try {
            Image image = new Image(new ByteArrayInputStream(imageData));
            coverImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void downloadPDF() {
    System.out.println("Download PDF button clicked");
    if (downloadPDFCallback != null && course != null) {
        downloadPDFCallback.accept(course);
    }
}
}
