package com.melliott.gtdfx.gtdclient.model;

import java.time.OffsetDateTime;

public class NewTaskDTO {
    private final String description;
    private final OffsetDateTime dateRecorded;
    private final int userId;

    public NewTaskDTO(String description, OffsetDateTime dateRecorded, int userId) {
        this.description = description;
        this.dateRecorded = dateRecorded;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getDateRecorded() {
        return dateRecorded;
    }

    public int getUserId() {
        return userId;
    }
}
