package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ContextMenuBuilder;
import com.melliott.gtdfx.gtdclient.ui.SimpleDateTableCell;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import com.melliott.gtdfx.gtdclient.util.UIUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.OffsetDateTime;

public class NextController extends AbstractTaskController {
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colSince;
    @FXML TableColumn<Task, String> colDesc;
    @FXML TableColumn<Task, OffsetDateTime> colDue;
    @FXML TableColumn<Task, OffsetDateTime> colScheduled;

    public NextController(AppServices appServices, TaskRepository taskRepository) {
        super(taskRepository, appServices.alertService, appServices.statusService);
    }

    @Override
    public void initialize() {
        tbTasks.itemsProperty().bind(tasks);
        UIUtil.clearSelectionOnEmptyRowClick(tbTasks);
        super.initialize();
    }

    @Override
    protected void setCellValueFactories() {
        colSince.setCellValueFactory(col -> col.getValue().createdProperty());
        colDesc.setCellValueFactory(col -> col.getValue().descriptionProperty());
        colDue.setCellValueFactory(col -> col.getValue().dueProperty());
        colScheduled.setCellValueFactory(col -> col.getValue().scheduledProperty());
    }

    @Override
    protected void setCellFactories() {
        colSince.setCellFactory(col -> new TimeAgoTableCell());
        colDue.setCellFactory(col -> new SimpleDateTableCell());
        colScheduled.setCellFactory(col -> new SimpleDateTableCell());
    }

    @Override
    protected void setContextMenu() {
        tbTasks.setContextMenu(new ContextMenuBuilder()
                .submenu(new ContextMenuBuilder.MenuBuilder("Move to...")
                        .item("Waiting", () -> moveToWaiting(getSelectedTask()))
                        .item("Someday", () -> moveToSomeday(getSelectedTask().getId()))
                        .build())
                .item("Complete", () -> completeTask(getSelectedTask().getId()))
                .item("Delete", () -> deleteTask(getSelectedTask().getId()))
                .build());
    }

    private Task getSelectedTask() {
        return tbTasks.getSelectionModel().getSelectedItem();
    }

    @Override
    public Status getStatus() {
        return Status.NEXT;
    }
}
