package SportHub.Controller;

import SportHub.Entity.Category;
import SportHub.Entity.Cours;
import SportHub.Services.CategoryService;
import SportHub.Services.CoursService;

import java.sql.SQLException;
import java.util.List;

public class CoursController {
    private final CoursService coursService;

    public CoursController() {
        this.coursService = new CoursService();
    }

    public void addCours(String name, String description, String pdfFile, String cover, Category category) {
        try {
            Cours cours = new Cours(name, description, pdfFile, cover, category);
            coursService.addCours(cours);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCours(int id, String name, String description, String pdfFile, String cover, Category category) {
        try {
            Cours cours = new Cours(id, name, description, pdfFile, cover, category);
            coursService.updateCours(cours);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCours(int courseId) {
        try {
            coursService.deleteCours(courseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cours> getAllCours() {
        try {
            return coursService.getAllCours();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
