package com.melliott.gtdfx.gtdclient.model;

import com.melliott.gtdfx.gtdclient.model.dto.TaskCreateDTO;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RemoteTaskRepository implements TaskRepository {

    private final HttpService httpService;
    private final JsonService jsonService;
    private final String baseUrl;

    public RemoteTaskRepository(HttpService httpService, JsonService jsonService, String baseUrl) {
        this.httpService = httpService;
        this.jsonService = jsonService;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<Task> getTasks(String status) throws IOException, InterruptedException {
        var response = httpService.get(baseUrl + "/tasks?status=" + status);
        if (response.statusCode() == 200) return jsonService.parseTaskList(response.body());
        else return Collections.emptyList();
    }

    @Override
    public boolean addTask(TaskCreateDTO task) throws IOException, InterruptedException {
        var response = httpService.post(baseUrl + "/tasks", jsonService.createNewTask(task));
        return response.statusCode() == 201;
    }

    @Override
    public boolean updateTask(TaskUpdateDTO task) throws IOException, InterruptedException {
        String patch = jsonService.createUpdateTask(task);
        var response = httpService.patch(baseUrl + "/tasks/" + task.getId(), patch);
        return response.statusCode() == 204;
    }

    @Override
    public boolean deleteTask(int taskId) throws IOException, InterruptedException {
        return httpService.delete(baseUrl + "/tasks/" + taskId).statusCode() == 204;
    }
}
