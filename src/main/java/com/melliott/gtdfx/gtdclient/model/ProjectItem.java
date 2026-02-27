package com.melliott.gtdfx.gtdclient.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectItem {
    private final IntegerProperty id;
    private final IntegerProperty taskId;
    private final IntegerProperty projectId;
    private final IntegerProperty level;
    private final StringProperty description;
    private final StringProperty status;

    public ProjectItem(int id, int taskId, int projectId, int level, String description, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.taskId = new SimpleIntegerProperty(taskId);
        this.projectId = new SimpleIntegerProperty(projectId);
        this.level = new SimpleIntegerProperty(level);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getTaskId() {
        return taskId.get();
    }

    public IntegerProperty taskIdProperty() {
        return taskId;
    }

    public int getProjectId() {
        return projectId.get();
    }

    public IntegerProperty projectIdProperty() {
        return projectId;
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }
}
