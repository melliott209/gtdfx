package com.melliott.gtdfx.gtdclient.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
public class TaskCreateDTO {
    private final String description;
    private final int userId;
    @Setter private OffsetDateTime dueDate;
    @Setter private OffsetDateTime scheduledDate;

    public TaskCreateDTO(String description, int userId) {
        this.description = description;
        this.userId = userId;
    }
}
