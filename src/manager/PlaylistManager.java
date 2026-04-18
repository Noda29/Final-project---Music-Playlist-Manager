package manager;

import storage.DatabaseManager;
import java.sql.SQLException;
import java.util.List;

public class PlaylistManager {

    private DatabaseManager db;

    public PlaylistManager(DatabaseManager db) {
        this.db = db;
    }

    public void createPlaylist(String name) {
        try {
            db.addPlaylist(name);
            System.out.println("Playlist created!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showAllPlaylists() {
        try {
            List<String> playlists = db.getAllPlaylists();
            if (playlists.isEmpty()) {
                System.out.println("No playlists yet.");
            } else {
                playlists.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void renamePlaylist(int id, String newName) {
        try {
            db.updatePlaylist(id, newName);
            System.out.println("Playlist renamed!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deletePlaylist(int id) {
        try {
            db.deletePlaylist(id);
            System.out.println("Playlist deleted!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addSong(String title, String artist, int duration, String genre, int playlistId) {
        try {
            db.addSong(title, artist, duration, genre, playlistId);
            System.out.println("Song added!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showSongs(int playlistId) {
        try {
            List<String> songs = db.getSongsByPlaylist(playlistId);
            if (songs.isEmpty()) {
                System.out.println("No songs in this playlist.");
            } else {
                songs.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateSong(int id, String newTitle) {
        try {
            db.updateSong(id, newTitle);
            System.out.println("Song updated!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteSong(int id) {
        try {
            db.deleteSong(id);
            System.out.println("Song deleted!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
