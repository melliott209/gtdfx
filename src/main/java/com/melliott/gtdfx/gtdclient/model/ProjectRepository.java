package com.melliott.gtdfx.gtdclient.model;

import com.melliott.gtdfx.gtdclient.model.dto.ProjectItemCreateDTO;

import java.util.List;

public interface ProjectRepository {
    List<ProjectItem> getAllProjects();
    List<Task> getTasksForProject(int projectId);
    boolean addProject(ProjectItemCreateDTO project);
}
