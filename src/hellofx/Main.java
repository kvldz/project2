package hellofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Stage primaryStage;
    private static final Map<String, String> users = new HashMap<>();
    private String currentUser;
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private DashboardController dashboardController;

    public static void main(String[] args) {
        users.put("admin", "admin123");
        launch(args);
    }

    public static Map<String, String> getUsers() {
        return users;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private double totalIncome = 0.0;
    private int totalOrders = 0;

    public double getTotalIncome() {
        return totalIncome;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void addToIncome(double amount) {
        totalIncome += amount;
        totalOrders++;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        productList.addAll(
                new Product("Chocolate Cake", 10, 499.00, "Available"),
                new Product("Banana Bread", 8, 180.00, "Available"),
                new Product("Blueberry Muffin", 20, 65.00, "Available"),
                new Product("Croissant", 15, 70.00, "Available"),
                new Product("Apple Pie", 5, 350.00, "Out of Stock"),
                new Product("Ensaymada", 25, 45.00, "Available"),
                new Product("Ube Cheese Pandesal", 30, 55.00, "Available"),
                new Product("Egg Pie", 12, 299.00, "Available"),
                new Product("Leche Flan", 10, 220.00, "Available"),
                new Product("Cheesecake Slice", 16, 150.00, "Available"),
                new Product("Spanish Bread", 40, 15.00, "Available"),
                new Product("Mamon", 18, 35.00, "Available"),
                new Product("Cassava Cake", 7, 250.00, "Available"),
                new Product("Brazo de Mercedes", 6, 280.00, "Available"),
                new Product("Yema Cake", 9, 300.00, "Available")
        );

        showLogin();
    }

    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginv2.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root, 620, 445);
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signupv2.fxml"));
            Parent root = loader.load();

            SignupController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root, 620, 445);
            primaryStage.setTitle("Sign Up");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addproduct.fxml"));
            Parent root = loader.load();

            AddProductController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root, 1100, 600);
            primaryStage.setTitle("Add Product");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOrderButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order.fxml"));
            Parent orderPage = loader.load();

            OrderController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(orderPage);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainApp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setMainApp(this);
            this.dashboardController = controller; // Set the dashboardController here

            Scene scene = new Scene(root, 1100, 600);
            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}