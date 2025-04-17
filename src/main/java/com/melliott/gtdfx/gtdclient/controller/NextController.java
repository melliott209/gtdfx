package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

public class NextController {
    private static final Logger logger = Logger.getLogger(NextController.class.getName());
    private final AlertService alertService;
    private final TaskRepository taskRepository;
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;
    @FXML Button btnComplete;

    ListProperty<Task> tasks = new SimpleListProperty<>(FXCollections.observableArrayList());

    public NextController(AppServices appServices, TaskRepository taskRepository) {
        this.alertService = appServices.alertService;
        this.taskRepository = taskRepository;
    }

    public void initialize() {
        logger.info("initializing...");
        setContextMenu();
        colSinceNext.setCellFactory(col -> new TimeAgoTableCell());
        colSinceNext.setCellValueFactory(col -> col.getValue().createdProperty());
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());
        tbTasks.itemsProperty().bind(tasks);

        getTasks();
    }

    private void setContextMenu() {
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

    public void getTasks() {
        try {
            tasks.setAll(taskRepository.getTasks("NEXT"));
        } catch (HttpTimeoutException te) {
            alertService.error("The request timed out");
        } catch (IOException e) {
            alertService.error("Error while loading tasks: " + e.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void updateTask(TaskUpdateDTO task) {
        try {
            if (!taskRepository.updateTask(task)) alertService.error("The task to be updated was not found");
            getTasks();
        } catch (IOException e) {
            alertService.error("Error while updating task: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void completeTask() {
        logger.info("completing task...");
        TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
        task.setStatus("COMPLETE");
        task.setDateCompleted(OffsetDateTime.now());
        updateTask(task);
    }
}
