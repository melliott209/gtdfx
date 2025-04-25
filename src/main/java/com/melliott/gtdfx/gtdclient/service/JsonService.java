package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.Config;
import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.model.dto.TaskCreateDTO;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import com.melliott.gtdfx.gtdclient.util.TimeUtil;
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
            OffsetDateTime dateCreated = dateCreatedString == null ? null : TimeUtil.getLocalTimeFromString(dateCreatedString);
            String dateDueString = taskObj.optString("dateDue", null);
            OffsetDateTime dateDue = dateDueString == null ? null : TimeUtil.getLocalTimeFromString(dateDueString);
            String dateScheduledString = taskObj.optString("dateScheduled", null);
            OffsetDateTime dateScheduled = dateScheduledString == null ? null : TimeUtil.getLocalTimeFromString(dateScheduledString);
            String dateCompletedString = taskObj.optString("dateCompleted", null);
            OffsetDateTime dateCompleted = dateCompletedString == null ? null : TimeUtil.getLocalTimeFromString(dateCompletedString);
            String description = taskObj.getString("description");
            String status = taskObj.getString("status");
            int userId = taskObj.getInt("userId");
            taskList.add(new Task(id, dateCreated, dateDue, dateScheduled, dateCompleted, description, status, userId));
        });

        return taskList;
    }

    public String createNewTask(TaskCreateDTO task) {
        JSONObject taskObj = new JSONObject();
        taskObj.put("description", task.getDescription());
        taskObj.put("dateRecorded", OffsetDateTime.now());
        taskObj.put("userId", task.getUserId());
        return taskObj.toString();
    }

    public String createUpdateTask(TaskUpdateDTO task) {
        JSONObject taskObj = new JSONObject();
        if (task.getDescription() != null) taskObj.put("description", task.getDescription());
        if (task.getDateCreated() != null) taskObj.put("dateRecorded", task.getDateCreated());
        if (task.getDateDue() != null) taskObj.put("dateDue", task.getDateDue());
        if (task.getDateCompleted() != null) taskObj.put("dateCompleted", task.getDateCompleted());
        if (task.getStatus() != null) taskObj.put("status", task.getStatus());
        return taskObj.toString();
    }

    public Config parseConfig(String fileContents) {
        Config defaults = Config.DEFAULT;
        JSONObject userConfig = new JSONObject(fileContents);
        boolean remote = (userConfig.has("remote") ? userConfig.getBoolean("remote") : defaults.remote());
        String serverIP = (userConfig.has("serverIP") ? userConfig.getString("serverIP") : defaults.serverIP());
        String serverPort = (userConfig.has("serverPort") ? userConfig.getString("serverPort") : defaults.serverPort());
        return new Config(remote, serverIP, serverPort);
    }
}
