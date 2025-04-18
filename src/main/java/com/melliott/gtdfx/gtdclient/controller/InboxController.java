package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.dto.TaskCreateDTO;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.time.OffsetDateTime;

public class InboxController extends AbstractTaskController {
    @FXML TableColumn<Task, OffsetDateTime> colSince;
    @FXML TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbTasks;
    @FXML TextField tfNewTask;
    @FXML Button btnAdd;
    @FXML Button btnDelete;

    public InboxController(AppServices appServices, TaskRepository taskRepository) {
        super(taskRepository, appServices.alertService, appServices.statusService);
    }

    @Override
    public void initialize() {
        tfNewTask.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addTask();
            }
        });
        btnAdd.disableProperty().bind(tfNewTask.textProperty().isEmpty());
        btnAdd.setOnAction(event -> addTask());
        btnDelete.disableProperty().bind(tbTasks.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.setOnAction(event -> deleteTask(tbTasks.getSelectionModel().getSelectedItem().getId()));
        tbTasks.itemsProperty().bind(tasks);
        super.initialize();
    }

    @Override
    protected void setCellFactories() {
        colSince.setCellFactory(col -> new TimeAgoTableCell());
    }

    @Override
    protected void setCellValueFactories() {
        colSince.setCellValueFactory(col -> col.getValue().createdProperty());
        colDesc.setCellValueFactory(col -> col.getValue().descriptionProperty());
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
        moveTo.getItems().addAll(toNext, toWaiting, toSomeday);
        contextMenu.getItems().add(moveTo);
        tbTasks.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            return row;
        });
    }

    private void addTask() {
        if (tfNewTask.getText() == null || tfNewTask.getText().isBlank()) return;
        TaskCreateDTO task = new TaskCreateDTO(tfNewTask.getText(), 0);
        try {
            if (taskRepository.addTask(task)) {
                statusService.updateStatus("Task added.");
                tfNewTask.setText("");
                getTasks();
            } else alertService.error("Error while adding task");
        } catch (IOException ioe) {
            alertService.error("Error while adding task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Status getStatus() {
        return Status.INBOX;
    }
}
