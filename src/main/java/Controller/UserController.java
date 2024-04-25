package Controller;

import Entity.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


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


    private String userEmail;

    public void initialize() {
        updateUserInfo();
        firstname.setCellValueFactory(new PropertyValueFactory<>("nom"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumTele())));
        adress.setCellValueFactory(new PropertyValueFactory<>("adresse"));

    }

    private void updateUserInfo() {

        firstNameConnect.setText(SessionManager.getInstance().getUserFirstName());
        lastNameConnect.setText(SessionManager.getInstance().getUserLastName());
        emailConnect.setText(SessionManager.getInstance().getUserEmail());
    }





    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }




}
