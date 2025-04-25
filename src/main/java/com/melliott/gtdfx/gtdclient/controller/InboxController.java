package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.dto.TaskCreateDTO;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ContextMenuBuilder;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import com.melliott.gtdfx.gtdclient.util.UIUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.OffsetDateTime;

@Slf4j
public class InboxController extends AbstractTaskController {
    @FXML TableColumn<Task, OffsetDateTime> colSince;
    @FXML TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbTasks;
    @FXML TextField tfNewTask;
    @FXML Button btnAdd;

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
        tbTasks.itemsProperty().bind(tasks);
        UIUtil.clearSelectionOnEmptyRowClick(tbTasks);
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
        tbTasks.setContextMenu(new ContextMenuBuilder()
                .submenu(new ContextMenuBuilder.MenuBuilder("Move to...")
                        .item("Next", () -> moveToNext(getSelectedTask()))
                        .item("Waiting", () -> moveToWaiting(getSelectedTask()))
                        .item("Someday", () -> moveToSomeday(getSelectedTask().getId()))
                        .build())
                .item("Delete", () -> deleteTask(getSelectedTask().getId()))
                .build());
    }

    private Task getSelectedTask() {
        return tbTasks.getSelectionModel().getSelectedItem();
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
