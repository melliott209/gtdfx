package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Status;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.ui.ContextMenuBuilder;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

@Slf4j
public class WaitingController extends AbstractTaskController {
    @FXML TextArea taNotes;
    @FXML VBox notesBox;
    @FXML TableView<Task> tbTasks;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;

    public WaitingController(AppServices appServices, TaskRepository taskRepository) {
        super(taskRepository, appServices.alertService, appServices.statusService);
    }

    @Override
    public void initialize() {
        tbTasks.itemsProperty().bind(tasks);
        notesBox.managedProperty().bind(notesBox.visibleProperty());
        notesBox.visibleProperty().bind(tbTasks.getSelectionModel().selectedItemProperty().isNotNull());
        tbTasks.setOnMouseClicked(event -> {
            Node selectedNode = event.getPickResult().getIntersectedNode();
            while (selectedNode != null && !(selectedNode instanceof TableCell) && !(selectedNode instanceof TableView)) {
                selectedNode = selectedNode.getParent();
            }
            if (selectedNode instanceof TableView || selectedNode != null && ((TableCell<?, ?>) selectedNode).getTableRow().isEmpty()) {
                tbTasks.getSelectionModel().clearSelection();
            }
        });
        tbTasks.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Task selectedTask = tbTasks.getSelectionModel().getSelectedItem();
                taNotes.setText(selectedTask.getNotes());
            }
        });
        taNotes.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(newValue) && tbTasks.getSelectionModel().getSelectedItem() != null) {
                log.info("Saving notes...");
                tbTasks.getSelectionModel().getSelectedItem().setNotes(taNotes.getText());
                statusService.updateStatus("Notes updated.");
            }
        });
        super.initialize();
    }

    @Override
    protected void setCellFactories() {
        colSinceNext.setCellFactory(col -> new TimeAgoTableCell());
    }

    @Override
    protected void setCellValueFactories() {
        colSinceNext.setCellValueFactory(col -> col.getValue().createdProperty());
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());
    }

    @Override
    protected void setContextMenu() {
        tbTasks.setContextMenu(new ContextMenuBuilder()
                .submenu(new ContextMenuBuilder.MenuBuilder("Move to...")
                        .item("Next", () -> moveToNext(getSelectedTaskId()))
                        .item("Someday", () -> moveToSomeday(getSelectedTaskId()))
                        .build())
                .item("Complete", () -> completeTask(getSelectedTaskId()))
                .item("Delete", () -> deleteTask(getSelectedTaskId()))
                .build());
    }

    private int getSelectedTaskId() {
        return tbTasks.getSelectionModel().getSelectedItem().getId();
    }

    @Override
    public Status getStatus() {
        return Status.WAITING;
    }
}
