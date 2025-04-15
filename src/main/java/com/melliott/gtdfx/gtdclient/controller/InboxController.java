package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.NewTaskDTO;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;
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
    private final HttpService httpService;
    private final AlertService alertService;
    private final JsonService jsonService;
    @FXML TableColumn<Task, OffsetDateTime> colSince;
    @FXML TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbInbox;
    @FXML TextField tfNewTask;
    @FXML Button btnAdd;
    @FXML Button btnDelete;

    public InboxController(AppServices appServices) {
        this.httpService = appServices.httpService;
        this.alertService = appServices.alertService;
        this.jsonService = appServices.jsonService;
    }

    public void initialize() {
        logger.info("Initializing InboxController...");
        ContextMenu contextMenu = new ContextMenu();
        Menu moveTo = new Menu("Move to...");
        MenuItem toNext = new MenuItem("Next");
        toNext.setOnAction(event -> {
            Task task = tbInbox.getSelectionModel().getSelectedItem();
            try {
                httpService.patch("http://localhost:8080/tasks/" + task.getId() + "/status", "{\"status\":\"NEXT\"}");
                loadTasks();
            } catch (IOException e) {
                alertService.error("Error while updating task: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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

        loadTasks();
    }

    public void loadTasks() {
        try {
            var response = httpService.get("http://localhost:8080/tasks?status=INBOX");
            if (response.statusCode() == 200) {
                inbox.setAll(jsonService.parseTaskList(response.body()));
            }
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
        NewTaskDTO task = new NewTaskDTO(tfNewTask.getText(), OffsetDateTime.now(), 0);
        String json = jsonService.createNewTask(task);
        try {
            var response = httpService.post("http://localhost:8080/tasks", json);
            if (response.statusCode() == 200) {
                alertService.info("Task added successfully");
                tfNewTask.setText("");
                loadTasks();
            } else alertService.error(response.statusCode() + ": " + response.body());
        } catch (IOException ioe) {
            alertService.error("Error while adding task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void deleteTask() {
        if (tbInbox.getSelectionModel().isEmpty()) return;
        Task task = tbInbox.getSelectionModel().getSelectedItem();
        try {
            httpService.delete("http://localhost:8080/tasks/" + task.getId());
            loadTasks();
        } catch (IOException ioe) {
            alertService.error("Error while deleting task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
