package com.melliott.gtdfx.gtdclient.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ProjectItemUpdateDTO {
    private final int id;
    @Setter
    private int level;
    @Setter
    private int projectId;
    @Setter
    private int taskId;

    public ProjectItemUpdateDTO(int id) {
        this.id = id;
    }

}
