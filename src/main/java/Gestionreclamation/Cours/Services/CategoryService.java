package Gestionreclamation.Cours.Services;

import Gestionreclamation.Cours.Entity.Category;
import connectionSql.ConnectionSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static Connection connection;

    public CategoryService() {
        connection = ConnectionSql.getConnection();
    }

    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO category (type, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getType());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    public void updateCategory(Category category) throws SQLException {
        String query = "UPDATE category SET type = ?, description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getType());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, category.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        }
    }

    public static List<Category> getAllCategories() throws SQLException {
        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT id, type, description FROM category";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                categoryList.add(new Category(id, type, description));
            }
        }
        return categoryList;
    }

    public boolean categoryExists(String categoryName) throws SQLException {
        String query = "SELECT COUNT(*) FROM category WHERE type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Returns true if category with the given name exists
                }
            }
        }
        return false; // Default to false if there's an issue or no result
    }
}
