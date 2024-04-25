package SportHub.Entity;

import javafx.beans.property.*;

public class Cours {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final ObjectProperty<byte[]> pdfFileData;
    private final ObjectProperty<byte[]> coverImageData;
    private final ObjectProperty<Category> category;

    public Cours(String name, String description, byte[] pdfFileData, byte[] coverImageData, Category selectedCategory) {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.pdfFileData = new SimpleObjectProperty<>();
        this.coverImageData = new SimpleObjectProperty<>();
        this.category = new SimpleObjectProperty<>();
    }

    public Cours(int id, String name, String description, byte[] pdfFileData, byte[] coverImageData, Category category) {
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

    public ObjectProperty<byte[]> pdfFileDataProperty() {
        return pdfFileData;
    }

    public byte[] getPdfFileData() {
        return pdfFileData.get();
    }

    public void setPdfFileData(byte[] pdfFileData) {
        this.pdfFileData.set(pdfFileData);
    }

    public ObjectProperty<byte[]> coverImageDataProperty() {
        return coverImageData;
    }

    public byte[] getCoverImageData() {
        return coverImageData.get();
    }

    public void setCoverImageData(byte[] coverImageData) {
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
                ", pdfFileData=" + (pdfFileData.get() != null ? pdfFileData.get().length + " bytes" : "null") +
                ", coverImageData=" + (coverImageData.get() != null ? coverImageData.get().length + " bytes" : "null") +
                ", category=" + category +
                '}';
    }
}
