package com.melliott.gtdfx.gtdclient.ui;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MinuteTimeField extends TwoDigitTimeField {
    public MinuteTimeField() {
        super("00");
    }

    @Override
    public void handleKeyInput() {
        addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().isDigitKey()) {
                if (getText().equals("01")) {
                    Platform.runLater(() -> setText("1" + e.getText()));
                } else if (getText().equals("02")) {
                    Platform.runLater(() -> setText("2" + e.getText()));
                } else if (getText().equals("03")) {
                    Platform.runLater(() -> setText("3" + e.getText()));
                } else if (getText().equals("04")) {
                    Platform.runLater(() -> setText("4" + e.getText()));
                } else if (getText().equals("05")) {
                    Platform.runLater(() -> setText("5" + e.getText()));
                } else {
                    Platform.runLater(() -> setText("0" + e.getText()));
                }
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() == KeyCode.UP) {
                setText(String.format("%02d", (Integer.parseInt(getText()) + 1) % 60));
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() == KeyCode.DOWN) {
                if (Integer.parseInt(getText()) >= 1) {
                    setText(String.format("%02d", (Integer.parseInt(getText()) - 1) % 60));
                } else {
                    setText("59");
                }
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() == KeyCode.RIGHT) {
                setText(String.format("%02d", (Integer.parseInt(getText()) + 5) % 60));
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() == KeyCode.LEFT) {
                if (Integer.parseInt(getText()) >= 5) {
                    setText(String.format("%02d", (Integer.parseInt(getText()) - 5) % 60));
                } else {
                    setText(String.format("%02d", 59 - (4 - Integer.parseInt(getText()))));
                }
                Platform.runLater(() -> selectRange(1, 2));
            } else if (e.getCode() != KeyCode.TAB) {
                e.consume();
            }
        });
    }
}
