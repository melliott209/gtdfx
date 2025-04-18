package com.melliott.gtdfx.gtdclient.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatusService {
    private final StringProperty status = new SimpleStringProperty();

    public StringProperty statusProperty() {
        return status;
    }

    public void updateStatus(String status) {
        this.status.set(status);
    }
}
