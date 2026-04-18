package ui.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import manager.AuthManager;
import manager.PlaylistManager;
import manager.UserManager;
import storage.DatabaseManager;
import storage.FileManager;

public class MainApp extends Application {

    public static DatabaseManager db;
    public static PlaylistManager playlistManager;
    public static FileManager fileManager;
    public static AuthManager authManager;
    public static UserManager userManager;

    @Override
    public void start(Stage primaryStage) {
        // Show the login screen first
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
    }

    public static void main(String[] args) {
        // Initialize the database
        try {
            db = new DatabaseManager();
            db.connect();
            db.initTables();

            playlistManager = new PlaylistManager(db);
            fileManager = new FileManager(db);
            authManager = new AuthManager(db);
            userManager = new UserManager(db);

        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }

        launch(args); // launches JavaFX
    }
}