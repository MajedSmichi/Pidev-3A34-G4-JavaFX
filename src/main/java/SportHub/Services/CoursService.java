package SportHub.Services;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import connectionSql.ConnectionSql;

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
        String query = "INSERT INTO cours (name, description, pdfFile, cover, category_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cours.getName());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setBytes(3, toPrimitive(cours.getPdfFileData()));
            preparedStatement.setBytes(4, toPrimitive(cours.getCoverImageData()));
            preparedStatement.setInt(5, cours.getCategory().getId());
            preparedStatement.executeUpdate();
        }
    }

    public void updateCours(Cours cours) throws SQLException {
        String query = "UPDATE cours SET name = ?, description = ?, pdfFile = ?, cover = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cours.getName());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setBytes(3, toPrimitive(cours.getPdfFileData()));
            preparedStatement.setBytes(4, toPrimitive(cours.getCoverImageData()));
            preparedStatement.setInt(5, cours.getCategory().getId());
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

    public List<Cours> getAllCours() throws SQLException {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT id, name, description, pdfFile, cover, category_id FROM cours";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                byte[] pdfFileData = resultSet.getBytes("pdfFile");
                byte[] coverImageData = resultSet.getBytes("cover");
                int categoryId = resultSet.getInt("category_id");
                Category category = getCategoryById(categoryId);
                coursList.add(new Cours(name, description, pdfFileData, coverImageData, category));
            }
        }
        return coursList;
    }

    private Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT id, name, description FROM categories WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                return new Category(id, name, description);
            }
        }
        return null;
    }

    private byte[] toPrimitive(java.lang.Byte[] array) {
        if (array == null) {
            return null;
        }
        byte[] primitiveArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            primitiveArray[i] = array[i];
        }
        return primitiveArray;
    }
}
