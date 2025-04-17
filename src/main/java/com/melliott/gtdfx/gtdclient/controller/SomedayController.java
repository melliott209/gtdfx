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

public class SomedayController {
    private static final Logger logger = Logger.getLogger(SomedayController.class.getName());
    private final TaskRepository taskRepository;
    private final AlertService alertService;
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;

    ListProperty<Task> tasks = new SimpleListProperty<>(FXCollections.observableArrayList());

    public SomedayController(AppServices appServices, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.alertService = appServices.alertService;
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
        MenuItem toNext = new MenuItem("Next");
        toNext.setOnAction(event -> {
            TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
            task.setStatus("NEXT");
            updateTask(task);
        });
        MenuItem toWaiting = new MenuItem("Waiting");
        toWaiting.setOnAction(event -> {
            TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
            task.setStatus("WAITING");
            updateTask(task);
        });
        moveTo.getItems().addAll(toNext, toWaiting);
        contextMenu.getItems().add(moveTo);
        tbTasks.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            return row;
        });
    }

    public void getTasks() {
        try {
            tasks.setAll(taskRepository.getTasks("SOMEDAY"));
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
}
