package SportHub.Entity;

import java.time.LocalDateTime; // Import LocalDateTime for createdAt and updatedAt fields
import java.util.Arrays;
import java.util.UUID;

public class User {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String[] roles;
    private int numTele;
    private String password;
    private String adresse;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isVerified;

    public User() {

    }

    // Constructors
    public User(String id, String nom, String prenom, String email, String[] roles, int numTele, String password, String adresse, String avatar, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isVerified) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.roles = roles;
        this.numTele = numTele;
        this.password = password;
        this.adresse = adresse;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isVerified = isVerified;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
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
                ", roles=" + Arrays.toString(roles) +
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