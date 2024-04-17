package GestionSalle.Services;

import connectionSql.ConnectionSql;
import java.sql.Connection;
import GestionSalle.Entity.Reponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ReponseServise {
    ConnectionSql connectionSql = new ConnectionSql();
    Connection cnx = connectionSql.getConnection();
    public List<Reponse> afficherReponse() {

        List<Reponse> reponse =new ArrayList<>();
        String req ="SELECT * FROM reponse";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()){
                Reponse rep = new Reponse( rs.getTimestamp(4).toLocalDateTime(), rs.getString(5));
                reponse.add(rep);
                System.out.println(rep);
            }
        } catch (SQLException ex) {
        }
        return reponse;
    }
    public void ajouterReponse(Reponse r) {
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
