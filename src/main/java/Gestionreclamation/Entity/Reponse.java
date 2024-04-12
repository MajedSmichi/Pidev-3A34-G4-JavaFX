package Gestionreclamation.Entity;

import java.time.LocalDateTime;


public class Reponse {


    private int id;
    private LocalDateTime date;
    private String reponse;

    private Reclamation idReclamation;

    private User utilisateur;

    public Reponse(  LocalDateTime date, String reponse) {

        this.date = date;
        this.reponse = reponse;
    }

    public Reponse(int id, LocalDateTime date, String reponse, Reclamation idReclamation, User utilisateur) {
        this.id = id;
        this.date = date;
        this.reponse = reponse;
        this.idReclamation = idReclamation;
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                ", date=" + date +
                ", reponse='" + reponse + '\'' +


                '}';
    }



    // Constructors
    public Reponse() {
        // Default constructor
    }
    public Reclamation getidReclamation() {
        return idReclamation;
    }
    public void setidReclamation(Reclamation $idReclamation) {
        this.idReclamation = $idReclamation;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
}
