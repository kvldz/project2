package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AddProductController {

    private Main mainApp;

    @FXML
    private Button homeButton;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleHomeButton() {
        if (mainApp != null) {
            mainApp.showMainApp();
        }
    }
}
