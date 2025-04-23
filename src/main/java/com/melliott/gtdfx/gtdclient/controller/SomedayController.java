package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ContextMenuBuilder;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.OffsetDateTime;

public class SomedayController extends AbstractTaskController {
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;

    public SomedayController(AppServices appServices, TaskRepository taskRepository) {
        super(taskRepository, appServices.alertService, appServices.statusService);
    }

    @Override
    public void initialize() {
        tbTasks.itemsProperty().bind(tasks);
        super.initialize();
    }

    @Override
    protected void setCellValueFactories() {
        colSinceNext.setCellValueFactory(col -> col.getValue().createdProperty());
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());
    }

    @Override
    protected void setCellFactories() {
        colSinceNext.setCellFactory(col -> new TimeAgoTableCell());
    }

    @Override
    protected void setContextMenu() {
        tbTasks.setContextMenu(new ContextMenuBuilder()
                .submenu(new ContextMenuBuilder.MenuBuilder("Move to...")
                        .item("Next", () -> moveToNext(getSelectedTask().getId()))
                        .item("Waiting", () -> moveToWaiting(getSelectedTask()))
                        .build())
                .item("Delete", () -> deleteTask(getSelectedTask().getId()))
                .build());
    }

    private Task getSelectedTask() {
        return tbTasks.getSelectionModel().getSelectedItem();
    }

    @Override
    public Status getStatus() {
        return Status.SOMEDAY;
    }
}
