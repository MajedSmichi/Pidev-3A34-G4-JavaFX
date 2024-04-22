package SportHub.Services;

import SportHub.Entity.Category;
import connectionSql.ConnectionSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private Connection connection;

    public CategoryService() {
        connection = ConnectionSql.getInstance().getConnection();
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
        String query = "UPDATE categories SET type = ?, description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getType());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, category.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, type, description FROM categories";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                categories.add(new Category(id, type, description));
            }
        }
        return categories;
    }
}
