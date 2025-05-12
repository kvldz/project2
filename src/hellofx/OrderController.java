package hellofx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.logging.Logger;

public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class.getName());
    private static final String SENIOR = "Senior";
    private static final String PWD = "PWD";
    private static final String ERROR = "Error";

    @FXML
    private Button addProductButton;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private TextField priceTextField;

    @FXML
    private ComboBox<String> discountComboBox;

    @FXML
    private TextField amountPaidTextField;

    @FXML
    private Label balanceLabel;

    @FXML
    private TableView<OrderItem> orderTable;

    @FXML
    private TableColumn<OrderItem, String> productNameColumn;

    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderItem, Double> priceColumn;

    @FXML
    private TableView<AvailableProductView> availableProductsTableView;
    @FXML
    private TableColumn<AvailableProductView, String> availableProductNameColumn;
    @FXML
    private TableColumn<AvailableProductView, Integer> availableQuantityColumn;
    @FXML
    private TableColumn<AvailableProductView, Double> availablePriceColumn;
// kinukuha ang list sa table para maview - katulad ng available products - if may laman mag uupdate
    private final ObservableList<OrderItem> orderList = FXCollections.observableArrayList();
    private final ObservableList<AvailableProductView> availableProductViewList = FXCollections.observableArrayList();

    private Main mainApp; // need ref sa mainapp  to access yung data dito, kasi kinukuha yung data dito para mapakita sa dashboard 

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        updateAvailableProducts();
    } 

    @FXML
    private void handleAddProduct() {
        if (mainApp != null) {
            mainApp.showAddProduct();
        }
    }

    @FXML
    public void initialize() {
        availableProductsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productComboBox.setValue(newSelection.getName());
                quantitySpinner.getValueFactory().setValue(1); 
                priceTextField.setText(String.valueOf(newSelection.getPrice()));
            } else {
                productComboBox.setValue(null);
                quantitySpinner.getValueFactory().setValue(1);
                priceTextField.clear();
            }
        });

        productComboBox.setOnAction(e -> {
            String selectedProductName = productComboBox.getValue();
            if (selectedProductName != null) {
                availableProductViewList.stream()
                        .filter(apv -> apv.getName().equals(selectedProductName))
                        .findFirst()
                        .ifPresent(selectedProduct -> {
                            priceTextField.setText(String.valueOf(selectedProduct.getPrice()));
                        });
            } else {
                priceTextField.clear();
            }
        });

        SpinnerValueFactory<Integer> quantityFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantitySpinner.setValueFactory(quantityFactory);

        priceTextField.setEditable(false);

        discountComboBox.setItems(FXCollections.observableArrayList("None", SENIOR, PWD));
        discountComboBox.getSelectionModel().select("None");
        discountComboBox.setOnAction(e -> updateTotalAndBalance());

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            OrderItem orderItem = event.getRowValue();
            int newQuantity = event.getNewValue();
            if (newQuantity > 0) {
                mainApp.getProductList().stream()
                        .filter(p -> p.getName().equals(orderItem.getProductName()) && "Available".equalsIgnoreCase(p.getStatus()))
                        .findFirst()
                        .ifPresent(product -> {
                            orderItem.setQuantity(newQuantity);
                            orderItem.setPrice(newQuantity * product.getPrice());
                            orderTable.refresh();
                            updateTotalAndBalance();
                        });
            } else {
                showAlert(ERROR, "Quantity must be greater than 0.");
                orderTable.refresh(); 
            }
        });

        orderTable.setItems(orderList);
        orderTable.setEditable(true); 

        availableProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        availablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableProductsTableView.setItems(availableProductViewList);

        updateProductComboBoxItems();
    }

    private void updateAvailableProducts() {
        if (mainApp == null) return;

        ObservableList<Product> allProducts = mainApp.getProductList();
        availableProductViewList.clear();

        for (Product product : allProducts) {
            if ("Available".equalsIgnoreCase(product.getStatus())) {
                availableProductViewList.add(new AvailableProductView(product.getName(), product.getQuantity(), product.getPrice()));
            }
        }

        updateProductComboBoxItems();
    }

    private void updateProductComboBoxItems() {
        if (mainApp == null) return;

        ObservableList<String> availableProductNames = FXCollections.observableArrayList();
        for (Product product : mainApp.getProductList()) {
            if ("Available".equalsIgnoreCase(product.getStatus())) {
                availableProductNames.add(product.getName());
            }
        }
        productComboBox.setItems(availableProductNames);
    }

    private void addProductToOrder(String productName, int quantity, double unitPrice) {
        for (OrderItem item : orderList) {
            if (item.getProductName().equals(productName)) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setPrice(item.getQuantity() * unitPrice);
                orderTable.refresh();
                updateTotalAndBalance();
                return;
            }
        }
        double totalPrice = unitPrice * quantity;
        orderList.add(new OrderItem(productName, quantity, totalPrice));
        updateTotalAndBalance();
    }

    @FXML
    public void handleAddOrder() {
        String productName = productComboBox.getValue();
        Integer quantity = quantitySpinner.getValue();

        if (productName == null || mainApp == null) {
            showAlert(ERROR, "Please select a valid product from the Available Products table or dropdown.");
            return;
        }

        availableProductViewList.stream()
                .filter(apv -> apv.getName().equals(productName))
                .findFirst()
                .ifPresentOrElse(
                        selectedProductView -> {
                            if (selectedProductView.getQuantity() >= quantity) {
                                addProductToOrder(productName, quantity, selectedProductView.getPrice());
                            } else {
                                showAlert(ERROR, "Insufficient stock for " + productName + ". Available: " + selectedProductView.getQuantity());
                            }
                        },
                        () -> showAlert(ERROR, "Product not found in available products.")
                );
    }

    private void updateTotalAndBalance() {
        double subtotal = orderList.stream().mapToDouble(OrderItem::getPrice).sum();

        double discountPercent = switch (discountComboBox.getValue()) {
            case SENIOR, PWD -> 0.20;
            default -> 0.0;
        };

        double discountedTotal = subtotal - (subtotal * discountPercent);

        double amountPaid = 0.0;
        try {
            if (!amountPaidTextField.getText().isEmpty()) {
                amountPaid = Double.parseDouble(amountPaidTextField.getText());
            }
        } catch (NumberFormatException e) {
            showAlert(ERROR, "Invalid payment amount.");
        }

        double balance = amountPaid - discountedTotal;
        balanceLabel.setText("₱" + String.format("%.2f", balance));
    }

    @FXML
    public void handlePay() {
        updateTotalAndBalance();
        double subtotal = orderList.stream().mapToDouble(OrderItem::getPrice).sum();
        double discountPercent = switch (discountComboBox.getValue()) {
            case SENIOR, PWD -> 0.20;
            default -> 0.0;
        };
        double totalDue = subtotal - (subtotal * discountPercent);

        double amountPaid;
        try {
            amountPaid = Double.parseDouble(amountPaidTextField.getText());
        } catch (NumberFormatException e) {
            showAlert(ERROR, "Enter a valid payment amount.");
            return;
        }

        if (amountPaid < totalDue) {
            showAlert("Payment Error", "Insufficient amount. Please enter enough to cover the total.");
        } else {
            showAlert("Success", "Payment completed. Thank you!");
            handleReceipt();
            updateProductQuantities();
            updateAvailableProducts(); 

            if (mainApp != null) {
                mainApp.addToIncome(totalDue);  
                if (mainApp.getDashboardController() != null) {
                    mainApp.getDashboardController().updateIncomeAndOrders();
                }
            }
        }
    }

    private void updateProductQuantities() {
        if (mainApp != null) {
            for (OrderItem orderItem : orderList) {
                mainApp.getProductList().stream()
                        .filter(p -> p.getName().equals(orderItem.getProductName()))
                        .findFirst()
                        .ifPresent(product -> {
                            int newQuantity = product.getQuantity() - orderItem.getQuantity();
                            product.setQuantity(Math.max(0, newQuantity)); 
                        });
            }
        }
    }

   @FXML
public void handleReceipt() {
    String storeName = "Angono Eats";
    String address = "123 Rizal Avenue, Angono, Calabarzon, Philippines";
    String contactNumber = "+63 912 345 6789";
    String tinNumber = "123-456-789-000"; 
    String transactionDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    int transactionNumber = (int) (Math.random() * 10000); 

    StringBuilder receipt = new StringBuilder();

    receipt.append(String.format("%-30s%n", storeName.toUpperCase()));
    receipt.append(String.format("%-30s%n", address));
    receipt.append(String.format("Tel: %-26s%n", contactNumber));
    receipt.append(String.format("TIN: %-27s%n", tinNumber));
    receipt.append(String.format("------------------------------%n"));

    receipt.append(String.format("Date: %-22s%n", transactionDate));
    receipt.append(String.format("Transaction #: %-15d%n", transactionNumber));
    receipt.append(String.format("------------------------------%n"));
    receipt.append(String.format("%-20s %-5s %8s%n", "Item", "Qty", "Price"));
    receipt.append(String.format("------------------------------%n"));

    for (OrderItem item : orderList) {
        receipt.append(String.format("%-20s x%-4d ₱%-7.2f%n", item.getProductName(), item.getQuantity(), item.getPrice()));
    }

    receipt.append(String.format("------------------------------%n"));

    double subtotal = orderList.stream().mapToDouble(OrderItem::getPrice).sum();
    double discountRate = switch (discountComboBox.getValue()) {
        case SENIOR, PWD -> 0.20;
        default -> 0.0;
    };
    double discountAmount = subtotal * discountRate;
    double discountedTotal = subtotal - discountAmount;
    double amountPaid = 0.0;
    try {
        amountPaid = Double.parseDouble(amountPaidTextField.getText());
    } catch (NumberFormatException e) {
        amountPaid = 0.0; 
    }
    double change = discountedTotal <= amountPaid ? amountPaid - discountedTotal : 0.0; 

    receipt.append(String.format("%-25s ₱%-9.2f%n", "Subtotal:", subtotal));
    receipt.append(String.format("%-25s %-8.0f%%%n", "Discount:", discountRate * 100));
    receipt.append(String.format("%-25s ₱%-9.2f%n", "Total Due:", discountedTotal));
    receipt.append(String.format("%-25s ₱%-9.2f%n", "Amount Paid:", amountPaid));
    receipt.append(String.format("%-25s ₱%-9.2f%n", "Change:", change));

    receipt.append(String.format("------------------------------%n"));
    receipt.append(String.format("%-30s%n", "Thank you for your purchase!"));
    receipt.append(String.format("%-30s%n", "Have a great day!"));
    receipt.append(String.format("==============================%n"));

    TextArea textArea = new TextArea(receipt.toString());
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setPrefSize(400, 400); 

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Receipt");
    alert.setHeaderText("Customer Receipt");
    alert.getDialogPane().setContent(textArea);
    alert.showAndWait();
}
    @FXML
    public void handleReset() {
        productComboBox.getSelectionModel().clearSelection();
        discountComboBox.getSelectionModel().select("None");
        quantitySpinner.getValueFactory().setValue(1);
        priceTextField.clear();
        amountPaidTextField.clear();
        balanceLabel.setText("₱0.00");
        orderList.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleHomeButton() {
        if (mainApp != null) {
            mainApp.showMainApp();
        }
    }

    public static class OrderItem {
        private final SimpleStringProperty productName;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty price;

        public OrderItem(String productName, int quantity, double price) {
            this.productName = new SimpleStringProperty(productName);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
        }

        public String getProductName() {
            return productName.get();
        }

        public SimpleStringProperty productNameProperty() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName.set(productName);
        }

        public int getQuantity() {
            return quantity.get();
        }

        public SimpleIntegerProperty quantityProperty() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
        }

        public double getPrice() {
            return price.get();
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }

        public void setPrice(double price) {
            this.price.set(price);
        }
    }

    public static class AvailableProductView {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty price;

        public AvailableProductView(String name, int quantity, double price) {
            this.name = new SimpleStringProperty(name);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public SimpleIntegerProperty quantityProperty() {
            return quantity;
        }

        public double getPrice() {
            return price.get();
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }
    }
}