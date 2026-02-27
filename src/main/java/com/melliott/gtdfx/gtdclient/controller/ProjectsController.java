package com.melliott.gtdfx.gtdclient.controller;


import com.melliott.gtdfx.gtdclient.model.*;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ContextMenuBuilder;
import com.melliott.gtdfx.gtdclient.util.TimeUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProjectsController extends AbstractTaskController {
    @FXML
    Accordion main;

    public ProjectsController(AppServices appServices, TaskRepository taskRepository, ProjectRepository projectRepository) {
        super(taskRepository, projectRepository, appServices.alertService, appServices.statusService);
    }

    @Override
    protected void getTasks() {
        try {
            List<ProjectItem> projects = projectRepository.getAllProjects();
            if (!projects.isEmpty()) {
                List<TitledPane> panes = new ArrayList<>();
                for (ProjectItem project : projects) {
                    TableView<Task> content = new TableView<>();
                    content.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
                    content.setContextMenu(new ContextMenuBuilder()
                            .item("Complete", () -> completeTask(content.getSelectionModel().getSelectedItem().getId()))
                            .build()
                    );
                    TableColumn<Task, String> created = new TableColumn<>("Created");
                    created.setMaxWidth(100);
                    created.setCellValueFactory(val -> new SimpleStringProperty(TimeUtil.timeAgo(val.getValue().getCreated(), OffsetDateTime.now())));
                    content.getColumns().add(created);
                    TableColumn<Task, String> description = new TableColumn<>("Description");
                    description.setCellValueFactory(new PropertyValueFactory<>("description"));
                    content.getColumns().add(description);
                    List<Task> projectTasks = getTasksForProject(project.getProjectId());
                    for (Task task : projectTasks) {
                        content.getItems().add(task);
                    }
                    TitledPane projectContainer = new TitledPane(project.getDescription(), content);
                    panes.add(projectContainer);
                }
                main.getPanes().setAll(panes);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (main.getPanes().isEmpty() && getStatus() == Status.PROJECTS) {
                    main.getPanes().add(new TitledPane("No projects found.", new VBox()));
                }

        }
    }

    private List<Task> getTasksForProject(int projectId) {
        return projectRepository.getTasksForProject(projectId);
    }

    @Override
    protected void setContextMenu() {
        // document why this method is empty
    }

    @Override
    protected void setCellValueFactories() {
        // document why this method is empty
    }

    @Override
    protected void setCellFactories() {
        // document why this method is empty
    }

    @Override
    public Status getStatus() {
        return Status.PROJECTS;
    }
}
