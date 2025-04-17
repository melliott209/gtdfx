package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ElapsedTimeTableCell;
import com.melliott.gtdfx.gtdclient.ui.SimpleDateTableCell;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

public class CompleteController {
    private static final Logger logger = Logger.getLogger(CompleteController.class.getName());
    private final TaskRepository taskRepository;
    private final AlertService alertService;
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colCreated;
    @FXML TableColumn<Task, OffsetDateTime> colFinished;
    @FXML TableColumn<Task, Duration> colElapsed;
    @FXML TableColumn<Task, String> colDescNext;
    @FXML Button btnUndo;

    ListProperty<Task> tasks = new SimpleListProperty<>(FXCollections.observableArrayList());

    public CompleteController(AppServices appServices, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.alertService = appServices.alertService;
    }

    public void initialize() {
        logger.info("initializing...");
        colCreated.setCellValueFactory(col -> col.getValue().createdProperty());
        colFinished.setCellValueFactory(col -> col.getValue().completedProperty());
        colElapsed.setCellValueFactory(col -> new SimpleObjectProperty<>(Duration.between(col.getValue().getCreated(), col.getValue().getCompleted())));
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());

        colCreated.setCellFactory(col -> new SimpleDateTableCell());
        colFinished.setCellFactory(col -> new SimpleDateTableCell());
        colElapsed.setCellFactory(col -> new ElapsedTimeTableCell());

        tbTasks.itemsProperty().bind(tasks);

        loadCompleteTasks();
    }

    public void loadCompleteTasks() {
        try {
            tasks.setAll(taskRepository.getTasks("COMPLETE"));
        } catch (HttpTimeoutException te) {
            alertService.error("The request timed out");
        } catch (IOException e) {
            alertService.error("Error while loading tasks: " + e.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void undoTask() {
        logger.info("completing task...");
        try {
            TaskUpdateDTO task = new TaskUpdateDTO(tbTasks.getSelectionModel().getSelectedItem().getId());
            task.setStatus("NEXT");
            taskRepository.updateTask(task);
            loadCompleteTasks();
        } catch (IOException ioe) {
            alertService.error("Error while completing task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
