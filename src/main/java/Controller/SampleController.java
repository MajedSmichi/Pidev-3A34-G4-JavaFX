package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

public class SampleController {

    @FXML
    private TilePane HBoxList;
    @FXML
    private ScrollPane userListScrollPane;
    @FXML
    private TextField searchField;

    public void initialize() {

        loadUserListLayout();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
    }

    public void loadUserListLayout() {
        UserCardController.loadUsersIntoCards(HBoxList);
        userListScrollPane.setVisible(true);
    }

    private void filterUsers(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadUserListLayout();
        } else {
            HBoxList.getChildren().clear(); // Clear current cards
            UserCardController.loadFilteredUsersIntoCards(HBoxList, searchTerm);
        }
    }
}
