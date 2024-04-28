package GestionSalle.Entity;

import java.util.Objects;

public class Salle {
    private int id;
    private String nom;
    private String addresse;
    private int num_tel;
    private int capacite;
    private String description;
    private int nbr_client;
    private String logo_salle;

    public Salle(int id, String nom, String addresse, int num_tel, int capacite, String description, int nbr_client, String logo_salle) {
        this.id = id;
        this.nom = nom;
        this.addresse = addresse;
        this.num_tel = num_tel;
        this.capacite = capacite;
        this.description = description;
        this.nbr_client = nbr_client;
        this.logo_salle = logo_salle;
    }
    public Salle(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbr_client() {
        return nbr_client;
    }

    public void setNbr_client(int nbr_client) {
        this.nbr_client = nbr_client;
    }

    public String getLogo_salle() {
        return logo_salle;
    }

    public void setLogo_salle(String logo_salle) {
        this.logo_salle = logo_salle;
    }

}
