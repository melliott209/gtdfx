package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.controller.UpdateDialogController;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;

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

    public Optional<TaskUpdateDTO> updateTask(TaskUpdateDTO taskUpdateDTO) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Task");
        alert.setHeaderText(null);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/melliott/gtdfx/gtdclient/update_dialog.fxml"));
        Node root = loader.load();
        UpdateDialogController controller = loader.getController();
        alert.getDialogPane().setContent(root);
        controller.tfDescription.setText(taskUpdateDTO.getDescription());
        if (taskUpdateDTO.getDateDue() != null) {
            controller.cbDue.setSelected(true);
            controller.dueController.setFromOffsetDateTime(taskUpdateDTO.getDateDue());
        }
        if (taskUpdateDTO.getDateScheduled() != null) {
            controller.cbScheduled.setSelected(true);
            controller.scheduledController.setFromOffsetDateTime(taskUpdateDTO.getDateScheduled());
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            taskUpdateDTO.setDescription(controller.tfDescription.getText());
            if (controller.cbDue.isSelected()) {
                taskUpdateDTO.setDateDue(OffsetDateTime.parse(controller.dueController.getTimeStamp()));
            }
            if (controller.cbScheduled.isSelected()) {
                taskUpdateDTO.setDateScheduled(OffsetDateTime.parse(controller.scheduledController.getTimeStamp()));
            }
            return Optional.of(taskUpdateDTO);
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> notesDialog(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notes");
        alert.setHeaderText(null);
        TextArea textArea = getTextArea(task, alert);
        alert.getDialogPane().setContent(textArea);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Platform.runLater(textArea::requestFocus); // Start editing once the dialog opens
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) return Optional.of(textArea.getText());
        else return Optional.empty();
    }

    private static TextArea getTextArea(Task task, Alert alert) {
        TextArea textArea = new TextArea(task.getNotes() != null ? task.getNotes() : "");
        textArea.positionCaret(textArea.getLength());
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
