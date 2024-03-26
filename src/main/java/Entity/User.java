package Entity;


public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private int numTele;
    private String motDePass;
    private String adresse;

    public User(){}
    // Constructors
    public User(int id, String nom, String prenom, String email, String role, int numTele, String motDePass, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.numTele = numTele;
        this.motDePass = motDePass;
        this.adresse = adresse;
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


    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", numTele=" + numTele +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
