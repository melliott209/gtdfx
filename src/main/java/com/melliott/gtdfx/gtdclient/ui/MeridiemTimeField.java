package com.melliott.gtdfx.gtdclient.ui;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MeridiemTimeField extends TwoDigitTimeField {
    public MeridiemTimeField() {
        super("AM");
    }

    @Override
    protected void onFocused() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                Platform.runLater(this::selectAll);
            }
        });
    }

    @Override
    public void handleKeyInput() {
        addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.A) {
                Platform.runLater(() -> {
                    setText("AM");
                    selectAll();
                });
            } else if (e.getCode() == KeyCode.P) {
                Platform.runLater(() -> {
                    setText("PM");
                    selectAll();
                });
            } else if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                Platform.runLater(() -> {
                    setText(getText().equals("AM") ? "PM" : "AM");
                    selectAll();
                });
            } else if (e.getCode() != KeyCode.TAB) {
                e.consume();
            }
        });
    }
}
