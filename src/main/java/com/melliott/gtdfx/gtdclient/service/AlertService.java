package com.melliott.gtdfx.gtdclient.service;

import javafx.scene.control.Alert;

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
}
