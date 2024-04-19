package SportHub.Services;

import SportHub.Entity.Categorie_p;
import SportHub.Entity.Product;
import connectionSql.ConnectionSql;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Servicecategorie {
    Connection conn;
    public Servicecategorie() {
           conn =ConnectionSql.getInstance().getConnection();
    }

    public void ajouter(Categorie_p categorie) {
        String req= "INSERT INTO `categorie`(`nom`,`description`) VALUES (?,?)";
    try {
           PreparedStatement pst = conn.prepareStatement(req);
           pst.setString(1,categorie.getName());
           pst.executeUpdate();
           System.out.println("categorie ajoutée");
    }catch (SQLException e){
        System.out.println(e.getMessage());
    }

    }


    public void modifier(Categorie_p categorie){
        String req= "UPDATE `categorie` SET `nom`=?,`description`=? WHERE `id`=?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setString(1,categorie.getName());
            pst.setInt(3,categorie.getId());
            pst.executeUpdate();
            System.out.println("categorie modifiée");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }

    public void supprimer(int id) {

        String req= "DELETE FROM `categorie` WHERE `id`=?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("categorie supprimée ");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public Set<Categorie_p> getAllC() {
        Set<Categorie_p> categories=new HashSet<>();
        String req ="select * from categorie_p";
        try {
            Statement st= conn.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Categorie_p categorie = new Categorie_p(id,name);
                categories.add(categorie);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
    public List<Product> getAll() throws SQLException {
        return null;
    }

    public Categorie_p getOneById(int id){
        Categorie_p categorie = null;
        String req ="select * from categorie WHERE id=?";
        try {
            PreparedStatement pst= conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                categorie = new Categorie_p(id,nom);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorie;
    }
}
