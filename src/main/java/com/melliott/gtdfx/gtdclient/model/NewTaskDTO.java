package com.melliott.gtdfx.gtdclient.model;

public class NewTaskDTO {
    private final String description;
    private final int userId;

    public NewTaskDTO(String description, int userId) {
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
