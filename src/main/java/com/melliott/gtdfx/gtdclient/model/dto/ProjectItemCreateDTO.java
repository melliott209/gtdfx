package com.melliott.gtdfx.gtdclient.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ProjectItemCreateDTO {
    private final int taskId;
    @Setter
    private int projectId;
    @Setter
    private int level;

    public ProjectItemCreateDTO(int id) {
        this.taskId = id;
    }

}
