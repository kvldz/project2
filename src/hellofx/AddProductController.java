package hellofx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import hellofx.Product;


public class AddProductController {

    private Main mainApp;

    private ObservableList<Product> productList;

    @FXML private TextField productNameField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> statusColumn;
    @FXML private Button homeButton;
    @FXML private Button orderButton;
    @FXML private Button logoutButton;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button resetButton;
    @FXML private Button deleteButton;
    @FXML private Label usernameLabel;


    private Product selectedProduct = null;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        initializeUser();
        this.productList = mainApp.getProductList();
        productTable.setItems(productList); 
    }
    

    private void initializeUser() {
        if (mainApp != null && mainApp.getCurrentUser() != null) {
            usernameLabel.setText(mainApp.getCurrentUser());
        } else {
            usernameLabel.setText("Welcome, Guest");
        }
    }



    @FXML
    private void initialize() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusComboBox.setItems(FXCollections.observableArrayList("Available", "Out of Stock"));

        productTable.setItems(productList);

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProduct = newSelection;
                fillForm(selectedProduct);
            }
        });
    }

    private void fillForm(Product product) {
        productNameField.setText(product.getName());
        quantityField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(String.valueOf(product.getPrice()));
        statusComboBox.setValue(product.getStatus());
    }

    @FXML
    private void handleAddAction() {
        try {
            String name = productNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            String status = statusComboBox.getValue();

            if (name.isEmpty() || status == null) {
                showAlert("All fields must be filled properly.");
                return;
            }

            Product product = new Product(name, quantity, price, status);
            productList.add(product);
            clearForm();
        } catch (NumberFormatException e) {
            showAlert("Quantity and price must be numbers.");
        }
    }

    @FXML
    private void handleUpdateAction() {
        if (selectedProduct != null) {
            try {
                selectedProduct.setName(productNameField.getText());
                selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));
                selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                selectedProduct.setStatus(statusComboBox.getValue());
                productTable.refresh();
                clearForm();
            } catch (NumberFormatException e) {
                showAlert("Quantity and price must be numbers.");
            }
        } else {
            showAlert("Select a product to update.");
        }
    }

    @FXML
    private void handleDeleteAction() {
        if (selectedProduct != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete this product?");
    
            ButtonType okButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);
    
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == okButton) {
                    productList.remove(selectedProduct);
                    clearForm();
                }
            });
        } else {
            showAlert("Select a product to delete.");
        }
    }

    
    

    @FXML
    private void handleResetAction() {
        clearForm();
    }

    private void clearForm() {
        productNameField.clear();
        quantityField.clear();
        priceField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        productTable.getSelectionModel().clearSelection();
        selectedProduct = null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleHomeButton() {
        if (mainApp != null) {
            mainApp.showMainApp();
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
private void handleOrderButton() {
    if (mainApp != null) {
        mainApp.showOrderButton();
    }
}



}
