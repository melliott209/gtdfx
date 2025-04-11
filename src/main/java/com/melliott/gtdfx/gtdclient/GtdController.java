package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.model.NewTaskDTO;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Component
@Scope("prototype")
public class GtdController {
    private final Logger logger = Logger.getLogger(GtdController.class.getName());
    private final HttpService httpService;
    private final AlertService alertService;
    private final JsonService jsonService;

    @FXML TabPane tabPane;
    @FXML Tab tabInbox;
    @FXML TableColumn<Task, OffsetDateTime> colCreated;
    @FXML TableColumn<Task, OffsetDateTime> colSince;
    @FXML TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbInbox;
    @FXML TextField tfNewTask;

    public GtdController(HttpService httpService, AlertService alertService, JsonService jsonService) {
        this.httpService = httpService;
        this.alertService = alertService;
        this.jsonService = jsonService;
    }

    public void initialize() {
        logger.info("initializing...");

        colCreated.setCellValueFactory(col -> col.getValue().createdProperty());
        colSince.setCellValueFactory(col -> col.getValue().createdProperty());
        colDesc.setCellValueFactory(col -> col.getValue().descriptionProperty());
        colCreated.setCellFactory(row -> new TableCell<>() {
            @Override
            protected void updateItem(OffsetDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(item.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                }
            }
        });
        colSince.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(OffsetDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(ElapsedTimeUtil.timeAgo(item, OffsetDateTime.now()));
                }
            }
        });

        loadTasks();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue == tabInbox) loadTasks();
        });
        tfNewTask.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addTask();
            }
        });
    }

    public void loadTasks() {
        try {
            var response = httpService.get("http://localhost:8080/tasks");
            if (response.statusCode() == 200) {
                tbInbox.getItems().setAll(jsonService.parseTaskList(response.body()));
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
            }
            else alertService.error(response.statusCode() + ": " + response.body());
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