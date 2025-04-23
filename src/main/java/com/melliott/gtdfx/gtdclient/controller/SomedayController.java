package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        ContextMenu contextMenu = new ContextMenu();
        Menu moveTo = new Menu("Move to...");
        MenuItem toNext = new MenuItem("Next");
        toNext.setOnAction(event -> {
            TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
            task.setStatus("NEXT");
            updateTask(task);
        });
        MenuItem toWaiting = new MenuItem("Waiting");
        toWaiting.setOnAction(event -> moveToWaiting(tbTasks.getSelectionModel().getSelectedItem().getId()));
        moveTo.getItems().addAll(toNext, toWaiting);
        contextMenu.getItems().add(moveTo);
        tbTasks.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            return row;
        });
    }

    @Override
    public Status getStatus() {
        return Status.SOMEDAY;
    }
}
