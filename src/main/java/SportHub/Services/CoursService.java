package SportHub.Services;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import connectionSql.ConnectionSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoursService {
    private Connection connection;

    public CoursService() {
        connection = ConnectionSql.getInstance().getConnection();
    }

    public void addCours(Cours cours) throws SQLException {
        String query = "INSERT INTO cours (name, description, pdf, cover, category_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cours.getName());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setString(3, cours.getPdfFileData());
            preparedStatement.setString(4, cours.getCoverImageData());
            preparedStatement.setInt(5, cours.getCategoryId());
            preparedStatement.executeUpdate();
        }
    }

    public void updateCours(Cours cours) throws SQLException {
        String query = "UPDATE cours SET name = ?, description = ?, pdf = ?, cover = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cours.getName());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setString(3, cours.getPdfFileData());
            preparedStatement.setString(4, cours.getCoverImageData());
            preparedStatement.setInt(5, cours.getCategoryId());
            preparedStatement.setInt(6, cours.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteCours(int courseId) throws SQLException {
        String query = "DELETE FROM cours WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.executeUpdate();
        }
    }

    public ObservableList<Cours> getAllCours() throws SQLException {
        ObservableList<Cours> coursList = FXCollections.observableArrayList();
        String query = "SELECT id, name, description, pdf, cover, category_id FROM cours";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String pdfFileData = resultSet.getString("pdf");
                String coverImageData = resultSet.getString("cover");
                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId);
                coursList.add(new Cours(id, name, description, pdfFileData, coverImageData, category, categoryId));
            }
        }
        return coursList;
    }

    public boolean courseExists(String coursName) throws SQLException {
        String query = "SELECT COUNT(*) FROM cours WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, coursName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    public boolean courseNameExistsInCategory(String coursName, int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM cours WHERE name = ? AND category_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, coursName);
            preparedStatement.setInt(2, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    private Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT id, type, description FROM category WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                return new Category(id, type, description);
            } else {
                throw new SQLException("Category with ID " + categoryId + " not found.");
            }
        }
    }

    public ObservableList<Cours> getCoursesByCategory(int categoryId) throws SQLException {
        String query = "SELECT id, name, description, pdf, cover, category_id FROM cours WHERE category_id = ?";
        ObservableList<Cours> courses = FXCollections.observableArrayList();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String pdfFileData = resultSet.getString("pdf");
                String coverImageData = resultSet.getString("cover");
                courses.add(new Cours(id, name, description, pdfFileData, coverImageData, null, categoryId));
            }
        }

        return courses;
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, type, description FROM category";
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
