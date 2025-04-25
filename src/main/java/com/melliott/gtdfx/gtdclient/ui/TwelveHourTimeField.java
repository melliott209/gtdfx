package com.melliott.gtdfx.gtdclient.ui;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TwelveHourTimeField extends TwoDigitTimeField {
    public TwelveHourTimeField() {
        super("12");
    }

    @Override
    public void handleKeyInput() {
        addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().isDigitKey()) {
                if (getText().equals("01") && Integer.parseInt(e.getText()) <= 2) {
                    Platform.runLater(() -> setText("1" + e.getText()));
                } else {
                    Platform.runLater(() -> setText("0" + e.getText()));
                }
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() == KeyCode.UP) {
                Platform.runLater(() -> {
                    setText(String.format("%02d", (Integer.parseInt(getText()) + 1) % 12));
                    selectRange(1, 2);
                });
            } else if (e.getCode() == KeyCode.DOWN) {
                Platform.runLater(() -> {
                    if (Integer.parseInt(getText()) > 1) {
                        setText(String.format("%02d", (Integer.parseInt(getText()) - 1)));
                    } else setText("12");
                    selectRange(1, 2);
                });
            } else if (e.getCode() != KeyCode.TAB) {
                e.consume();
            }
        });
    }
}
