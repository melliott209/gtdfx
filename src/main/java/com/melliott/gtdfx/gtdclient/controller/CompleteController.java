package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ContextMenuBuilder;
import com.melliott.gtdfx.gtdclient.ui.ElapsedTimeTableCell;
import com.melliott.gtdfx.gtdclient.ui.SimpleDateTableCell;
import com.melliott.gtdfx.gtdclient.util.UIUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;

public class CompleteController extends AbstractTaskController {
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colCreated;
    @FXML TableColumn<Task, OffsetDateTime> colFinished;
    @FXML TableColumn<Task, Duration> colElapsed;
    @FXML TableColumn<Task, String> colDescNext;
    @FXML Button btnUndo;

    public CompleteController(AppServices appServices, TaskRepository taskRepository) {
        super(taskRepository, appServices.alertService, appServices.statusService);
    }

    @Override
    public void initialize() {
        tbTasks.itemsProperty().bind(tasks);
        UIUtil.clearSelectionOnEmptyRowClick(tbTasks);
        super.initialize();
    }

    @Override
    protected void setContextMenu() {
        tbTasks.setContextMenu(new ContextMenuBuilder()
                .item("Uncomplete", () -> moveToNext(getSelectedTaskId()))
                .item("Delete", () -> deleteTask(getSelectedTaskId()))
                .build());
    }

    private int getSelectedTaskId() {
        return tbTasks.getSelectionModel().getSelectedItem().getId();
    }

    @Override
    protected void setCellFactories() {
        colCreated.setCellFactory(col -> new SimpleDateTableCell());
        colFinished.setCellFactory(col -> new SimpleDateTableCell());
        colElapsed.setCellFactory(col -> new ElapsedTimeTableCell());
    }

    @Override
    protected void setCellValueFactories() {
        colCreated.setCellValueFactory(col -> col.getValue().createdProperty());
        colFinished.setCellValueFactory(col -> col.getValue().completedProperty());
        colElapsed.setCellValueFactory(col -> new SimpleObjectProperty<>(Duration.between(col.getValue().getCreated(), col.getValue().getCompleted())));
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());
    }

    public void undoTask() {
        try {
            TaskUpdateDTO task = new TaskUpdateDTO(getSelectedTaskId());
            task.setStatus("NEXT");
            taskRepository.updateTask(task);
            getTasks();
        } catch (IOException ioe) {
            alertService.error("Error while completing task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Status getStatus() {
        return Status.COMPLETE;
    }
}
