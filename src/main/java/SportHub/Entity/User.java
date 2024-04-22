package SportHub.Entity;

import java.util.Collection;
import java.util.ArrayList;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private int numTele;
    private String motDePass;
    private String adresse;
    private Collection<Reclamation> reclamations;
    //private Collection<Reponse> reponses;


    // Constructors
    public User() {
        // Default constructor
        this.reclamations = new ArrayList<>();
        //this.reponses = new ArrayList<>();

    }

    // Getters and Setters
    // Implement your getters and setters here...

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNumTele() {
        return numTele;
    }

    public void setNumTele(int numTele) {
        this.numTele = numTele;
    }

    public String getMotDePass() {
        return motDePass;
    }

    public void setMotDePass(String motDePass) {
        this.motDePass = motDePass;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Collection<Reclamation> getReclamations() {
        return reclamations;
    }

    public void setReclamations(Collection<Reclamation> reclamations) {
        this.reclamations = reclamations;
    }

  /*  public Collection<Reponse> getReponses() {
        return reponses;
    }*/

/*    public void setReponses(Collection<Reponse> reponses) {
        this.reponses = reponses;
    }*/



    // Add and remove methods for relations...
}
