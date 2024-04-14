package Entity;

import java.time.LocalDateTime; // Import LocalDateTime for createdAt and updatedAt fields

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private int numTele;
    private String password;
    private String adresse;
    private String avatar;            // URL to the user's avatar image
    private LocalDateTime createdAt;  // Time when the user was created
    private LocalDateTime updatedAt;  // Time when the user's information was last updated
    private boolean isVerified;       // Flag to indicate whether the user's account is verified

    public User() {}

    // Constructors
    public User(int id, String nom, String prenom, String email, String role, int numTele, String password, String adresse, String avatar, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isVerified) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.numTele = numTele;
        this.password = password;
        this.adresse = adresse;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isVerified = isVerified;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", numTele=" + numTele +
                ", password='" + password + '\'' +
                ", adresse='" + adresse + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isVerified=" + isVerified +
                '}';
    }
}
