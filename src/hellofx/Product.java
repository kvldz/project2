package hellofx;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Product {
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final StringProperty status;

    @FXML private Button homeButton;
    @FXML private Button logoutButton;

    public Product(String name, int quantity, double price, String status) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() {
        return name.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getPrice() {
        return price.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
