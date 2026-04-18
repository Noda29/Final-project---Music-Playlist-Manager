package storage;

import model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:music.db";
    private Connection connection;

    // CONNECTION

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL);
        System.out.println("Connected to database.");
    }

    public void disconnect() throws SQLException {
        if (connection != null) connection.close();
    }

    // INIT TABLES

    public void initTables() throws SQLException {
        String createPlaylists = """
            CREATE TABLE IF NOT EXISTS playlists (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            );
        """;

        String createSongs = """
            CREATE TABLE IF NOT EXISTS songs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                duration INTEGER,
                genre TEXT,
                playlist_id INTEGER,
                FOREIGN KEY (playlist_id) REFERENCES playlists(id)
            );
        """;

        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                login TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            );
        """;


        Statement stmt = connection.createStatement();
        stmt.execute(createPlaylists);
        stmt.execute(createSongs);
        stmt.execute(createUsers);
    }

    // PLAYLIST CRUD

    public void addPlaylist(String name) throws SQLException {
        String sql = "INSERT INTO playlists (name) VALUES (?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.executeUpdate();
    }

    public List<String> getAllPlaylists() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM playlists";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getInt("id") + ". " + rs.getString("name"));
        }
        return list;
    }

    public void updatePlaylist(int id, String newName) throws SQLException {
        String sql = "UPDATE playlists SET name = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newName);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    public void deletePlaylist(int id) throws SQLException {
        String sql = "DELETE FROM playlists WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    // SONG CRUD

    public void addSong(String title, String artist, int duration, String genre, int playlistId) throws SQLException {
        String sql = "INSERT INTO songs (title, artist, duration, genre, playlist_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setString(2, artist);
        stmt.setInt(3, duration);
        stmt.setString(4, genre);
        stmt.setInt(5, playlistId);
        stmt.executeUpdate();
    }

    public List<String> getSongsByPlaylist(int playlistId) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE playlist_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, playlistId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(rs.getInt("id") + ". " + rs.getString("title") + " — " + rs.getString("artist"));
        }
        return list;
    }

    public void updateSong(int id, String newTitle) throws SQLException {
        String sql = "UPDATE songs SET title = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newTitle);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    public void deleteSong(int id) throws SQLException {
        String sql = "DELETE FROM songs WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    // USER CRUD

    public void addUser(String login, String password, String role) throws SQLException {
        String sql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, login);
        stmt.setString(2, password);
        stmt.setString(3, role);
        stmt.executeUpdate();
    }

    public User getUserByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, login);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("role")
            );
        }
        return null;
    }

    public List<String> getAllUsers() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getInt("id") + ". " + rs.getString("login") + " [" + rs.getString("role") + "]");
        }
        return list;
    }

    public void updateUserRole(String login, String newRole) throws SQLException {
        String sql = "UPDATE users SET role = ? WHERE login = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newRole);
        stmt.setString(2, login);
        stmt.executeUpdate();
    }
}