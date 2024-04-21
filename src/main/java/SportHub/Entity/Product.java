package SportHub.Entity;

public class Product {
    private int id;
    private String name;
    private int price;
    private int quantite;
    private String description;
    private SportHub.Entity.Categorie_p category;
    private String image;

    public Product(){}

    public Product(int id, Categorie_p categorie, int quantite, double price, String name, String description, String image){}

    public Product (Categorie_p category, int quantite, double price, String name, String description, String image) {
    }

    public Product(int id, String name, int quantite, int price,  Categorie_p category, String description, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantite = quantite;
        this.category = category;
        this.description = description;
        this.image = image;
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

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public SportHub.Entity.Categorie_p getCategory() {
        return category;
    }

    public void setCategory(Categorie_p category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantite=" + quantite +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", image='" + image + '\'' +
                '}';
    }

    public void setCategory(String name) {
    }
}
