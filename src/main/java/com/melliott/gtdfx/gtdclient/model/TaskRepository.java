package com.melliott.gtdfx.gtdclient.model;

import com.melliott.gtdfx.gtdclient.model.dto.TaskCreateDTO;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;

import java.io.IOException;
import java.util.List;

public interface TaskRepository {
    List<Task> getTasks(String status) throws IOException, InterruptedException;

    boolean addTask(TaskCreateDTO task) throws IOException, InterruptedException;

    boolean updateTask(TaskUpdateDTO task) throws IOException, InterruptedException;

    boolean deleteTask(int taskId) throws IOException, InterruptedException;
}
