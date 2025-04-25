package com.melliott.gtdfx.gtdclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UpdateDialogController {
    @FXML public TextField tfDescription;
    @FXML public CheckBox cbDue;
    @FXML public VBox due;
    @FXML public TimeInputController dueController;
    @FXML public CheckBox cbScheduled;
    @FXML public VBox scheduled;
    @FXML public TimeInputController scheduledController;

    public void initialize() {
        due.managedProperty().bind(due.visibleProperty());
        due.visibleProperty().bind(cbDue.selectedProperty());

        scheduled.managedProperty().bind(scheduled.visibleProperty());
        scheduled.visibleProperty().bind(cbScheduled.selectedProperty());
    }
}
