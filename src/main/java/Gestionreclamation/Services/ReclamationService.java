package Gestionreclamation.Services;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.Reponse;
import Gestionreclamation.Entity.User;
import connectionSql.ConnectionSql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.PreparedStatement;
import java.util.Map;


public class ReclamationService {
    ConnectionSql connectionSql = new ConnectionSql();
    Connection cnx = connectionSql.getConnection();

    public List<Reclamation> afficherreclamation() {
        List<Reclamation> reclamations = new ArrayList<>();
        // Requête SQL pour récupérer les réclamations
        String req = "SELECT * FROM reclamation";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            // Parcourir le résultat de la requête et ajouter les réclamations à la liste
            while (rs.next()) {
                User user = new User();

                int id = rs.getInt(1);
                int userId = rs.getInt(2);
                String nom = rs.getString(3);
                String prenom = rs.getString(10);
                String email = rs.getString(4);
                int numTele = rs.getInt(5);
                String etat = rs.getString(6);
                String sujet = rs.getString(7);
                String description = rs.getString(8);
                // Vous pouvez utiliser java.sql.Timestamp pour récupérer la date
                java.sql.Timestamp timestamp = rs.getTimestamp(9);
                LocalDateTime date = timestamp.toLocalDateTime();
                user.setId(userId);

                Reclamation reclamation = new Reclamation();
                reclamation.setId(id);
                reclamation.setUtilisateur(user); // Set the user with only the ID
                reclamation.setNom(nom);
                reclamation.setPrenom(prenom);
                reclamation.setEmail(email);
                reclamation.setNumTele(numTele);
                reclamation.setEtat(etat);
                reclamation.setSujet(sujet);
                reclamation.setDescription(description);
                reclamation.setDate(date);
                reclamations.add(reclamation);
                System.out.println(reclamation);

            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Gérer l'exception selon vos besoins
        }
        return reclamations;
    }

    public void ajouterReclamation(Reclamation r) {
        String req = "INSERT INTO reclamation (utilisateur_id, nom, email, num_tele, etat, sujet, description, date, prenom) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(req);
            // Définir les valeurs des paramètres dans la requête
            preparedStatement.setInt(1, r.getUtilisateur().getId());
            preparedStatement.setString(2, r.getNom());
            preparedStatement.setString(3, r.getEmail());
            preparedStatement.setInt(4, r.getNumTele());
            preparedStatement.setString(5, r.getEtat());
            preparedStatement.setString(6, r.getSujet());
            preparedStatement.setString(7, r.getDescription());
            preparedStatement.setTimestamp(8, java.sql.Timestamp.valueOf(r.getDate()));
            preparedStatement.setString(9, r.getPrenom());

            // Exécuter la requête
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réclamation ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout de la réclamation.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Gérer l'exception selon vos besoins
        }
    }

    public void supprimerReclamation(Reclamation r) {
        try {
            // Supprimer d'abord les réponses associées à la réclamation
            supprimerReponsesDeReclamation(r);

            // Ensuite, supprimer la réclamation elle-même
            String req = "DELETE FROM reclamation WHERE id=?";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, r.getId());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réclamation supprimée avec succès !");
            } else {
                System.out.println("Échec de la suppression de la réclamation.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void supprimerReponsesDeReclamation(Reclamation r) {
        try {
            // Supprimer les réponses associées à la réclamation
            String req = "DELETE FROM reponse WHERE id_reclamation_id=?";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, r.getId());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réponses associées à la réclamation supprimées avec succès !");
            } else {
                System.out.println("Aucune réponse associée à la réclamation.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void modifierReclamation(Reclamation r) {
        if (r.getId() != 0) {
            StringBuilder reqBuilder = new StringBuilder("UPDATE `reclamation` SET ");

            if (r.getNom() != null) {
                reqBuilder.append("nom='" + r.getNom() + "', ");
            }
            if (r.getPrenom() != null) {
                reqBuilder.append("prenom='" + r.getPrenom() + "', ");
            }
            if (r.getEmail() != null) {
                reqBuilder.append("email='" + r.getEmail() + "', ");
            }
            if (r.getNumTele() != 0) {
                reqBuilder.append("num_tele='" + r.getNumTele() + "', ");
            }
            if (r.getSujet() != null) {
                reqBuilder.append("sujet='" + r.getSujet() + "', ");
            }
            if (r.getDescription() != null) {
                reqBuilder.append("description='" + r.getDescription() + "', ");
            }
            if (r.getEtat() != null) {
                reqBuilder.append("etat='" + r.getEtat() + "', ");
            }
            r.setDate(LocalDateTime.now());

            // Append modification date to the SQL query
            reqBuilder.append("date='" + r.getDate() + "', ");

            // Supprimer la virgule et l'espace finaux
            reqBuilder.delete(reqBuilder.length() - 2, reqBuilder.length());

            // Ajouter la condition WHERE
            reqBuilder.append(" WHERE id = ").append(r.getId());

            try {
                Statement st = cnx.createStatement();
                st.executeUpdate(reqBuilder.toString());
                System.out.println("Reclamation modifiée avec succès");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Map<Integer, Reclamation> reclamationMap = new HashMap<>();

    // Méthode pour récupérer une réclamation à partir de son ID
    public Reclamation getReclamationById(int reclamationId) {
        // Supposons que reclamationMap contient des données simulées de réclamations
        return reclamationMap.get(reclamationId);
    }
    public Reponse getReponseByReclamationId(int reclamationId) {
        Reponse response = null;
        String sql = "SELECT * FROM reponse WHERE id_reclamation_id = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, reclamationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                response = new Reponse();
                response.setId(resultSet.getInt("id"));
                response.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                response.setReponse(resultSet.getString("reponse"));
                // Set other fields of the response object as necessary
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
}
