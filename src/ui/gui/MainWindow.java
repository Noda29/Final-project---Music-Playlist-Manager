package ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class MainWindow {

    private Stage stage;

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        ListView<String> playlistView = new ListView<>();
        refreshPlaylists(playlistView);

        TextField nameField = new TextField();
        nameField.setPromptText("Playlist name");

        Label messageLabel = new Label();

        // create button
        Button createBtn = new Button("Create");
        createBtn.setOnAction(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                messageLabel.setText("Enter a name!");
                return;
            }
            MainApp.playlistManager.createPlaylist(name);
            nameField.clear();
            refreshPlaylists(playlistView);
            messageLabel.setText("Playlist created!");
        });

        // delete button
        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            String selected = playlistView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                messageLabel.setText("Select a playlist!");
                return;
            }
            int id = Integer.parseInt(selected.split("\\.")[0].trim());
            MainApp.playlistManager.deletePlaylist(id);
            refreshPlaylists(playlistView);
            messageLabel.setText("Playlist deleted!");
        });

        // open songs button
        Button openBtn = new Button("Open Songs");
        openBtn.setOnAction(e -> {
            String selected = playlistView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                messageLabel.setText("Select a playlist!");
                return;
            }
            int id = Integer.parseInt(selected.split("\\.")[0].trim());
            String name = selected.split("\\. ")[1].trim();
            PlaylistView pv = new PlaylistView(stage, id, name);
            pv.show();
        });

        // export and import button
        Button exportJsonBtn = new Button("Export JSON");
        exportJsonBtn.setOnAction(e -> {
            MainApp.fileManager.exportToJson("playlists.json");
            messageLabel.setText("Exported to playlists.json");
        });

        Button importJsonBtn = new Button("Import JSON");
        importJsonBtn.setOnAction(e -> {
            MainApp.fileManager.importFromJson("playlists.json");
            refreshPlaylists(playlistView);
            messageLabel.setText("Imported from playlists.json");
        });

        // exit button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            MainApp.authManager.logout();
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        // buttons layout
        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(createBtn, deleteBtn, openBtn);

        HBox fileRow = new HBox(10);
        fileRow.getChildren().addAll(exportJsonBtn, importJsonBtn);

        // main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                new Label("Welcome, " + MainApp.authManager.getCurrentUser().getLogin() + "!"),
                new Label("Your Playlists:"),
                playlistView,
                nameField,
                buttonRow,
                fileRow,
                logoutBtn,
                messageLabel
        );

        stage.setTitle("Music Playlist Manager");
        stage.setScene(new Scene(layout, 500, 500));
        stage.show();
    }

    private void refreshPlaylists(ListView<String> listView) {
        try {
            List<String> playlists = MainApp.db.getAllPlaylists();
            listView.getItems().clear();
            listView.getItems().addAll(playlists);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
