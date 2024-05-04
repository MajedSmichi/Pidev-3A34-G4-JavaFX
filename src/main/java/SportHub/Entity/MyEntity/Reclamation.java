package SportHub.Entity.MyEntity;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private String nom;
    private String prenom;

    private String email;
    private int numTele;
    private String etat;
    private String sujet;
    private String description;
    private LocalDateTime date;
    //private Reponse reponse;
    private User utilisateur;

    public Reclamation(int id, String nom, String prenom, String email, int numTele, String etat, String sujet, String description, LocalDateTime date,  User utilisateur) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTele = numTele;
        this.etat = etat;
        this.sujet = sujet;
        this.description = description;
        this.date = date;
       // this.reponse = reponse;
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", numTele='" + numTele + '\'' +
                ", etat='" + etat + '\'' +
                ", sujet='" + sujet + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +

                '}';
    }

    // Constructors
    public Reclamation() {
        // Default constructor
    }

    // Getters and Setters
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
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumTele() {
        return numTele;
    }

    public void setNumTele(int numTele) {
        this.numTele = numTele;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

/*    public Reponse getReponse() {
        return reponse;
    }*/

  /*  public void setReponse(Reponse reponse) {
        this.reponse = reponse;
    }*/

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
}
