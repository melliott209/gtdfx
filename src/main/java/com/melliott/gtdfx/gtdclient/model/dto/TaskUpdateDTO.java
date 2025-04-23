package com.melliott.gtdfx.gtdclient.model.dto;

import java.time.OffsetDateTime;

public class TaskUpdateDTO {
    private final int taskId;
    private String description;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateDue;
    private OffsetDateTime dateCompleted;
    private String status;
    private String notes;

    public TaskUpdateDTO(int id) {
        this.taskId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getDateDue() {
        return dateDue;
    }

    public void setDateDue(OffsetDateTime dateDue) {
        this.dateDue = dateDue;
    }

    public OffsetDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(OffsetDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return this.taskId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
