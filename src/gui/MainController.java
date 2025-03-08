package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {

    @FXML
    private TextArea textArea;

    @FXML
    void onButtonClick(ActionEvent event) {
        textArea.setText("Hello, JavaFX!");
    }

}
