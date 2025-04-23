package com.melliott.gtdfx.gtdclient.model;

import javafx.beans.property.*;

import java.time.OffsetDateTime;

public class Task {
    private IntegerProperty id;
    private ObjectProperty<OffsetDateTime> created;
    private ObjectProperty<OffsetDateTime> due;
    private ObjectProperty<OffsetDateTime> completed;
    private StringProperty description;
    private StringProperty status;
    private StringProperty notes;
    private IntegerProperty userId;

    public Task() {
        this.id = new SimpleIntegerProperty();
        this.created = new SimpleObjectProperty<>();
        this.due = new SimpleObjectProperty<>();
        this.completed = new SimpleObjectProperty<>();
        this.description = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.notes = new SimpleStringProperty();
        this.userId = new SimpleIntegerProperty();
    }

    public Task(int id, OffsetDateTime created, OffsetDateTime due, OffsetDateTime completed, String description, String status, int userId) {
        this.id = new ReadOnlyIntegerWrapper(id);
        this.created = new SimpleObjectProperty<>(created);
        this.due = new SimpleObjectProperty<>(due);
        this.completed = new SimpleObjectProperty<>(completed);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty(status);
        this.notes = new SimpleStringProperty();
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

    public void setId(int id) {
        this.id.set(id);
    }

    public void setCreated(OffsetDateTime created) {
        this.created.set(created);
    }

    public void setDue(OffsetDateTime due) {
        this.due.set(due);
    }

    public void setCompleted(OffsetDateTime completed) {
        this.completed.set(completed);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
