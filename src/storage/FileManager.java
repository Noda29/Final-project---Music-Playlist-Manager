package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private DatabaseManager db;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileManager(DatabaseManager db) {
        this.db = db;
    }

    // HELPER CLASS

    static class PlaylistData {
        String name;
        List<SongData> songs;
    }

    static class SongData {
        String title;
        String artist;
        int duration;
        String genre;
    }

    // JSON EXPORT

    public void exportToJson(String filename) {
        try {
            List<String> playlistNames = db.getAllPlaylists();
            List<PlaylistData> data = new ArrayList<>();

            for (String p : playlistNames) {
                int id = Integer.parseInt(p.split("\\.")[0].trim());
                String name = p.split("\\. ")[1].trim();

                PlaylistData pd = new PlaylistData();
                pd.name = name;
                pd.songs = new ArrayList<>();

                List<String> songs = db.getSongsByPlaylist(id);
                for (String s : songs) {
                    // формат: "id. title — artist"
                    String[] parts = s.split("\\. ", 2)[1].split(" — ");
                    SongData sd = new SongData();
                    sd.title = parts[0].trim();
                    sd.artist = parts[1].trim();
                    pd.songs.add(sd);
                }
                data.add(pd);
            }

            FileWriter writer = new FileWriter(filename);
            gson.toJson(data, writer);
            writer.close();
            System.out.println("Exported to " + filename);

        } catch (Exception e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }

    // JSON IMPORT

    public void importFromJson(String filename) {
        try {
            Reader reader = new FileReader(filename);
            Type listType = new TypeToken<List<PlaylistData>>(){}.getType();
            List<PlaylistData> data = gson.fromJson(reader, listType);
            reader.close();

            for (PlaylistData pd : data) {
                db.addPlaylist(pd.name);
                List<String> playlists = db.getAllPlaylists();
                String last = playlists.get(playlists.size() - 1);
                int playlistId = Integer.parseInt(last.split("\\.")[0].trim());

                for (SongData sd : pd.songs) {
                    db.addSong(sd.title, sd.artist, sd.duration, sd.genre, playlistId);
                }
            }
            System.out.println("Imported from " + filename);

        } catch (Exception e) {
            System.out.println("Import error: " + e.getMessage());
        }
    }

    // CSV EXPORT

    public void exportToCsv(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("playlist,title,artist,duration,genre");
            writer.newLine();

            List<String> playlists = db.getAllPlaylists();
            for (String p : playlists) {
                int id = Integer.parseInt(p.split("\\.")[0].trim());
                String name = p.split("\\. ")[1].trim();

                List<String> songs = db.getSongsByPlaylist(id);
                for (String s : songs) {
                    String[] parts = s.split("\\. ", 2)[1].split(" — ");
                    String title = parts[0].trim();
                    String artist = parts[1].trim();
                    writer.write(name + "," + title + "," + artist + ",,");
                    writer.newLine();
                }
            }
            writer.close();
            System.out.println("Exported to " + filename);

        } catch (Exception e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }

    // CSV IMPORT

    public void importFromCsv(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            reader.readLine(); // пропускаем заголовок

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String playlistName = parts[0].trim();
                String title = parts[1].trim();
                String artist = parts[2].trim();
                int duration = parts.length > 3 && !parts[3].isEmpty()
                        ? Integer.parseInt(parts[3].trim()) : 0;
                String genre = parts.length > 4 ? parts[4].trim() : "";

                // Создаём плейлист если не существует
                db.addPlaylist(playlistName);
                List<String> playlists = db.getAllPlaylists();
                String last = playlists.get(playlists.size() - 1);
                int playlistId = Integer.parseInt(last.split("\\.")[0].trim());

                db.addSong(title, artist, duration, genre, playlistId);
            }
            reader.close();
            System.out.println("Imported from " + filename);

        } catch (Exception e) {
            System.out.println("Import error: " + e.getMessage());
        }
    }
}
