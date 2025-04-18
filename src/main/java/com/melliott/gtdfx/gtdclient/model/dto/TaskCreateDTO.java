package com.melliott.gtdfx.gtdclient.model.dto;

public class TaskCreateDTO {
    private final String description;
    private final int userId;

    public TaskCreateDTO(String description, int userId) {
        this.description = description;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public int getUserId() {
        return userId;
    }
}
