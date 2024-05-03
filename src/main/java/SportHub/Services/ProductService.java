package SportHub.Services;

import SportHub.Entity.Categorie_p;
import SportHub.Entity.Product;
import connectionSql.ConnectionSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    public void modifier(int id , String name, String description, int quantite , int price , String image , Categorie_p category){
        String req= "UPDATE `product` SET`categorie_p_id`=?,`name`=?,`price`=?,`quantite`=?,`description`=?,`image`=? WHERE `id`=?";
        try {
            PreparedStatement pst = conn.prepareStatement(req);
            pst.setInt(1,category.getId());
            pst.setString(2,name);
            pst.setInt(3,price);
            pst.setInt(4,quantite);
            pst.setString(5,description);
            pst.setString(6,image);
            pst.setInt(7,id);
            pst.executeUpdate();
            System.out.println("produit modifiée");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public  void delete(int id) throws SQLException{

        String deleteProductQuery = "DELETE FROM product WHERE id = ?";
        PreparedStatement deleteEventStmt = conn.prepareStatement(deleteProductQuery);
        deleteEventStmt.setInt(1, id);
        deleteEventStmt.executeUpdate();

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
            product.setQuantite(resultSet.getInt("quantite"));
            product.setImage(resultSet.getString("image"));
            // Récupérer la catégorie du produit à partir de la table de catégorie
            int categoryId = resultSet.getInt("categorie_p_id");
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
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int quantite = rs.getInt("quantite");
        String description = rs.getString("description");
        String image = rs.getString("image");


        // Assurez-vous que tous les champs sont correctement initialisés
        if (categorie == null || name == null || description == null || image == null) {
            throw new SQLException("One or more fields are null");
        }

        return new Product(id, categorie, quantite, price, name, description, image);
    }
    public List<Product> search(String searchTerm) throws SQLException {
        String query = "SELECT * FROM product WHERE name LIKE ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, "%" + searchTerm + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice((int) resultSet.getDouble("price"));
            product.setQuantite(resultSet.getInt("quantite"));
            product.setImage(resultSet.getString("image"));
            // Récupérer la catégorie du produit à partir de la table de catégorie
            int categoryId = resultSet.getInt("categorie_p_id");
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

    public boolean productExist(String productName) throws SQLException {
        String query = "SELECT COUNT(*) FROM product WHERE name = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,productName );
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    public List<String> getCategories() throws SQLException {
        String query = "SELECT name FROM categorie_p";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> categories = new ArrayList<>();
        while (resultSet.next()) {
            categories.add(resultSet.getString("name"));
        }
        return categories;
    }

    public List<Product> getByCategory(String category) throws SQLException {
        String query = "SELECT * FROM product WHERE categorie_p_id = (SELECT id FROM categorie_p WHERE name = ?)";
        System.out.println("Executing query: " + query); // Add logging
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, category);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            System.out.println("ResultSet: " + resultSet.toString()); // Add logging
            Product product = getProduit(resultSet.getInt("id"), resultSet);
            products.add(product);
        }
        return products;
    }
    public List<Product> getByPrice(double price) throws SQLException {
        // Récupérer tous les produits
        List<Product> allProducts = getAll();

        // Filtrer les produits par prix
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getPrice() <= price)
                .collect(Collectors.toList());

        return filteredProducts;
    }
}
