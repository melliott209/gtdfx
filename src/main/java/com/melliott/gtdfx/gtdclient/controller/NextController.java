package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.OffsetDateTime;

public class NextController extends AbstractTaskController {
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;
    @FXML Button btnComplete;

    public NextController(AppServices appServices, TaskRepository taskRepository) {
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
        MenuItem toWaiting = new MenuItem("Waiting");
        toWaiting.setOnAction(event -> {
            TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
            task.setStatus("WAITING");
            updateTask(task);
        });
        MenuItem toSomeday = new MenuItem("Someday");
        toSomeday.setOnAction(event -> {
            TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
            task.setStatus("SOMEDAY");
            updateTask(task);
        });
        moveTo.getItems().addAll(toWaiting, toSomeday);

        MenuItem complete = new MenuItem("Complete Task");
        complete.setOnAction(event -> completeTask());

        contextMenu.getItems().addAll(moveTo, complete);
        tbTasks.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            return row;
        });
    }

    public void completeTask() {
        TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
        task.setStatus("COMPLETE");
        task.setDateCompleted(OffsetDateTime.now());
        updateTask(task);
    }

    @Override
    public Status getStatus() {
        return Status.NEXT;
    }
}
