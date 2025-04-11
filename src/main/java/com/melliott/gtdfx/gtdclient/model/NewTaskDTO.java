package com.melliott.gtdfx.gtdclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class NewTaskDTO {
    private String description;
    private OffsetDateTime dateRecorded;
    private int userId;
}
