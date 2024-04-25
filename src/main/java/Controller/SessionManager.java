package Controller;

import Entity.User;

public class SessionManager {
    private static SessionManager instance;

    private boolean isAuthenticated;
    private String userEmail;

    private User currentUser;

    private SessionManager() {
        isAuthenticated = false;
        userEmail = null;
        currentUser = null;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            this.isAuthenticated = true;
            this.userEmail = user.getEmail(); // Assuming your User entity has getEmail() method
        } else {
            clearSession(); // If null is passed, clear the session
        }
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public String getUserFirstName() {
        if (currentUser != null) {
            return currentUser.getPrenom();
        } else {
            return "";
        }
    }

    public String getUserLastName() {
        if (currentUser != null) {
            return currentUser.getNom();
        } else {
            return "";
        }
    }

    // Clear all session information
    public void clearSession() {
        isAuthenticated = false;
        userEmail = null;
        currentUser = null;
    }
}
