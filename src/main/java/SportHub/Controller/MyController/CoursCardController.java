package SportHub.Controller.MyController;

import SportHub.Entity.Cours;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CoursCardController extends AnchorPane {
    @FXML
    private Label coursNameLabel;
    @FXML
    private Label coursDescriptionLabel;
    @FXML
    private ImageView coursCoverImageView;
    @FXML
    private Button downloadButton;

    private Cours cours;

    public CoursCardController() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SportHub/MyFxml/CoursCard.fxml"));
    fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    public void setCours(Cours cours) {
        this.cours = cours;
        coursNameLabel.setText(cours.getName());
        coursDescriptionLabel.setText(cours.getDescription());
        Image image = new Image(cours.getCoverImageData());
        coursCoverImageView.setImage(image);

        downloadButton.setOnAction(event -> downloadPDF());
    }

    private void downloadPDF() {
        try {
            byte[] pdfBytes = java.util.Base64.getDecoder().decode(cours.getPdfFileData());

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.setInitialFileName(cours.getName() + ".pdf");
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(pdfBytes);
                outputStream.close();
            } else {
                System.out.println("File selection was cancelled.");
            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
            System.out.println("Failed to download PDF: " + e.getMessage());
        }
    }
}
