package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

public class SampleController implements UserCardRefreshListener {

    @FXML
    private TilePane HBoxList;
    @FXML
    private ScrollPane userListScrollPane;
    @FXML
    private TextField searchField;

    public void initialize() {
        // Uncomment the following line if you want to load the user list layout on initialization
        // loadUserListLayout();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
    }

    public void loadUserListLayout() {
        UserCardController.loadUsersIntoCards(HBoxList, this); // Pass 'this' as UserCardRefreshListener
        userListScrollPane.setVisible(true);
        searchField.setVisible(true);
    }

    @Override
    public void refreshUserList() {
        loadUserListLayout();
    }

    private void filterUsers(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadUserListLayout();
        } else {
            HBoxList.getChildren().clear();
            UserCardController.loadFilteredUsersIntoCards(HBoxList, searchTerm, this); // Pass 'this' as UserCardRefreshListener
        }
    }

}
