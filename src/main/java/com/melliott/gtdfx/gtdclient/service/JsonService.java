package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.model.NewTaskDTO;
import com.melliott.gtdfx.gtdclient.model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonService {
    public List<Task> parseTaskList(String response) {
        List<Task> taskList = new ArrayList<>();

        new JSONArray(response).forEach(task -> {
            JSONObject taskObj = (JSONObject) task;
            int id = taskObj.getInt("id");
            String dateCreatedString = taskObj.optString("dateRecorded", null);
            OffsetDateTime dateCreated = dateCreatedString == null ? null : OffsetDateTime.parse(dateCreatedString);
            String dateDueString = taskObj.optString("dateDue", null);
            OffsetDateTime dateDue = dateDueString == null ? null : OffsetDateTime.parse(dateDueString);
            String dateCompletedString = taskObj.optString("dateCompleted", null);
            OffsetDateTime dateCompleted = dateCompletedString == null ? null : OffsetDateTime.parse(dateCompletedString);
            String description = taskObj.getString("description");
            String status = taskObj.getString("status");
            int userId = taskObj.getInt("userId");
            taskList.add(new Task(id, dateCreated, dateDue, dateCompleted, description, status, userId));
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
