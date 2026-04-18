package manager;

import model.User;
import storage.DatabaseManager;
import java.sql.SQLException;
import java.util.List;

public class UserManager {

    private DatabaseManager db;

    public UserManager(DatabaseManager db) {
        this.db = db;
    }

    // show all users (only for admin)
    public void showAllUsers(User currentUser) {
        if (!currentUser.getRole().equals("ADMIN")) {
            System.out.println("Access denied! Admins only.");
            return;
        }
        try {
            List<String> users = db.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                users.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // change role (only for admin)
    public void changeRole(User currentUser, String login, String newRole) {
        if (!currentUser.getRole().equals("ADMIN")) {
            System.out.println("Access denied! Admins only.");
            return;
        }
        if (!newRole.equals("ADMIN") && !newRole.equals("USER")) {
            System.out.println("Invalid role! Use ADMIN or USER.");
            return;
        }
        try {
            db.updateUserRole(login, newRole);
            System.out.println("Role updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
