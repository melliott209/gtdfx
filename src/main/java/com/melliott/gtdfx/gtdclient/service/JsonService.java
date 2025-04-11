package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.model.NewTaskDTO;
import com.melliott.gtdfx.gtdclient.model.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsonService {
    public List<Task> parseTaskList(String response) {
        List<Task> taskList = new ArrayList<>();

        new JSONArray(response).forEach(task -> {
            JSONObject taskObj = (JSONObject) task;
            OffsetDateTime date = OffsetDateTime.parse(taskObj.getString("dateRecorded"));
            String description = taskObj.getString("description");
            taskList.add(new Task(date, description));
        });

        return taskList;
    }

    public String createNewTask(NewTaskDTO task) {
        JSONObject taskObj = new JSONObject();
        taskObj.put("description", task.getDescription());
        taskObj.put("dateRecorded", OffsetDateTime.now());
        taskObj.put("userId", task.getUserId());
        return taskObj.toString();
    }
}
