package SportHub.Controller.MyController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import SportHub.Entity.Cours;
import SportHub.Services.CoursService;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateCoursController {
    @FXML
    private TextField coursNameField;

    @FXML
    private TextField coursDescriptionField;

    @FXML
    private Button updateCoursButton;

    private Cours selectedCours;
    private final CoursService coursService;

    public UpdateCoursController() {
        this.coursService = new CoursService();
    }

    public void setSelectedCours(Cours cours) {
        this.selectedCours = cours;
        coursNameField.setText(cours.getName());
        coursDescriptionField.setText(cours.getDescription());
        // Set other fields as needed
    }

    @FXML
    public void updateCours(MouseEvent event) {
        String name = coursNameField.getText();
        String description = coursDescriptionField.getText();
        // Retrieve category information if needed

        if (selectedCours != null) {
            try {
                // Update selectedCours with new data and call service method to update
                selectedCours.setName(name);
                selectedCours.setDescription(description);
                // Update category and other fields if necessary

                coursService.updateCours(selectedCours);
                closeWindow();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQLException if needed
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) updateCoursButton.getScene().getWindow();
        stage.close();
    }
}
