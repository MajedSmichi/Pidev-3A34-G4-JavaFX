package Controller;



import Entity.User;
import Services.UserService;
import connectionSql.ConnectionSql;

import java.sql.Connection;
import java.util.List;

public class TestConnectionSql {
    public static void main(String[] args) {
        Connection conn = ConnectionSql.getConnection();
        User newUser = new User();
        newUser.setNom("test");
        newUser.setPrenom("test");
        newUser.setEmail("jean.dupont@example.com");
      //  newUser.setRole("Utilisateur");
        newUser.setNumTele(123456789);
        newUser.setPassword("motdepasse");
        newUser.setAdresse("123 Rue de Paris, 75000 Paris");

        // Création d'une instance de UserController
        UserController userController = new UserController();

        // Insertion de l'utilisateur
        try {
            UserService.insertUser(newUser);
            System.out.println("Insertion réussie.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<User> users = UserService.selectAllUsers();


        for (User user : users) {
            System.out.println(user);
        }

        User user = UserService.selectUser(String.valueOf(Integer.parseInt("1")));
        System.out.println("Utilisateur d'id 1 : " + user);


    }
}

