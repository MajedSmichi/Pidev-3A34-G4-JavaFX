package SportHub.Services;

import SportHub.Entity.Categorie_p;
import SportHub.Services.Servicecategorie;
import SportHub.Entity.Product;
import connectionSql.ConnectionSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ProductService {
     Connection conn;
     Servicecategorie serviceCategorie = new Servicecategorie();

    public ProductService() {
         conn= ConnectionSql.getInstance().getConnection();

    }
    public void ajouter(Product produit) throws SQLException {
        String req= "INSERT INTO `product`(`categorie_p_id`, `name`, `price`, `quantite`, `description`, `image`) VALUES (?,?,?,?,?,?)";

            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1,produit.getCategory().getId());
            pst.setString(2,produit.getName());
            pst.setDouble(3,produit.getPrice());
            pst.setInt(4,produit.getQuantite());
            pst.setString(5,produit.getDescription());
            pst.setString(6,produit.getImage());
            pst.executeUpdate();
            System.out.println("produit ajoutée");

    }

    public void modifier(Product produit){
        String req= "UPDATE `produit` SET`categorie_p_id`=?,`name`=?,`price`=?,`quantite`=?,`description`=?,`image`=? WHERE `id`=?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1,produit.getCategory().getId());
            pst.setString(2,produit.getName());
            pst.setDouble(3,produit.getPrice());
            pst.setInt(4,produit.getQuantite());
            pst.setString(5,produit.getDescription());
            pst.setString(6,produit.getImage());
            pst.setInt(7,produit.getId());
            pst.executeUpdate();
            System.out.println("produit modifiée");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void supprimer(int id) {

        String req= "DELETE FROM `product` WHERE `id`=?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("produit supprimée ");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }


    public  List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice((int) resultSet.getDouble("price"));
            product.setQuantite(resultSet.getInt("quantity"));

            // Récupérer la catégorie du produit à partir de la table de catégorie
            int categoryId = resultSet.getInt("category_id");
            String categoryQuery = "SELECT name FROM categorie_p WHERE id = ?";
            PreparedStatement categoryStatement = conn.prepareStatement(categoryQuery);
            categoryStatement.setInt(1, categoryId);
            ResultSet categoryResult = categoryStatement.executeQuery();
            if (categoryResult.next()) {
                product.setCategory(categoryResult.getString("name"));
            }

            products.add(product);
        }
        return products;
    }
    public Product getOneById(int id){
        Product produit = null;
        String req ="select * from product WHERE id=?";
        try {
            PreparedStatement pst= conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                produit = getProduit(id, rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produit;
    }

    private Product getProduit(int id, ResultSet rs) throws SQLException {
        int categorie_p_id = rs.getInt("categorie_p_id");
        Categorie_p categorie = serviceCategorie.getOneById(categorie_p_id);
        String name = rs.getString("nom");
        double price = rs.getDouble("prix");
        int quantite = rs.getInt("quantite");
        String description = rs.getString("description");
        String image = rs.getString("image");
        return new Product(id,categorie,quantite, price,name,description,image);
    }
}
