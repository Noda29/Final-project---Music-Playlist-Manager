package ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Input fields
        TextField loginField = new TextField();
        loginField.setPromptText("Login");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label messageLabel = new Label();

        // buttons
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        // logic of button Login
        loginBtn.setOnAction(e -> {
            String login = loginField.getText();
            String password = passwordField.getText();

            if (login.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Fill in all fields!");
                return;
            }

            boolean success = MainApp.authManager.login(login, password);
            if (success) {
                MainWindow mainWindow = new MainWindow(stage);
                mainWindow.show();
            } else {
                messageLabel.setText("Wrong login or password!");
            }
        });

        // logic of button Register
        registerBtn.setOnAction(e -> {
            String login = loginField.getText();
            String password = passwordField.getText();

            if (login.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Fill in all fields!");
                return;
            }

            boolean success = MainApp.authManager.register(login, password);
            if (success) {
                messageLabel.setText("Registered! Now login.");
            } else {
                messageLabel.setText("User already exists!");
            }
        });

        // layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.getChildren().addAll(
                new Label("Music Playlist Manager"),
                loginField,
                passwordField,
                loginBtn,
                registerBtn,
                messageLabel
        );

        stage.setTitle("Login");
        stage.setScene(new Scene(layout, 350, 300));
        stage.show();
    }
}
