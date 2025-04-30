package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Access the user map from the Main class
        Map<String, String> users = Main.getUsers();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            // Successful login
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
            if (mainApp != null) {
                mainApp.showMainApp(); // Switch to the main application scene
            }
        } else {
            // Login failed
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            usernameField.clear();
            passwordField.clear();
        }
    }

    @FXML
    public void handleSignupButtonAction(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showSignup(); // Switch to the signup scene
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
