package SportHub.Entity;

public class Cours {
    private int id;
    private String name;
    private String description;
    private String pdfFile;
    private String cover;
    private Category category;

    public Cours() {
    }

    public Cours(String name, String description, String pdfFile, String cover, Category category) {
        this.name = name;
        this.description = description;
        this.pdfFile = pdfFile;
        this.cover = cover;
        this.category = category;
    }

    public Cours(int id, String name, String description, String pdfFile, String cover, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pdfFile = pdfFile;
        this.cover = cover;
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

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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
                ", pdfFile='" + pdfFile + '\'' +
                ", cover='" + cover + '\'' +
                ", category=" + category +
                '}';
    }
}
