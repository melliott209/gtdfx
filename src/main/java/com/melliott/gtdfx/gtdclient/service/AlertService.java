package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.model.Task;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    public String notesDialog(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        TextArea textArea = getTextArea(task, alert);
        alert.getDialogPane().setContent(textArea);
        alert.getButtonTypes().setAll(ButtonType.OK);
        Platform.runLater(textArea::requestFocus); // Start editing once the dialog opens
        alert.showAndWait();
        return textArea.getText();
    }

    private static TextArea getTextArea(Task task, Alert alert) {
        TextArea textArea = new TextArea(task.getNotes() != null ? task.getNotes() : "");
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) { // Shift+Enter for new line
                    int caretPosition = textArea.getCaretPosition();
                    textArea.insertText(caretPosition, "\n");
                } else { // Enter for submit notes
                    event.consume();
                    Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.fire();
                }
            }
        });
        return textArea;
    }
}
