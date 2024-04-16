package Controller;

import Entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static Controller.UserController.selectAllUsers;

public class UserCardController {
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label userEmail;
    @FXML private Label userPhone;
    @FXML private Button deleteButton;
    private User currentUser;
    private UserCardRefreshListener refreshListener;

    public void setUserData(User user) {
        this.currentUser = user;
        userName.setText(user.getNom() + " " + user.getPrenom());
        userEmail.setText(user.getEmail());
        userPhone.setText(String.valueOf(user.getNumTele()));

        // Check if the avatar URL is not null and is a valid URL or file path
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            try {
                // Get the URL of the avatar image
                URL avatarUrlResource = getClass().getResource(avatarUrl);
                if (avatarUrlResource != null) {
                    userImage.setImage(new Image(avatarUrlResource.toExternalForm()));
                } else {
                    // If the URL is invalid, set a default image
                    userImage.setImage(new Image(getClass().getResource("/avatars/default.jpg").toExternalForm()));
                }
            } catch (IllegalArgumentException e) {
                // If the URL is invalid, set a default image
                userImage.setImage(new Image(getClass().getResource("/avatars/default.jpg").toExternalForm()));
            }
        } else {
            // If the URL is null, set a default image
            userImage.setImage(new Image(getClass().getResource("/avatars/default.jpg").toExternalForm()));
        }
    }

    public static TilePane loadUserCard(User user, UserCardRefreshListener listener) {
        try {
            FXMLLoader loader = new FXMLLoader(UserCardController.class.getResource("UserCrud/userList.fxml"));
            TilePane card = loader.load();

            UserCardController cardController = loader.getController();
            cardController.setUserData(user);
            cardController.setRefreshListener(listener);
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setRefreshListener(UserCardRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public static void loadUsersIntoCards(TilePane targetContainer, UserCardRefreshListener listener) {
        List<User> userList = selectAllUsers();
        targetContainer.getChildren().clear();
        userList.forEach(user -> {
            TilePane card = loadUserCard(user, listener);
            if (card != null) {
                targetContainer.getChildren().add(card);
            }
        });
    }

    public static void loadFilteredUsersIntoCards(TilePane targetContainer, String searchTerm, UserCardRefreshListener listener) {
        List<User> userList = selectAllUsers().stream()
                .filter(user -> user.getEmail().contains(searchTerm))
                .collect(Collectors.toList());
        targetContainer.getChildren().clear();
        userList.forEach(user -> {
            TilePane card = loadUserCard(user, listener);
            if (card != null) {
                targetContainer.getChildren().add(card);
            }
        });
    }

    @FXML
    private void handleDeleteUser() {
        if (this.currentUser != null) {
            try {
                if (UserController.deleteUser(this.currentUser.getId())) {
                    System.out.println("User deleted successfully");
                    if (refreshListener != null) {
                        refreshListener.refreshUserList();
                    }
                } else {
                    System.out.println("Failed to delete user");
                }
            } catch (SQLException e) {
                System.err.println("Error deleting user: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditUser() {
        if (this.currentUser != null && this.refreshListener instanceof SampleController) {
            ((SampleController) this.refreshListener).showEditUser(currentUser);
        }
    }

    @FXML
    private void handleViewUser() {
        if (this.currentUser != null && this.refreshListener instanceof SampleController) {
            ((SampleController) this.refreshListener).showViewUser(currentUser);
        }
    }

}
