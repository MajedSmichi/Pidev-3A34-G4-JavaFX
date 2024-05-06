package Gestionreclamation.Cours.Controller;

import Gestionreclamation.Cours.Entity.Cours;
import Gestionreclamation.Cours.Services.CoursService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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
    @FXML
    private Button sendPdfButton;

    private Cours course;
    private Consumer<Cours> downloadPDFCallback;
    private CoursService coursService;

    public CoursCardController() {
        this.coursService = new CoursService();

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
@FXML
public void sendPdf() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Send PDF");
    dialog.setHeaderText("Enter your email");
    dialog.setContentText("Email:");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(email -> {
        try {
            // Get PDF data
            byte[] pdfData = course.getPdfFileData();

            // Create email content
            String content = "Here is the PDF file of the course " + course.getName().toUpperCase() + ":";

            // Send email
            EmailUtil.sendEmail(email, "PDF File of " + course.getName(), content, pdfData, course.getName() + ".pdf");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("PDF sent successfully!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to send PDF: " + e.getMessage());
            alert.showAndWait();
        }
    });
}}