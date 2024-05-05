package Gestionreclamation.Controller;

import Gestionreclamation.Entity.User;
import Gestionreclamation.Services.UserService;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.geometry.Pos;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import java.io.FileOutputStream;

import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.controlsfx.control.Notifications;

import static Gestionreclamation.Services.UserService.selectUser;


public class SampleController implements UserCardRefreshListener {
    public Button closeDataViewButton, updateDataButton;
    public Label viewAddressLabel, viewPhoneLabel, viewLastNameLabel, viewFirstNameLabel, viewEmailLabel, viewCreatedAtLabel, viewUpdatedAtLabel, updateEmailError, updateFirstNameError, updateLastNameError, updatePhoneError, updateAdressError;
    public AnchorPane DataView = new AnchorPane(), anchor=new AnchorPane(), EditProfileField, EditField = new AnchorPane();
    public ImageView avatarImageView;
    public TextField updateEmailText, updateFirstNameText, updateLastNameText, updatePhoneText, updateAdressText;
    public Button updateProfileButton= new Button();
    public Button logoutButton=new Button();

    private User currentUser;
    private User currentUser1;
    public Label updateProfileEmailError, updateProfileFirstNameError, updateProfileLastNameError, updateProfilePhoneError, updateProfileAdressError;
    public TextField updateProfileEmailText, updateProfileFirstNameText, updateProfileLastNameText, updateProfilePhoneText, updateProfileAdressText;
    @FXML
    private TilePane HBoxList;
    @FXML
    private ScrollPane userListScrollPane = new ScrollPane();
    @FXML
    private TextField searchField = new TextField();
    private UserService userService = new UserService();
    @FXML
    private Button exportButton=new Button();
    @FXML
    private Button gestionActivite;

    @FXML
    private Button gestionSalle;



    private static SampleController instance;

    public SampleController() {
        instance = this;
    }

    public static SampleController getInstance()
    {
        return instance;
    }




    public void initialize() {
        EditField.setVisible(false);
        // loadUserListLayout();
        loadUserCreationStatistics();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
        updateProfileButton.setOnAction(event -> updateProfile());
        logoutButton.setOnAction(event -> logout());
        Circle clip = new Circle(avatarImageView.getFitWidth() / 2, avatarImageView.getFitHeight() / 2, Math.min(avatarImageView.getFitWidth(), avatarImageView.getFitHeight()) / 2);
        avatarImageView.setClip(clip);
        exportButton.setOnAction(event -> exportToPdf());


    }

    @FXML
    void openActivite(ActionEvent event) {
        try {
            // Load the FXML file
            Parent activiteView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/GestionSalle/Activite.fxml"));

            // Clear the existing content of the anchor pane and add the loaded FXML
            anchor.getChildren().setAll(activiteView);
        } catch (IOException e) {
            // Handle the exception (e.g., the file is not found)
            e.printStackTrace();
        }
    }


    @FXML
    void openSalle(ActionEvent event) {
        try {
            Parent salleView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/GestionSalle/Salle.fxml"));
            searchField.setVisible(false);
            userListScrollPane.setVisible(false);
            exportButton.setVisible(false);
            anchor.getChildren().setAll(salleView);
            anchor.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AnchorPane getAnchor() {
        return anchor;
    }
    public void refreshSalle() {
        try {
            Parent salleView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/GestionSalle/Salle.fxml"));
            searchField.setVisible(false);
            userListScrollPane.setVisible(false);
            exportButton.setVisible(false);
            anchor.getChildren().setAll(salleView);
            anchor.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void loadRec() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReclamationView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            searchField.setVisible(false);
            userListScrollPane.setVisible(false);
            exportButton.setVisible(false);
            anchor.getChildren().setAll(content);
            anchor.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadRep() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Gestionreclamation/ReponseView.fxml"));
            Pane content = fxmlLoader.load(); // Load as Pane
            searchField.setVisible(false);
            userListScrollPane.setVisible(false);
            exportButton.setVisible(false);
            anchor.getChildren().setAll(content);
            anchor.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void loadUserListLayout() {
        UserCardController.loadUsersIntoCards(HBoxList, this);
        userListScrollPane.setVisible(true);
        searchField.setVisible(true);
        anchor.setVisible(false);
        exportButton.setVisible(true);
    }

    @FXML
    void logout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/Gestionreclamation/Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

  private void exportToPdf() {
    System.out.println("Export to PDF method called");
    UserService userService = new UserService();
    List<User> users = userService.selectAllUsers();
    System.out.println("Users fetched: " + users.size());

    String homeDirectory = System.getProperty("user.home");
    String filePath = homeDirectory + "/Downloads/users.pdf";

    try {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Create a table with 7 columns (for ID, First Name, Last Name, Email, Phone, Address, CreatedDT)
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Define column widths
        float[] columnWidths = {1f, 2f, 2f, 3f, 2f, 3f, 2f};
        table.setWidths(columnWidths);

        // Add table headers
        table.addCell("ID");
        table.addCell("First Name");
        table.addCell("Last Name");
        table.addCell("Email");
        table.addCell("Phone");
        table.addCell("Address");
        table.addCell("CreatedDT");

        // Add table rows
        for (User user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getPrenom());
            table.addCell(user.getNom());
            table.addCell(user.getEmail());
            table.addCell(String.valueOf(user.getNumTele()));
            table.addCell(user.getAdresse());
            table.addCell(user.getCreatedAt().toString());
        }

        // Add table to document
        document.add(table);

        document.close();
        System.out.println("PDF Created!");

        Notifications.create()
                .title("PDF Export")
                .text("PDF file has been created and downloaded successfully!")
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(e -> System.out.println("Notification clicked!"))
                .showInformation();
    } catch (Exception e) {
        e.printStackTrace();
    }
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
            UserCardController.loadFilteredUsersIntoCards(HBoxList, searchTerm, this);
        }
    }


    private Map<String, Boolean> validateInputs(String email, String phone, String firstName, String lastName, String address) {
        Map<String, Boolean> validationResults = new HashMap<>();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String phoneRegex = "\\d{8}";

        validationResults.put("isEmailValid", email.matches(emailRegex));
        validationResults.put("isPhoneValid", phone.matches(phoneRegex));
        validationResults.put("isFirstNameValid", !firstName.trim().isEmpty());
        validationResults.put("isLastNameValid", !lastName.trim().isEmpty());
        validationResults.put("isAddressValid", !address.trim().isEmpty());

        return validationResults;
    }

    public void showEditUser(User user) {
        this.currentUser = user;
        userListScrollPane.setVisible(false);
        searchField.setVisible(false);
        exportButton.setVisible(false);
        EditField.setVisible(true);
        DataView.setVisible(false);
        updateEmailText.setText(user.getEmail());
        updateFirstNameText.setText(user.getPrenom());
        updateLastNameText.setText(user.getNom());
        updatePhoneText.setText(String.valueOf(user.getNumTele()));
        updateAdressText.setText(user.getAdresse());
    }


    @FXML
    private void handleUpdateAction() {
        String email = updateEmailText.getText();
        String firstName = updateFirstNameText.getText();
        String lastName = updateLastNameText.getText();
        String phone = updatePhoneText.getText();
        String address = updateAdressText.getText();

        Map<String, Boolean> validationResults = validateInputs(email, phone, firstName, lastName, address);

        if (!validationResults.get("isEmailValid")) {
            updateEmailError.setText("Invalid email format.");
            return;
        }
        if (!validationResults.get("isPhoneValid")) {
            updatePhoneError.setText("Phone must be 8 digits.");
            return;
        }
        if (!validationResults.get("isFirstNameValid")) {
            updateFirstNameError.setText("First name is required.");
            return;
        }
        if (!validationResults.get("isLastNameValid")) {
            updateLastNameError.setText("Last name is required.");
            return;
        }
        if (!validationResults.get("isAddressValid")) {
            updateAdressError.setText("Address is required.");
            return;
        }

        if (currentUser == null) {
            System.out.println("Current user is null. Cannot update user.");
            return;
        }

        String userId = currentUser.getId();
        User currentUser = selectUser(userId);
        boolean updatePassword = false;
        String[] roles = currentUser.getRoles();
        User userToUpdate = new User(userId, firstName, lastName, email, roles, Integer.parseInt(phone), currentUser.getPassword(), address,
                currentUser.getAvatar(), currentUser.getCreatedAt(), LocalDateTime.now(), currentUser.isVerified());

        try {
            if (userService.updateUser(userToUpdate, updatePassword)) {
                System.out.println("User updated successfully.");
                clearErrors();
                EditField.setVisible(false);
                loadUserListLayout();
            } else {
                System.out.println("Failed to update user.");
            }
        } catch (SQLException ex) {
            if (ex instanceof java.sql.SQLIntegrityConstraintViolationException && ex.getMessage().contains("Duplicate entry")) {
                updateEmailError.setText("Email already in use. Please use a different email.");
            } else {
                System.out.println("Error updating user: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    private void clearErrors() {
        updateEmailError.setText("");
        updatePhoneError.setText("");
        updateFirstNameError.setText("");
        updateLastNameError.setText("");
        updateAdressError.setText("");
    }
    private void clearErrors1() {
        updateProfileEmailError.setText("");
        updateProfilePhoneError.setText("");
        updateProfileFirstNameError.setText("");
        updateProfileLastNameError.setText("");
        updateProfileAdressError.setText("");
    }

    public void showViewUser(User user) {

        this.currentUser = user;

        userListScrollPane.setVisible(false);
        searchField.setVisible(false);
        EditField.setVisible(false);
        exportButton.setVisible(false);

        // Set user details in view labels
        viewFirstNameLabel.setText(user.getPrenom());
        viewLastNameLabel.setText(user.getNom());
        viewEmailLabel.setText(user.getEmail());
        viewPhoneLabel.setText(String.valueOf(user.getNumTele()));
        viewAddressLabel.setText(user.getAdresse());
        viewCreatedAtLabel.setText(user.getCreatedAt().toString());
        viewUpdatedAtLabel.setText(user.getUpdatedAt().toString());
        DataView.setVisible(true);
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            try {
                // Get the URL of the avatar image
                URL avatarUrlResource = getClass().getResource(avatarUrl);
                if (avatarUrlResource != null) {
                    avatarImageView.setImage(new Image(avatarUrlResource.toExternalForm()));
                } else {
                    // If the URL is invalid, set a default image
                    avatarImageView.setImage(new Image(getClass().getResource("/Gestionreclamation//avatars/default.png").toExternalForm()));
                }
            } catch (IllegalArgumentException e) {
                // If the URL is invalid, set a default image
                avatarImageView.setImage(new Image(getClass().getResource("/Gestionreclamation//avatars/default.png").toExternalForm()));
            }
        } else {
            // If the URL is null, set a default image
            avatarImageView.setImage(new Image(getClass().getResource("/Gestionreclamation//avatars/default.png").toExternalForm()));
        }
    }

    @FXML
    private void handleCloseDataViewAction() {
        viewFirstNameLabel.setText("");
        viewLastNameLabel.setText("");
        viewEmailLabel.setText("");
        viewPhoneLabel.setText("");
        viewAddressLabel.setText("");

        DataView.setVisible(false);
        userListScrollPane.setVisible(true);
        searchField.setVisible(true);
        exportButton.setVisible(true);
    }

    @FXML
    private void handleEditFromView() {
        if (currentUser != null) {
            showEditUser(currentUser);
        }
    }

    public void showEditUser1(User user) {
        // Set the user details in the updateProfile fields
        updateProfileEmailText.setText(user.getEmail());
        updateProfileFirstNameText.setText(user.getPrenom());
        updateProfileLastNameText.setText(user.getNom());
        updateProfilePhoneText.setText(String.valueOf(user.getNumTele()));
        updateProfileAdressText.setText(user.getAdresse());
        // avatarImageView.setImage(new Image(getClass().getResource(user.getAvatar()).toExternalForm()));
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            try {
                // Get the URL of the avatar image
                URL avatarUrlResource = getClass().getResource(avatarUrl);
                if (avatarUrlResource != null) {
                    avatarImageView.setImage(new Image(avatarUrlResource.toExternalForm()));
                } else {
                    // If the URL is invalid, set a default image
                    avatarImageView.setImage(new Image(getClass().getResource("/Gestionreclamation/avatars/default.png").toExternalForm()));
                }
            } catch (IllegalArgumentException e) {
                // If the URL is invalid, set a default image
                avatarImageView.setImage(new Image(getClass().getResource("/Gestionreclamation/avatars/default.png").toExternalForm()));
            }
        } else {
            // If the URL is null, set a default image
            avatarImageView.setImage(new Image(getClass().getResource("/Gestionreclamation/avatars/default.png").toExternalForm()));
        }

    }

    public void handleProfileEdit() {
        try {
            this.currentUser1 = SessionManager.getInstance().getCurrentUser();
            // Load the updateProfile.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/UserCrud/updateProfile.fxml"));
            Parent updateProfileRoot = loader.load();

            // Get the controller of updateProfile.fxml
            SampleController sampleController = loader.getController();
            sampleController.showEditUser1(currentUser1);

            // Enable the text fields and change the button text
            sampleController.updateProfileEmailText.setEditable(false);
            sampleController.updateProfileFirstNameText.setEditable(false);
            sampleController.updateProfileLastNameText.setEditable(false);
            sampleController.updateProfilePhoneText.setEditable(false);
            sampleController.updateProfileAdressText.setEditable(false);


            // Set the updateProfile view in the anchor pane
            anchor.getChildren().setAll(updateProfileRoot);
            anchor.setVisible(true);
            userListScrollPane.setVisible(false);
            searchField.setVisible(false);
            exportButton.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void updateProfile() {
    if (updateProfileButton.getText().equals("Enregistrer Data")) {
        // Call the method to update the data in the database
        if (handleUpdateAction1()) {
            // If the update is successful, disable the text fields and change the button text
            String disabledStyle = "-fx-background-color: #938f8f; -fx-text-fill: #000000;-fx-border-color:#070707;";
            updateProfileEmailText.setEditable(false);
            updateProfileEmailText.setStyle(disabledStyle);
            updateProfileFirstNameText.setEditable(false);
            updateProfileFirstNameText.setStyle(disabledStyle);
            updateProfileLastNameText.setEditable(false);
            updateProfileLastNameText.setStyle(disabledStyle);
            updateProfilePhoneText.setEditable(false);
            updateProfilePhoneText.setStyle(disabledStyle);
            updateProfileAdressText.setEditable(false);
            updateProfileAdressText.setStyle(disabledStyle);
            updateProfileButton.setText("Update Data");
            updateProfileButton.setStyle("-fx-background-color: #318fdc; -fx-text-fill: white;");
        }
    } else {
        // Enable the text fields
        String enabledStyle = "-fx-background-color: #ffffff; -fx-text-fill: #090909;-fx-border-color:#070707;";
        updateProfileEmailText.setEditable(true);
        updateProfileEmailText.setStyle(enabledStyle);
        updateProfileFirstNameText.setEditable(true);
        updateProfileFirstNameText.setStyle(enabledStyle);
        updateProfileLastNameText.setEditable(true);
        updateProfileLastNameText.setStyle(enabledStyle);
        updateProfilePhoneText.setEditable(true);
        updateProfilePhoneText.setStyle(enabledStyle);
        updateProfileAdressText.setEditable(true);
        updateProfileAdressText.setStyle(enabledStyle);

        // Change the button text
        updateProfileButton.setText("Enregistrer Data");
        updateProfileButton.setStyle("-fx-background-color: #318fdc; -fx-text-fill: white;");
    }
}

    private boolean handleUpdateAction1() {
        String email = updateProfileEmailText.getText();
        String firstName = updateProfileFirstNameText.getText();
        String lastName = updateProfileLastNameText.getText();
        String phone = updateProfilePhoneText.getText();
        String address = updateProfileAdressText.getText();

        Map<String, Boolean> validationResults = validateInputs(email, phone, firstName, lastName, address);

        if (!validationResults.get("isEmailValid")) {
            updateProfileEmailError.setText("Invalid email format.");
            return false;
        }
        if (!validationResults.get("isPhoneValid")) {
            updateProfilePhoneError.setText("Phone must be 8 digits.");
            return false;
        }
        if (!validationResults.get("isFirstNameValid")) {
            updateProfileFirstNameError.setText("First name is required.");
            return false;
        }
        if (!validationResults.get("isLastNameValid")) {
            updateProfileLastNameError.setText("Last name is required.");
            return false;
        }
        if (!validationResults.get("isAddressValid")) {
            updateProfileAdressError.setText("Address is required.");
            return false;
        }
        this.currentUser1 = SessionManager.getInstance().getCurrentUser();
        if (this.currentUser1 == null) {
            System.out.println("Current user is null. Cannot update user.");
            return false;
        }



        String userId = currentUser1.getId();
        boolean updatePassword = false;
        String[] roles = currentUser1.getRoles();
        User userToUpdate = new User(userId, firstName, lastName, email, roles, Integer.parseInt(phone), currentUser1.getPassword(), address,
                currentUser1.getAvatar(), currentUser1.getCreatedAt(), LocalDateTime.now(), currentUser1.isVerified());

        try {
            if (userService.updateUser(userToUpdate, updatePassword)) {
                System.out.println("User updated successfully.");
                clearErrors1();
                return true;
            } else {
                System.out.println("Failed to update user.");
                return false;
            }
        } catch (SQLException ex) {
            updateProfileEmailError.setText("Email already in use. Please use a different email.");
        }
        return false;
    }

   public void loadUserCreationStatistics() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestionreclamation/UserCrud/UserCreationStatistics.fxml"));
        AnchorPane userCreationStatisticsView = loader.load();

        // Get the controller and set the data
        UserCreationStatisticsController controller = loader.getController();
        List<User> users = userService.selectAllUsers(); // Replace with your method to get all users
        controller.setUserCreationData(users);

        anchor.getChildren().setAll(userCreationStatisticsView);
        anchor.setVisible(true);
        searchField.setVisible(false);
        userListScrollPane.setVisible(false);
        exportButton.setVisible(false);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}

