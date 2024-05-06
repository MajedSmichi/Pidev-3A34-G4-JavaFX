package Gestionreclamation.Produit.Entity;

public class Categorie_p {

    private int id;

    private String name;

    public Categorie_p(){}

    public Categorie_p(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Categorie_p{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
