package com.melliott.gtdfx.gtdclient.ui;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TwentyFourHourTimeField extends TwoDigitTimeField {
    public TwentyFourHourTimeField() {
        super("00");
    }

    @Override
    public void handleKeyInput() {
        addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().isDigitKey()) {
                if (getText().endsWith("0")) {
                    Platform.runLater(() -> setText("0" + e.getText()));
                } else if (getText().equals("01")) {
                    Platform.runLater(() -> setText("1" + e.getText()));
                } else if (getText().equals("02") && Integer.parseInt(e.getText()) <= 3) {
                    Platform.runLater(() -> setText("2" + e.getText()));
                }
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() != KeyCode.TAB) {
                e.consume();
            }
        });
    }
}
