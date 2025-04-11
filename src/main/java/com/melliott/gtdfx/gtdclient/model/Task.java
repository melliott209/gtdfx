package com.melliott.gtdfx.gtdclient.model;

import javafx.beans.property.*;

import java.time.OffsetDateTime;

public class Task {
    private final IntegerProperty id;
    private final ObjectProperty<OffsetDateTime> created;
    private final ObjectProperty<OffsetDateTime> due;
    private final ObjectProperty<OffsetDateTime> completed;
    private final StringProperty description;
    private final StringProperty status;
    private final IntegerProperty userId;

    public Task(int id, OffsetDateTime created, OffsetDateTime due, OffsetDateTime completed, String description, String status, int userId) {
        this.id = new ReadOnlyIntegerWrapper(id);
        this.created = new SimpleObjectProperty<>(created);
        this.due = new SimpleObjectProperty<>(due);
        this.completed = new SimpleObjectProperty<>(completed);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty(status);
        this.userId = new SimpleIntegerProperty(userId);
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

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public OffsetDateTime getDue() {
        return due.get();
    }

    public ObjectProperty<OffsetDateTime> dueProperty() {
        return due;
    }

    public OffsetDateTime getCompleted() {
        return completed.get();
    }

    public ObjectProperty<OffsetDateTime> completedProperty() {
        return completed;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }
}
