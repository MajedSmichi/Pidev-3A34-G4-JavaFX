package SportHub.Controller;

import SportHub.Entity.Categorie_p;
import SportHub.Services.Servicecategorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

public class CategorieController {

    @FXML
    private TextField categorie_name;

    @FXML
    private TableView<Categorie_p> categorieTableView;

    @FXML
    private TableColumn<Categorie_p, String> col_categorie_name;

    private Servicecategorie categorieService;

    public CategorieController() {
        categorieService = new Servicecategorie();
    }

    public void categorieAdd() {
        try {
            if (!categorie_name.getText().isEmpty()) {
                Categorie_p categorie = new Categorie_p();
                categorie.setName(categorie_name.getText());
                categorieService.ajouter(categorie);
                showListCategorie();
                showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Successfully Added!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Le nom de la catégorie est requis");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Erreur lors de l'ajout de la catégorie");
        }
    }

    public void categorieDelete() {
        Categorie_p categorie = categorieTableView.getSelectionModel().getSelectedItem();
        if (categorie != null) {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer la catégorie : " + categorie.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    categorieService.supprimer(categorie.getId());
                    showListCategorie();
                    showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Supprimé avec succès!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Erreur lors de la suppression de la catégorie");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Veuillez sélectionner une catégorie à supprimer");
        }
    }

    public void showListCategorie() {
        ObservableList<Categorie_p> categorieList = FXCollections.observableArrayList(categorieService.getAllC());
        col_categorie_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        categorieTableView.setItems(categorieList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void initialize() throws SQLException {
        if (categorieTableView != null) {
            showListCategorie();

            // Ajouter un écouteur de changement au modèle de sélection de la TableView
            categorieTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Obtenir la catégorie sélectionnée
                    Categorie_p selectedCategorie = newValue;

                    // Définir les détails de la catégorie dans les champs de mise à jour
                    categorie_name.setText(selectedCategorie.getName());
                }
            });
        }
    }
    public void categorieUpdate() {
        Categorie_p categorie = categorieTableView.getSelectionModel().getSelectedItem();
        if (categorie != null) {
            try {
                if (!categorie_name.getText().isEmpty()) {
                    categorie.setName(categorie_name.getText());
                    categorieService.modifier(categorie);
                    showListCategorie();
                    showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Mise à jour réussie!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error Message", null, "Le nom de la catégorie est requis");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Erreur lors de la mise à jour de la catégorie");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Veuillez sélectionner une catégorie à mettre à jour");
        }
    }

    public void categorieClear() {
        categorie_name.setText("");
    }
}
