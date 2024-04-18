package Controller;

import Entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connectionSql.ConnectionSql;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


public class UserController {
    @FXML
    private TableColumn<User, String> firstname=new TableColumn<>();
    @FXML
    private TableColumn<User, String> lastName=new TableColumn<>();
    @FXML
    private TableColumn<User, String> email=new TableColumn<>();
    @FXML
    private TableColumn<User, String> phone=new TableColumn<>();
    @FXML
    private TableColumn<User, String> adress=new TableColumn<>();

    @FXML
    private Label firstNameConnect;

    @FXML
    private Label lastNameConnect;

    @FXML
    private Label emailConnect;

    @FXML
    private Button logoutButton=new Button();
    private String userEmail;

    public void initialize() {
        updateUserInfo();
        firstname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumTele())));
        adress.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        logoutButton.setOnAction(event -> logout());
    }

    private void updateUserInfo() {

        firstNameConnect.setText(SessionManager.getInstance().getUserFirstName());
        lastNameConnect.setText(SessionManager.getInstance().getUserLastName());
        emailConnect.setText(SessionManager.getInstance().getUserEmail());
    }

    private void logout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("Login/login.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
   
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }




}
