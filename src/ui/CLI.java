package ui;

import manager.PlaylistManager;
import manager.AuthManager;
import manager.UserManager;
import storage.FileManager;
import util.InputValidator;
import java.util.Scanner;

public class CLI {

    private PlaylistManager playlistManager;
    private Scanner scanner;
    private FileManager fileManager;
    private AuthManager authManager;
    private UserManager userManager;

    public CLI(PlaylistManager playlistManager, FileManager fileManager,
               AuthManager authManager, UserManager userManager) {
        this.playlistManager = playlistManager;
        this.fileManager = fileManager;
        this.authManager = authManager;
        this.userManager = userManager;
        this.scanner = new Scanner(System.in);
    }

    //START
    public void start() {
        System.out.println("Welcome to Music Playlist Manager");

        boolean loggedIn = false; //authentification
        while (!loggedIn) {
            loggedIn = authMenu();
        }

        boolean running = true; //main menu
        while (running) {
            showMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1 -> playlistMenu();
                case 2 -> songMenu();
                case 3 -> fileMenu();
                case 4 -> {
                    if (authManager.isAdmin()) adminMenu();
                    else System.out.println("Access denied!");
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
        System.out.println("Goodbye!");
    }

    //MAIN MENU
    private void showMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1. Manage Playlists");
        System.out.println("2. Manage Songs");
        System.out.println("3. Export / Import");
        if (authManager.isAdmin()) {
            System.out.println("4. Admin Panel");
        }
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    //PLAYLIST MENU
    private void playlistMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nPLAYLISTS");
            System.out.println("1. Show all playlists");
            System.out.println("2. Create playlist");
            System.out.println("3. Rename playlist");
            System.out.println("4. Delete playlist");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            switch (readInt()) {
                case 1 -> playlistManager.showAllPlaylists();
                case 2 -> {
                    System.out.print("Enter playlist name: ");
                    String name = scanner.nextLine();
                    if (!InputValidator.isValidString(name)) {
                        System.out.println("Name cannot be empty!");
                    } else {
                        playlistManager.createPlaylist(name);
                    }
                }
                case 3 -> {
                    System.out.print("Enter playlist id: ");
                    int id = readInt();
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    playlistManager.renamePlaylist(id, newName);
                }
                case 4 -> {
                    System.out.print("Enter playlist id: ");
                    int id = readInt();
                    playlistManager.deletePlaylist(id);
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void songMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nSONGS");
            System.out.println("1. Show songs in playlist");
            System.out.println("2. Add song");
            System.out.println("3. Rename song");
            System.out.println("4. Delete song");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            switch (readInt()) {
                case 1 -> {
                    System.out.print("Enter playlist id: ");
                    int id = readInt();
                    playlistManager.showSongs(id);
                }
                case 2 -> {
                    System.out.print("Enter playlist id: ");
                    int playlistId = readInt();
                    if (!InputValidator.isValidId(playlistId)) {
                        System.out.println("Invalid id!");
                        break;
                    }
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    if (!InputValidator.isValidString(title)) {
                        System.out.println("Title cannot be empty!");
                        break;
                    }
                    System.out.print("Enter artist: ");
                    String artist = scanner.nextLine();
                    if (!InputValidator.isValidString(artist)) {
                        System.out.println("Artist cannot be empty!");
                        break;
                    }
                    System.out.print("Enter duration (seconds): ");
                    int duration = readInt();
                    if (!InputValidator.isValidDuration(duration)) {
                        System.out.println("Duration must be greater than 0!");
                        break;
                    }
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    playlistManager.addSong(title, artist, duration, genre, playlistId);
                }
                case 3 -> {
                    System.out.print("Enter song id: ");
                    int id = readInt();
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    playlistManager.updateSong(id, newTitle);
                }
                case 4 -> {
                    System.out.print("Enter song id: ");
                    int id = readInt();
                    playlistManager.deleteSong(id);
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void fileMenu() {
        System.out.println("\nFILE MENU");
        System.out.println("1. Export to JSON");
        System.out.println("2. Import from JSON");
        System.out.println("3. Export to CSV");
        System.out.println("4. Import from CSV");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        switch (readInt()) {
            case 1 -> fileManager.exportToJson("playlists.json");
            case 2 -> fileManager.importFromJson("playlists.json");
            case 3 -> fileManager.exportToCsv("playlists.csv");
            case 4 -> fileManager.importFromCsv("playlists.csv");
            case 0 -> {}
            default -> System.out.println("Invalid choice.");
        }
    }

    private boolean authMenu() {
        System.out.println("\nAUTH");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Choose: ");

        switch (readInt()) {
            case 1 -> {
                System.out.print("Login: ");
                String login = scanner.nextLine()   ;
                System.out.print("Password: ");
                String password = scanner.nextLine();
                return authManager.login(login, password);
            }
            case 2 -> {
                System.out.print("Login: ");
                String login = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                return authManager.register(login, password);
            }
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid choice.");
        }
        return false;
    }

    private void adminMenu() {
        System.out.println("\nADMIN PANEL");
        System.out.println("1. Show all users");
        System.out.println("2. Change user role");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        switch (readInt()) {
            case 1 -> userManager.showAllUsers(authManager.getCurrentUser());
            case 2 -> {
                System.out.print("Enter login: ");
                String login = scanner.nextLine();
                System.out.print("Enter new role (ADMIN/USER): ");
                String role = scanner.nextLine();
                userManager.changeRole(authManager.getCurrentUser(), login, role);
            }
            case 0 -> {}
            default -> System.out.println("Invalid choice.");
        }
    }

    //HELPER METHOD
    private int readInt() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
            return -1;
        }
    }
}
