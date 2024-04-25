package Gestionreclamation.Controller;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Services.ReponseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReponseController implements Initializable {
    @FXML
    private GridPane GridReponse;

    @FXML
    private BorderPane borderpaneReponse;
    @FXML
    private TextField SearchRep;

    private Pagination pagination;
    private static final int ITEMS_PER_PAGE = 3;


    private List<Reponse> reponses = new ArrayList<>();

    public BorderPane getBorderPane() {
        return borderpaneReponse;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            reponses = getData();
            createPagination();
            populateGrid(reponses);

            // Add a listener to the SearchRep TextField
            SearchRep.textProperty().addListener((observable, oldValue, newValue) -> {
                // Filter the reponses list based on the search text
                List<Reponse> filteredReponses = reponses.stream()
                        .filter(rep -> rep.getidReclamation().getNom().toLowerCase().contains(newValue.toLowerCase())
                                || rep.getidReclamation().getPrenom().toLowerCase().contains(newValue.toLowerCase())
                                || rep.getReponse().toLowerCase().contains(newValue.toLowerCase())
                                || rep.getDate().toString().contains(newValue))
                        .collect(Collectors.toList());
                try {
                    populateGrid(filteredReponses);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createPagination() {
        int totalItems = reponses.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        pagination = new Pagination(totalPages);
        pagination.setPageFactory(pageIndex -> {
            try {
                int fromIndex = pageIndex * ITEMS_PER_PAGE;
                int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, totalItems);
                List<Reponse> pageReponses = reponses.subList(fromIndex, toIndex);
                populateGrid(pageReponses);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Return an empty Pane
            return new Pane();
        });

        borderpaneReponse.setBottom(pagination);
    }


    private void populateGrid(List<Reponse> reponses) throws IOException {
        GridReponse.getChildren().clear();
        int columns = 0;
        int row = 1;
        for (Reponse rep : reponses) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/OneReponse.fxml"));
            Pane pane = fxmlLoader.load(); // Load as Pane
            OneReponse oneReponse = fxmlLoader.getController();
            oneReponse.setData(rep);
            oneReponse.setReponseController(this); // Set the ReponseController instance

            GridReponse.add(pane, columns, row); // Use the loaded Pane
            columns++;
            if (columns > 2) {
                columns = 0;
                row++;
            }
            GridPane.setMargin(pane, new Insets(10));
        }
    }

    private List<Reponse> getData() throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        ReponseService reponseService = new ReponseService();
        for (Reponse rep : reponseService.afficherReponse()) {
            Reponse newRep = new Reponse();
            newRep.setId(rep.getId());
            newRep.setUtilisateur(rep.getUtilisateur());
            newRep.setReponse(rep.getReponse());
            newRep.setDate(rep.getDate());
            newRep.setidReclamation(rep.getidReclamation());
            reponseList.add(newRep);
        }
        return reponseList;
    }
}