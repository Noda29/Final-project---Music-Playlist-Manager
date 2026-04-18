package ui.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class PlaylistView {

    private Stage stage;
    private int playlistId;
    private String playlistName;

    public PlaylistView(Stage stage, int playlistId, String playlistName) {
        this.stage = stage;
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public void show() {
        ListView<String> songList = new ListView<>();
        refreshSongs(songList);

        // fields for new songs
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField artistField = new TextField();
        artistField.setPromptText("Artist");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration (seconds)");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        Label messageLabel = new Label();

        Button addBtn = new Button("Add Song");
        addBtn.setOnAction(e -> {
            String title = titleField.getText();
            String artist = artistField.getText();
            String genre = genreField.getText();

            if (title.isEmpty() || artist.isEmpty()) {
                messageLabel.setText("Fill title and artist!");
                return;
            }

            int duration = 0;
            try {
                duration = Integer.parseInt(durationField.getText());
            } catch (NumberFormatException ex) {
                messageLabel.setText("Duration must be a number!");
                return;
            }

            MainApp.playlistManager.addSong(title, artist, duration, genre, playlistId);
            titleField.clear();
            artistField.clear();
            durationField.clear();
            genreField.clear();
            refreshSongs(songList);
            messageLabel.setText("Song added!");
        });

        Button deleteBtn = new Button("Delete Song");
        deleteBtn.setOnAction(e -> {
            String selected = songList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                messageLabel.setText("Select a song!");
                return;
            }
            int id = Integer.parseInt(selected.split("\\.")[0].trim());
            MainApp.playlistManager.deleteSong(id);
            refreshSongs(songList);
            messageLabel.setText("Song deleted!");
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            MainWindow mainWindow = new MainWindow(stage);
            mainWindow.show();
        });

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(addBtn, deleteBtn, backBtn);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                new Label("Playlist: " + playlistName),
                songList,
                titleField,
                artistField,
                durationField,
                genreField,
                buttonRow,
                messageLabel
        );

        stage.setScene(new Scene(layout, 500, 550));
        stage.show();
    }

    private void refreshSongs(ListView<String> listView) {
        try {
            List<String> songs = MainApp.db.getSongsByPlaylist(playlistId);
            listView.getItems().clear();
            listView.getItems().addAll(songs);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}