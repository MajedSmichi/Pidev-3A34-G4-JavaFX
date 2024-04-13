package Controller;

import Entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static Controller.UserController.selectAllUsers;

public class UserCardController {
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label userEmail;
    @FXML private Label userPhone;

    public void setUserData(User user) {
        userName.setText(user.getNom() + " " + user.getPrenom());
        userEmail.setText(user.getEmail());
        userPhone.setText(String.valueOf(user.getNumTele()));
        // Optionally set the user image if you have it
        // userImage.setImage(new Image(user.getImagePath()));
    }

    public static TilePane loadUserCard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(UserCardController.class.getResource("UserCrud/userList.fxml"));
            TilePane card = loader.load();  // Load the TilePane

            UserCardController cardController = loader.getController();
            cardController.setUserData(user);

            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void loadUsersIntoCards(TilePane targetContainer) {
        List<User> userList = selectAllUsers();
        targetContainer.getChildren().clear();
        for (User user : userList) {
            TilePane card = loadUserCard(user);
            if (card != null) {
                targetContainer.getChildren().add(card);
            } else {
                System.out.println("Failed to load card for user: " + user.getNom());
            }
        }
    }

    public static void loadFilteredUsersIntoCards(TilePane targetContainer, String searchTerm) {
        List<User> userList = selectAllUsers().stream()
                .filter(user -> user.getEmail().contains(searchTerm))
                .collect(Collectors.toList());
        targetContainer.getChildren().clear();
        for (User user : userList) {
            TilePane card = loadUserCard(user);
            if (card != null) {
                targetContainer.getChildren().add(card);
            }
        }
    }
}
