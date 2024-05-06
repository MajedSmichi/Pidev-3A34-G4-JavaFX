package Gestionreclamation.Entity.Salle;

import java.sql.Timestamp;

public class Activite {
    private int id;
    private  int salle_id;
    private String nom;
    private Timestamp date;
    private int nbr_max;
    private String description;
    private String image_activte;
    private String coach;

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalle_id() {
        return salle_id;
    }

    public void setSalle_id(int salle_id) {
        this.salle_id = salle_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getNbr_max() {
        return nbr_max;
    }

    public void setNbr_max(int nbr_max) {
        this.nbr_max = nbr_max;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image_activte;
    }

    public void setImage(String image) {
        this.image_activte = image;
    }

    public Activite(int id, int salle_id, String nom, Timestamp date, int nbr_max, String description, String image, String coach){
        this.id = id;
        this.salle_id = salle_id;
        this.nom = nom;
        this.date = date;
        this.nbr_max = nbr_max;
        this.description = description;
        this.image_activte = image;
        this.coach = coach;
    }
    public Activite() {
    }
}
