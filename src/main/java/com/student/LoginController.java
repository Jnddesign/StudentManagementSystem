package com.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField     txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label         lblError;

    private static final String VALID_USERNAME = AppConfig.get("app.username");
    private static final String VALID_PASSWORD = AppConfig.get("app.password");

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            openMainWindow();
        } else {
            showError("Invalid username or password. Try again.");
            txtPassword.clear();
        }
    }

    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/student/main.fxml")
            );
            Scene scene = new Scene(loader.load(), 800, 570);

            // Get current stage and swap the scene
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("Student Record Management System");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
        } catch (Exception e) {
            showError("Failed to open main window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        lblError.setText("⚠ " + message);
    }
}