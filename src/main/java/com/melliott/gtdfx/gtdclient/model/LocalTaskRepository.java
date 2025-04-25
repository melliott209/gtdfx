package com.melliott.gtdfx.gtdclient.model;

import com.melliott.gtdfx.gtdclient.model.dto.TaskCreateDTO;
import com.melliott.gtdfx.gtdclient.model.dto.TaskUpdateDTO;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LocalTaskRepository implements TaskRepository {

    Connection connection;

    public LocalTaskRepository() {
        super();
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
            try (Statement statement = this.connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS tasks (id integer primary key, description text, dateCreated datetime, dateDue datetime, dateScheduled datetime, dateCompleted datetime, status text, notes text)");
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getTasks(String status) {
        String query = "SELECT * FROM tasks WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                List<Task> tasks = new ArrayList<>();
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setDescription(rs.getString("description"));
                    task.setCreated(OffsetDateTime.parse(rs.getString("dateCreated")));
                    if (rs.getString("dateDue") != null) {
                        task.setDue(OffsetDateTime.parse(rs.getString("dateDue")));
                    }
                    if (rs.getString("dateScheduled") != null) {
                        task.setScheduled(OffsetDateTime.parse(rs.getString("dateScheduled")));
                    }
                    if (rs.getString("dateCompleted") != null) {
                        task.setCompleted(OffsetDateTime.parse(rs.getString("dateCompleted")));
                    }
                    task.setStatus(rs.getString("status"));
                    task.setNotes(rs.getString("notes"));
                    task.setUserId(0);
                    tasks.add(task);
                }
                return tasks;
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean addTask(TaskCreateDTO task) {
        String query = """
                INSERT INTO tasks (description, dateCreated, dateDue, dateScheduled, status)
                VALUES(?, ?, ?, ?, ?);""";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, task.getDescription());
            statement.setString(2, OffsetDateTime.now().toString());
            statement.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);
            statement.setString(4, task.getScheduledDate() != null ? task.getScheduledDate().toString() : null);
            statement.setString(5, "INBOX");
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean updateTask(TaskUpdateDTO task) {
        List<String> columns = new ArrayList<>();
        List<Object> valuesList = new ArrayList<>();
        if (task.getDescription() != null) {
            columns.add("description = ?");
            valuesList.add(task.getDescription());
        }
        if (task.getDateCreated() != null) {
            columns.add("dateCreated = ?");
            valuesList.add(task.getDateCreated());
        }
        if (task.getDateDue() != null) {
            columns.add("dateDue = ?");
            valuesList.add(task.getDateDue());
        }
        if (task.getDateScheduled() != null) {
            columns.add("dateScheduled = ?");
            valuesList.add(task.getDateScheduled());
        }
        if (task.getDateCompleted() != null) {
            columns.add("dateCompleted = ?");
            valuesList.add(task.getDateCompleted());
        }
        if (task.getStatus() != null) {
            columns.add("status = ?");
            valuesList.add(task.getStatus());
        }
        if (task.getNotes() != null) {
            columns.add("notes = ?");
            valuesList.add(task.getNotes());
        }
        String query = MessageFormat.format("UPDATE tasks SET {0} WHERE id = ?", String.join(", ", columns));
        log.info("Executing query: {} with params {} and taskId {}", query, valuesList, task.getId());
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < valuesList.size(); i++) {
                statement.setObject(i + 1, valuesList.get(i));
            }
            statement.setInt(valuesList.size() + 1, task.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteTask(int taskId) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taskId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
