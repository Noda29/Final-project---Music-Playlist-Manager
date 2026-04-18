package manager;

import model.User;
import storage.DatabaseManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthManager {

    private DatabaseManager db;
    private User currentUser;

    public AuthManager(DatabaseManager db) {
        this.db = db;
    }

    // password hashing
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error: " + e.getMessage());
        }
    }

    // registtration
    public boolean register(String login, String password) {
        try {
            User existing = db.getUserByLogin(login);
            if (existing != null) {
                System.out.println("User already exists!");
                return false;
            }
            String hashed = hashPassword(password);
            db.addUser(login, hashed, "USER");
            System.out.println("Registered successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // login
    public boolean login(String login, String password) {
        try {
            User user = db.getUserByLogin(login);
            if (user == null) {
                System.out.println("User not found!");
                return false;
            }
            String hashed = hashPassword(password);
            if (user.getPassword().equals(hashed)) {
                currentUser = user;
                System.out.println("Welcome, " + user.getLogin() + "! Role: " + user.getRole());
                return true;
            } else {
                System.out.println("Wrong password!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole().equals("ADMIN");
    }
    public void logout() {
        currentUser = null;
    }
}
