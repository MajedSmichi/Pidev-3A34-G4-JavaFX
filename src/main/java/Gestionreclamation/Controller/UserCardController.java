package Gestionreclamation.Controller;

import javafx.application.Platform;
import javafx.util.Duration;
import javafx.geometry.Pos;
import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import org.controlsfx.control.Notifications;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import static Gestionreclamation.Services.UserService.selectAllUsers;

public class UserCardController {
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label userEmail;
    @FXML private Label userPhone;
    @FXML private Button deleteButton;
    @FXML
    private Button activateButton;
    private User currentUser;
    private UserCardRefreshListener refreshListener;

    public void initialize() {

        Circle clip = new Circle(userImage.getFitWidth() / 2, userImage.getFitHeight() / 2, Math.min(userImage.getFitWidth(), userImage.getFitHeight()) / 2);
        userImage.setClip(clip);

    }

    public void setUserData(User user) {
        this.currentUser = user;
        userName.setText(user.getNom() + " " + user.getPrenom());
        userEmail.setText(user.getEmail());
        userPhone.setText(String.valueOf(user.getNumTele()));
        activateButton.setText(user.isVerified() ? "Desactivate" : "Activate");

        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            try {
                // Get the URL of the avatar image
                URL avatarUrlResource = getClass().getResource(avatarUrl);
                if (avatarUrlResource != null) {
                    userImage.setImage(new Image(avatarUrlResource.toExternalForm()));
                } else {

                    userImage.setImage(new Image(getClass().getResource("/Gestionreclamation/avatars/default.png").toExternalForm()));
                }
            } catch (IllegalArgumentException e) {

                userImage.setImage(new Image(getClass().getResource("/Gestionreclamation/avatars/default.png").toExternalForm()));
            }
        } else {

            userImage.setImage(new Image(getClass().getResource("/Gestionreclamation/avatars/default.png").toExternalForm()));
        }
    }

    public static TilePane loadUserCard(User user, UserCardRefreshListener listener) {
        try {
            FXMLLoader loader = new FXMLLoader(UserCardController.class.getResource("/Gestionreclamation/UserCrud/userList.fxml"));
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
                if (UserService.deleteUser(this.currentUser.getId())) {
                    System.out.println("User deleted successfully");
                    Notifications.create()
                            .title("User Deletion")
                            .text("User has been deleted successfully!")
                            .hideAfter(Duration.seconds(5))
                            .position(Pos.BOTTOM_RIGHT)
                            .showInformation();
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
   @FXML
private void handleActivateUser() {
    if (this.currentUser != null) {
        boolean newStatus = !this.currentUser.isVerified();
        if (UserService.updateUserActiveStatus(this.currentUser.getId(), newStatus)) {
            System.out.println("User active status updated successfully");
            this.currentUser.setVerified(newStatus);
            activateButton.setText(newStatus ? "Deactivate" : "Activate");
            Platform.runLater(() -> {
                Notifications.create()
                        .title("User Activation")
                        .text("User has been " + (newStatus ? "activated" : "deactivated") + " successfully!")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            });

            // Send an email to the user
            String host = "smtp.gmail.com"; // Gmail SMTP server
            String from = "smichimajed@gmail.com"; // replace with your email
            String password = "oxaxivwyxzrnzelz"; // replace with your email password

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            try {
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.currentUser.getEmail()));
    message.setSubject("Account Status Change");

    // HTML message
    String htmlMessage = "<h1 style='color:blue;'>Account Status Change</h1>" +
                         "<p>Your account has been <strong>" + (newStatus ? "activated" : "deactivated") + "</strong> from our system.</p>";

    message.setContent(htmlMessage, "text/html");

    Transport.send(message);
    System.out.println("Email sent successfully");
} catch (MessagingException e) {
    e.printStackTrace();
}
        } else {
            System.out.println("Failed to update user active status");
        }
    }
}

}
