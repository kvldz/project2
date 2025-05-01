    package hellofx;

    import javafx.application.Application;
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

        public static void main(String[] args) {
            // Preload sample user
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

        public Stage getPrimaryStage() {
            return primaryStage;
        }

        @Override
        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage;
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
        

        public void showMainApp() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
                Parent root = loader.load();

                DashboardController controller = loader.getController();
                controller.setMainApp(this); // this also calls initializeUser()

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
