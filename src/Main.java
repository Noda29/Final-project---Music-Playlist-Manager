import manager.AuthManager;
import manager.PlaylistManager;
import manager.UserManager;
import storage.DatabaseManager;
import storage.FileManager;
import ui.CLI;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.initTables();

        PlaylistManager playlistManager = new PlaylistManager(db);
        FileManager fileManager = new FileManager(db);
        AuthManager authManager = new AuthManager(db);
        UserManager userManager = new UserManager(db);

        CLI cli = new CLI(playlistManager, fileManager, authManager, userManager);
        cli.start();

        db.disconnect();
    }
}