package model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private String createdDate;
    private List<Song> songs;

    public Playlist(String name, String createdDate) {
        this.name = name;
        this.createdDate = createdDate;
        this.songs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public List<Song> getSongs() {
        return songs;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void addSong(Song song){
        songs.add(song);
    }
    public void removeSong(Song song){
        songs.remove(song);
    }

    public int getTotalDuration() {
        return songs.stream().mapToInt(Song::getDuration).sum();
    }

    @Override
    public String toString() {
        return name + " (" + songs.size() + " songs)";
    }
}
