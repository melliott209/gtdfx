package com.melliott.gtdfx.gtdclient;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.OffsetDateTime;

public class Task {
    private final ObjectProperty<OffsetDateTime> created;
    private final StringProperty description;

    public Task(OffsetDateTime created, String description) {
        this.created = new SimpleObjectProperty<>(created);
        this.description = new SimpleStringProperty(description);
    }

    public OffsetDateTime getCreated() {
        return created.get();
    }

    public ObjectProperty<OffsetDateTime> createdProperty() {
        return created;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
