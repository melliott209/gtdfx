package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.NewTaskDTO;
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
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

public class InboxController {
    private final Logger logger = Logger.getLogger(InboxController.class.getName());
    private final ListProperty<Task> inbox = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final AlertService alertService;
    private final TaskRepository taskRepository;
    @FXML TableColumn<Task, OffsetDateTime> colSince;
    @FXML TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbInbox;
    @FXML TextField tfNewTask;
    @FXML Button btnAdd;
    @FXML Button btnDelete;

    public InboxController(AppServices appServices, TaskRepository taskRepository) {
        this.alertService = appServices.alertService;
        this.taskRepository = taskRepository;
    }

    public void initialize() {
        logger.info("Initializing InboxController...");
        ContextMenu contextMenu = new ContextMenu();
        Menu moveTo = new Menu("Move to...");
        MenuItem toNext = new MenuItem("Next");
        toNext.setOnAction(event -> {
            TaskUpdateDTO task = new TaskUpdateDTO(tbInbox.getSelectionModel().getSelectedItem().getId());
            task.setStatus("NEXT");
            updateTask(task);
        });
        moveTo.getItems().add(toNext);
        contextMenu.getItems().add(moveTo);

        tbInbox.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            return row;
        });

        colSince.setCellValueFactory(col -> col.getValue().createdProperty());
        colDesc.setCellValueFactory(col -> col.getValue().descriptionProperty());
        colSince.setCellFactory(col -> new TimeAgoTableCell());

        tfNewTask.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addTask();
            }
        });
        btnAdd.disableProperty().bind(tfNewTask.textProperty().isEmpty());
        btnDelete.disableProperty().bind(tbInbox.getSelectionModel().selectedItemProperty().isNull());
        tbInbox.itemsProperty().bind(inbox);

        getTasks();
    }

    public void getTasks() {
        try {
            inbox.setAll(taskRepository.getTasks("INBOX"));
        } catch (HttpTimeoutException te) {
            alertService.error("The request timed out");
        } catch (IOException e) {
            alertService.error("Error while loading tasks: " + e.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void addTask() {
        if (tfNewTask.getText() == null || tfNewTask.getText().isBlank()) return;
        NewTaskDTO task = new NewTaskDTO(tfNewTask.getText(), 0);
        try {
            if (taskRepository.addTask(task)) {
                alertService.info("Task added successfully");
                tfNewTask.setText("");
                getTasks();
            } else alertService.error("Error while adding task");
        } catch (IOException ioe) {
            alertService.error("Error while adding task: " + ioe.getMessage());
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

    public void deleteTask() {
        if (tbInbox.getSelectionModel().isEmpty()) return;
        try {
            if (!taskRepository.deleteTask(tbInbox.getSelectionModel().getSelectedItem().getId())) {
                alertService.error("The task to be deleted was not found");
            }
            getTasks();
        } catch (IOException ioe) {
            alertService.error("Error while deleting task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
