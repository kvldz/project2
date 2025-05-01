package hellofx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addProductButton;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        initializeUser();
    }

    private void initializeUser() {
        if (mainApp != null && mainApp.getCurrentUser() != null) {
            usernameLabel.setText("Welcome, " + mainApp.getCurrentUser());
        } else {
            usernameLabel.setText("Welcome, Guest");
        }
    }

    @FXML
    private void handleLogoutAction() {
        if (mainApp != null) {
            mainApp.setCurrentUser(null);
            mainApp.showLogin();
        }
    }

    @FXML
    private void handleAddProduct() {
        mainApp.showAddProduct();
    }
}
