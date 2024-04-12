package Gestionreclamation.Services;

import Gestionreclamation.Entity.Reclamation;
import Gestionreclamation.Entity.User;
import connectionSql.ConnectionSql;
import java.sql.Connection;
import Gestionreclamation.Entity.Reponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ReponseService {
    ConnectionSql connectionSql = new ConnectionSql();
    Connection cnx = connectionSql.getConnection();
    public List<Reponse> afficherReponse() {
        List<Reponse> reponseList = new ArrayList<>();
        String req = "SELECT * FROM reponse INNER JOIN reclamation ON reponse.id_reclamation_id = reclamation.id INNER JOIN user ON reponse.utilisateur_id = user.id";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()){
                int id = rs.getInt("reponse.id");
                LocalDateTime date = rs.getTimestamp("reponse.date").toLocalDateTime();
                String reponse = rs.getString("reponse.reponse");


                Reclamation idReclamation = new Reclamation();
                idReclamation.setId(rs.getInt("reclamation.id"));
                idReclamation.setNom(rs.getString("reclamation.nom")); // Fetch the nom field
                idReclamation.setPrenom(rs.getString("reclamation.prenom"));
                idReclamation.setEmail(rs.getString("reclamation.email"));
                idReclamation.setNumTele(rs.getInt("reclamation.num_tele"));
                idReclamation.setEtat(rs.getString("reclamation.etat"));
                idReclamation.setSujet(rs.getString("reclamation.sujet"));
                idReclamation.setDescription(rs.getString("reclamation.description"));
                idReclamation.setDate(rs.getTimestamp("reclamation.date").toLocalDateTime());


                User utilisateur = new User();
                utilisateur.setId(rs.getInt("user.id"));
                // Set other fields of User...

                Reponse rep = new Reponse(id, date, reponse, idReclamation, utilisateur);
                reponseList.add(rep);
                System.out.println(rep);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reponseList;
    }    public void ajouterReponse(Reponse r) {
        String req = "INSERT INTO `reponse`(`utilisateur_id`, `id_reclamation_id`, `date`, `reponse`) VALUES ('"+r.getUtilisateur().getId()+"','"+r.getidReclamation().getId()+"','"+r.getDate()+"','"+r.getReponse()+"')";

        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Réponse à la réclamation ajoutée");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void supprimerReponse( Reponse r  ) {
        String req="DELETE FROM `reponse` WHERE id ="+r.getId();

        try {
            //insert
            Statement st=cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("reclamation supprimer avec succes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void modifierReponse(Reponse r) {
        if (r.getId() != 0) {
            StringBuilder reqBuilder = new StringBuilder("UPDATE `reponse` SET ");
            if (r.getReponse() != null) {
                reqBuilder.append("reponse='" + r.getReponse() + "', ");
            }

            // Update the modification date
            r.setDate(LocalDateTime.now());

            // Append modification date to the SQL query
            reqBuilder.append("date='" + r.getDate() + "', ");

            // Remove the trailing comma and space
            reqBuilder.delete(reqBuilder.length() - 2, reqBuilder.length());

            // Add the WHERE condition
            reqBuilder.append(" WHERE id = ").append(r.getId());

            try {
                Statement st = cnx.createStatement();
                st.executeUpdate(reqBuilder.toString());
                System.out.println("Reponse modifiée avec succès");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
