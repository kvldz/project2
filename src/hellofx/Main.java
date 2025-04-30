package hellofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Stage primaryStage;
    private static final Map<String, String> users = new HashMap<>(); // In-memory user storage

    public static Map<String, String> getUsers() {
        return users;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLogin();
    }

    public void showLogin() {
        try {
            URL url = getClass().getResource("loginv2.fxml");
            if (url == null) {
                System.err.println("FXML file not found: loginv2.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 620, 445); // Set appropriate size
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();

            LoginController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading loginv2.fxml: " + e.getMessage());
        }
    }

    public void showSignup() {
        try {
            URL url = getClass().getResource("signupv2.fxml");
            if (url == null) {
                System.err.println("FXML file not found: signupv2.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 620, 445); // Set appropriate size
            primaryStage.setTitle("Sign Up");
            primaryStage.setScene(scene);
            primaryStage.show();
            SignupController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading signupv2.fxml: " + e.getMessage());
        }
    }

    public void showMainApp() {
        try {
            URL url = getClass().getResource("hellofx.fxml");
            if (url == null) {
                System.err.println("FXML file not found: hellofx.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Main Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading hellofx.fxml: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}