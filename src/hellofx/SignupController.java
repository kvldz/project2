// SignupController.java
package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Map;

public class SignupController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signupButton;
    @FXML
    private Button cancelButton;

    private Main mainApp;

    private static final Map<String, String> users = Main.getUsers();

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleSignupButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            passwordField.clear();
            confirmPasswordField.clear();
            return;
        }

        if (users.containsKey(username)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose a different one.");
            return;
        }

        users.put(username, password);
        showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Account created for " + username + ". You can now log in.");

        if (mainApp != null) {
            mainApp.showLogin();
        }
    }

    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLogin();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}