package SportHub.Services;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import connectionSql.ConnectionSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/*import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;*/

import java.io.IOException;
import java.nio.file.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
            preparedStatement.setBytes(3, cours.getPdfFileData());
            preparedStatement.setBytes(4, cours.getCoverImageData());
            preparedStatement.setInt(5, cours.getCategoryId());
            preparedStatement.executeUpdate();
        }
    }


    public void updateCours(Cours cours) throws SQLException {
        String query = "UPDATE cours SET name = ?, description = ?, pdf = ?, cover = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cours.getName());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setBytes(3, cours.getPdfFileData());
            preparedStatement.setBytes(4, cours.getCoverImageData());
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
                byte[] pdfFileData = resultSet.getBytes("pdf");
                byte[] coverImageData = resultSet.getBytes("cover");
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
                byte[] pdfFileData = resultSet.getBytes("pdf");
                byte[] coverImageData = resultSet.getBytes("cover");
                courses.add(new Cours(id, name, description, pdfFileData, coverImageData, null, categoryId));
            }
        }

        return courses;
    }


    public Map<String, Integer> getTop5Categories() throws SQLException {
        String query = "SELECT c.type, COUNT(*) as course_count FROM cours co JOIN category c ON co.category_id = c.id GROUP BY c.type ORDER BY course_count DESC LIMIT 5";
        Map<String, Integer> topCategories = new LinkedHashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String categoryName = resultSet.getString("type");
                int courseCount = resultSet.getInt("course_count");
                topCategories.put(categoryName, courseCount);
            }
        }

        return topCategories;
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



    /*public void sendPdfToEmail(Cours course, String email) {
    Email from = new Email("your-email@example.com");
    String subject = "PDF File of " + course.getName();
    Email to = new Email(email);
    Content content = new Content("text/plain", "Here is the PDF file of the course " + course.getName());
    Mail mail = new Mail(from, subject, to, content);

    // Assuming that the PDF data is stored in a byte array in the course object
    byte[] pdfData = course.getPdfFileData();
    Attachments attachments = new Attachments();
    Base64.Encoder encoder = Base64.getEncoder();
    String pdfDataEncoded = encoder.encodeToString(pdfData);
    attachments.setContent(pdfDataEncoded);
    attachments.setType("application/pdf");
    attachments.setFilename(course.getName() + ".pdf");
    attachments.setDisposition("attachment");
    mail.addAttachments(attachments);

    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    Request request = new Request();
    try {
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }*/
}
