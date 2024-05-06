package Gestionreclamation.Produit.Services;

import Gestionreclamation.Produit.Entity.Categorie_p;
import connectionSql.ConnectionSql;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;


public class Servicecategorie {
    Connection conn;
    public Servicecategorie() {
           conn =ConnectionSql.getConnection();
    }

    public void ajouter(Categorie_p categorie) throws SQLException{
        String req= "INSERT INTO `categorie_p`(`name`) VALUES (?)";
           PreparedStatement pst = conn.prepareStatement(req);
           pst.setString(1,categorie.getName());
           pst.executeUpdate();
           System.out.println("categorie ajoutée");

    }


    public void modifier(Categorie_p categorie) throws SQLException{
        String req= "UPDATE `categorie_p` SET `name`=? WHERE `id`=?";

            PreparedStatement pst = conn.prepareStatement(req);
            pst.setString(1,categorie.getName());
            pst.setInt(2,categorie.getId());
            pst.executeUpdate();
            System.out.println("categorie modifiée");


    }

    public void supprimer(int id) throws SQLException{

        String req= "DELETE FROM `categorie_p` WHERE `id`=?";
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("categorie supprimée ");

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


    public Categorie_p getOneById(int id){
        Categorie_p categorie = null;
        String req ="select * from categorie_p WHERE id=?";
        try {
            PreparedStatement pst= conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                categorie = new Categorie_p(id,name);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorie;
    }
}
