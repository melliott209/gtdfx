package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.model.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.StatusService;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.text.MessageFormat;

public abstract class AbstractTaskController implements StatusSpecific {
    protected TaskRepository taskRepository;
    protected AlertService alertService;
    protected StatusService statusService;
    protected ListProperty<Task> tasks;

    protected AbstractTaskController(TaskRepository taskRepository, AlertService alertService, StatusService statusService) {
        this.taskRepository = taskRepository;
        this.alertService = alertService;
        this.statusService = statusService;
        tasks = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public void initialize() {
        setContextMenu();
        setCellValueFactories();
        setCellFactories();
        long time = runTimed(this::getTasks);
        statusService.updateStatus(MessageFormat.format("Loaded {0} task(s) in {1}ms", tasks.get().size(), time));
    }

    protected void getTasks() {
        try {
            tasks.setAll(taskRepository.getTasks(getStatus().name()));
        } catch (IOException ioe) {
            alertService.error("Error while getting tasks: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    protected void updateTask(TaskUpdateDTO task) {
        try {
            if (!taskRepository.updateTask(task)) alertService.error("Failed to update task.");
            else statusService.updateStatus("Task updated.");
            getTasks();
        } catch (IOException ioe) {
            alertService.error("Error while updating task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    protected void deleteTask(int taskId) {
        try {
            if (!taskRepository.deleteTask(taskId)) alertService.error("Failed to delete task.");
            else statusService.updateStatus("Task deleted.");
            getTasks();
        } catch (IOException ioe) {
            alertService.error("Error while deleting task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    protected long runTimed(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    protected abstract void setContextMenu();

    protected abstract void setCellValueFactories();

    protected abstract void setCellFactories();
}
