package com.melliott.gtdfx.gtdclient.ui;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TwoDigitTimeField extends TextField {

    protected TwoDigitTimeField(String startingValue) {
        super(startingValue);
        setPrefColumnCount(2);
        onFocused();
        handleKeyInput();
    }

    protected void onFocused() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                Platform.runLater(() -> selectRange(1, 2));
            }
        });
    }

    public abstract void handleKeyInput();
}
