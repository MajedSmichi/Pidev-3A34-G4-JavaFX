package SportHub.Entity;

import javafx.beans.property.*;

public class Cours {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty pdfFileData;
    private final StringProperty coverImageData;
    private final ObjectProperty<Category> category;

    public Cours(String name, String description, String pdfFileData, String coverImageData, Category selectedCategory) {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.pdfFileData = new SimpleStringProperty();
        this.coverImageData = new SimpleStringProperty();
        this.category = new SimpleObjectProperty<>();
    }

    public Cours(int id, String name, String description, String pdfFileData, String coverImageData, Category category) {
        this(name, description, pdfFileData, coverImageData, category);
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.pdfFileData.set(pdfFileData);
        this.coverImageData.set(coverImageData);
        this.category.set(category);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty pdfFileDataProperty() {
        return pdfFileData;
    }

    public String getPdfFileData() {
        return pdfFileData.get();
    }

    public void setPdfFileData(String pdfFileData) {
        this.pdfFileData.set(pdfFileData);
    }

    public StringProperty coverImageDataProperty() {
        return coverImageData;
    }

    public String getCoverImageData() {
        return coverImageData.get();
    }

    public void setCoverImageData(String coverImageData) {
        this.coverImageData.set(coverImageData);
    }

    public ObjectProperty<Category> categoryProperty() {
        return category;
    }

    public Category getCategory() {
        return category.get();
    }

    public void setCategory(Category category) {
        this.category.set(category);
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pdfFileData='" + pdfFileData.get() + '\'' +
                ", coverImageData='" + coverImageData.get() + '\'' +
                ", category=" + category +
                '}';
    }
}
