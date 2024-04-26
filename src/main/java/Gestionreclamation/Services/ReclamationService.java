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
import java.time.temporal.IsoFields;
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
            String req = "UPDATE `reclamation` SET nom=?, prenom=?, email=?, num_tele=?, sujet=?, description=?, etat=?, date=? WHERE id=?";
            try {
                PreparedStatement st = cnx.prepareStatement(req);
                st.setString(1, r.getNom());
                st.setString(2, r.getPrenom());
                st.setString(3, r.getEmail());
                st.setInt(4, r.getNumTele());
                st.setString(5, r.getSujet());
                st.setString(6, r.getDescription());
                st.setString(7, r.getEtat());
                st.setTimestamp(8, java.sql.Timestamp.valueOf(r.getDate()));
                st.setInt(9, r.getId());

                st.executeUpdate();
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

    public double getTreatedReclamationPercentage() throws SQLException {
        String queryTotal = "SELECT COUNT(*) FROM reclamation";
        String queryTreated = "SELECT COUNT(*) FROM reclamation WHERE etat = 'Traité'";
        Statement stTotal = cnx.createStatement();
        Statement stTreated = cnx.createStatement();
        ResultSet rsTotal = stTotal.executeQuery(queryTotal);
        ResultSet rsTreated = stTreated.executeQuery(queryTreated);
        if (rsTotal.next() && rsTreated.next()) {
            int total = rsTotal.getInt(1);
            int treated = rsTreated.getInt(1);
            return (double) treated / total * 100;
        }
        return 0;
    }

    public double getPendingReclamationPercentage() throws SQLException {
        String queryTotal = "SELECT COUNT(*) FROM reclamation";
        String queryPending = "SELECT COUNT(*) FROM reclamation WHERE etat = 'En attente'";
        Statement stTotal = cnx.createStatement();
        Statement stPending = cnx.createStatement();
        ResultSet rsTotal = stTotal.executeQuery(queryTotal);
        ResultSet rsPending = stPending.executeQuery(queryPending);
        if (rsTotal.next() && rsPending.next()) {
            int total = rsTotal.getInt(1);
            int pending = rsPending.getInt(1);
            return (double) pending / total * 100;
        }
        return 0;
    }

    public Map<Integer, Double> calculateWeeklyResponseRate() throws SQLException {
        Map<Integer, Double> weeklyResponseRate = new HashMap<>();

        // Get the current week of the year
        int currentWeek = LocalDateTime.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        for (int week = 1; week <= currentWeek; week++) {
            // Count the number of complaints and responses in the week
            String queryComplaints = "SELECT COUNT(*) FROM reclamation WHERE WEEK(date, 1) = ?";
            String queryResponses = "SELECT COUNT(*) FROM reponse WHERE WEEK(date, 1) = ?";

            PreparedStatement stComplaints = cnx.prepareStatement(queryComplaints);
            PreparedStatement stResponses = cnx.prepareStatement(queryResponses);

            stComplaints.setInt(1, week);
            stResponses.setInt(1, week);

            ResultSet rsComplaints = stComplaints.executeQuery();
            ResultSet rsResponses = stResponses.executeQuery();

            if (rsComplaints.next() && rsResponses.next()) {
                int complaints = rsComplaints.getInt(1);
                int responses = rsResponses.getInt(1);

                // Calculate the response rate
                double responseRate = complaints == 0 ? 0 : (double) responses / complaints;

                // Add the week and response rate to the map
                weeklyResponseRate.put(week, responseRate);
            }
        }

        return weeklyResponseRate;
    }}
