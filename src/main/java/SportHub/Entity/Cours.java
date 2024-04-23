package SportHub.Entity;
import java.util.Arrays;


public class Cours {
    private int id;
    private String name;
    private String description;
    private Byte[] pdfFileData;
    private Byte[] coverImageData;
    private Category category;

    public Cours(String name, String description, byte[] pdfFileData, byte[] coverImageData, Category category) {
    }

    public Cours(String name, String description, Byte[] pdfFileData, Byte[] coverImageData, Category category) {
        this.name = name;
        this.description = description;
        this.pdfFileData = pdfFileData;
        this.coverImageData = coverImageData;
        this.category = category;
    }

    public Cours(int id, String name, String description, Byte[] pdfFileData, Byte[] coverImageData, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pdfFileData = pdfFileData;
        this.coverImageData = coverImageData;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte[] getPdfFileData() {
        return pdfFileData;
    }

    public void setPdfFileData(Byte[] pdfFileData) {
        this.pdfFileData = pdfFileData;
    }

    public Byte[] getCoverImageData() {
        return coverImageData;
    }

    public void setCoverImageData(Byte[] coverImageData) {
        this.coverImageData = coverImageData;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pdfFileData=" + Arrays.toString(pdfFileData) +
                ", coverImageData=" + Arrays.toString(coverImageData) +
                ", category=" + category +
                '}';
    }
}
