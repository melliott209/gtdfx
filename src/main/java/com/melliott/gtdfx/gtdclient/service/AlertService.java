package com.melliott.gtdfx.gtdclient.service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class AlertService {
    public void alert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void info(String message) {
        alert(Alert.AlertType.INFORMATION, null, message);
    }

    public void error(String message) {
        alert(Alert.AlertType.ERROR, null, message);
    }

    public String notesDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        TextArea textArea = new TextArea();
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) { // Shift+Enter for new line
                    textArea.appendText("\n");
                } else { // Enter for submit notes
                    Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.fire();
                }
            }
        });
        alert.getDialogPane().setContent(textArea);
        alert.getButtonTypes().setAll(ButtonType.OK);
        Platform.runLater(textArea::requestFocus); // Start editing once the dialog opens
        alert.showAndWait();
        return textArea.getText();
    }
}
