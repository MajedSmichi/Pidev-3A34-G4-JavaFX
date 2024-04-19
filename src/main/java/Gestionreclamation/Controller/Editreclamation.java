package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Editreclamation implements Initializable {


    @FXML
    private ComboBox<String> combosujet;

    @FXML
    private Label fullname;

    @FXML
    private TextArea textdescription;

    @FXML
    private Label choixEditError;

    @FXML
    private Label desEditError;

    private Reclamation rec; // Assuming Reclamation class exists
    private AnchorPane detailanchpane; // Declare detailanchpane here

    public void setDetailAnchorPane(AnchorPane detailanchpane) {
        this.detailanchpane = detailanchpane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the ComboBox items
        combosujet.getItems().addAll("Salle", "Service");
    }

    public void setData(Reclamation rec) {
        this.rec = rec;
        // Populate UI elements with Reclamation data
        textdescription.setText(rec.getDescription());
        fullname.setText(rec.getNom() + " " + rec.getPrenom());
        combosujet.setValue(rec.getSujet());
    }
    @FXML
    private void handleEditButtonClick() throws Exception{
        ReclamationService s = new ReclamationService();

        if (s == null || rec == null) {
            System.err.println("Service or Reclamation is null");
            System.out.println(rec);
            return;
        }

        // Check if the textdescription and combosujet fields are not empty
        if (textdescription.getText().isEmpty()) {
            desEditError.setText("Please enter a description.");
            return;
        } else {
            desEditError.setText(""); // Clear the error message
        }

        if (combosujet.getValue() == null || combosujet.getValue().isEmpty()) {
            choixEditError.setText("Please select a subject.");
            return;
        } else {
            choixEditError.setText(""); // Clear the error message
        }

        rec.setDescription(textdescription.getText());
        rec.setSujet(combosujet.getValue());
        s.modifierReclamation(rec);

        Alert a = new Alert(Alert.AlertType.INFORMATION, "votre reclamation est modifier ");
        a.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/DetailReclamation.fxml"));
        Parent detailReclamationView = loader.load();
        DetailReclamation detailReclamationController = loader.getController();
        detailReclamationController.setData(rec); // Passer la réclamation modifiée

        // Remplacer le contenu de detailanchpane avec la vue de détail actualisée
        detailanchpane.getChildren().setAll(detailReclamationView);
    }
}
