package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;

public class DashboardController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addProductButton;

   @FXML private Button orderButton;

    @FXML
private Label totalOrdersLabel;

@FXML
private Label totalIncomeLabel;


    @FXML
    private Label availableProductLabel;

    private Main mainApp;

   public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
    mainApp.setDashboardController(this); 
    initializeUser();
    updateAvailableProductCount();
    updateIncomeAndOrders();
}


    private void initializeUser() {
        if (mainApp != null && mainApp.getCurrentUser() != null) {
            usernameLabel.setText(mainApp.getCurrentUser());
        } else {
            usernameLabel.setText("Welcome, Guest");
        }
    }

    private void updateAvailableProductCount() {
        if (mainApp == null) return;

        ObservableList<Product> productList = mainApp.getProductList();

        int totalAvailable = productList.stream()
            .filter(p -> "Available".equalsIgnoreCase(p.getStatus()))
            .mapToInt(Product::getQuantity)
            .sum();

        availableProductLabel.setText(String.valueOf(totalAvailable));
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
        if (mainApp != null) {
            mainApp.showAddProduct();
        }
    }

   @FXML
private void handleOrderButton() {
    if (mainApp != null) {
        mainApp.showOrderButton();
    }
}


public void updateIncomeAndOrders() {
    if (mainApp != null) {
        double totalIncome = mainApp.getTotalIncome();
        int totalOrders = mainApp.getTotalOrders();
        totalIncomeLabel.setText("₱" + String.format("%.2f", totalIncome));
        totalOrdersLabel.setText("" + totalOrders);
    }
}
}