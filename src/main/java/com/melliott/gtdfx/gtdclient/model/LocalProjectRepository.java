package com.melliott.gtdfx.gtdclient.model;

import com.melliott.gtdfx.gtdclient.model.dto.ProjectItemCreateDTO;
import com.melliott.gtdfx.gtdclient.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LocalProjectRepository implements ProjectRepository {
    Connection connection;

    public LocalProjectRepository() {
        super();
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
            try (Statement statement = this.connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS projects (id integer primary key, task_id int references tasks(id), project_id integer references tasks(id), \"LEVEL\" integer)");
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<ProjectItem> getAllProjects() {
        String query = """
                SELECT t.description, t.status, p.id, p.task_id, p.project_id, p.level
                FROM projects p
                JOIN tasks t ON t.id = p.task_id
                WHERE p.project_id = p.task_id
                AND (EXISTS (
                    SELECT 1
                    FROM projects subp
                    JOIN tasks subt ON subt.id = subp.task_id
                    WHERE subp.project_id = t.id
                    AND subp.level > 0
                    AND subt.status != 'COMPLETE'
                )
                OR NOT EXISTS (
                    SELECT 1
                    FROM projects subp
                    JOIN tasks subt ON subt.id = subp.task_id
                    WHERE subp.project_id = t.id
                    AND subp.level > 0
                ))""";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<ProjectItem> projects = new ArrayList<>();
                while (rs.next()) {
                    ProjectItem item = new ProjectItem(
                            rs.getInt("id"),
                            rs.getInt("task_id"),
                            rs.getInt("project_id"),
                            rs.getInt("level"),
                            rs.getString("description"),
                            rs.getString("status")
                    );
                    projects.add(item);
                    log.debug(item.toString());
                }
                return projects;
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean addProject(ProjectItemCreateDTO newProject) {
        String query = """
                INSERT INTO projects (task_id, project_id, "level")
                VALUES(?, ?, ?);""";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newProject.getTaskId());
            statement.setInt(2, newProject.getProjectId());
            statement.setInt(3, newProject.getLevel());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<Task> getTasksForProject(int projectId) {
        String query = "SELECT t.description, t.dateCreated, t.dateDue, t.dateCompleted, t.status, t.notes FROM tasks t JOIN projects p ON t.id = p.task_id WHERE p.project_id = ? AND p.level > 0 AND t.status != 'COMPLETE'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Task> tasks = new ArrayList<>();
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setDescription(rs.getString("description"));
                    task.setCreated(TimeUtil.getLocalTimeFromString(rs.getString("dateCreated")));
                    if (rs.getString("status").equals("COMPLETED")) {
                        task.setCompleted(TimeUtil.getLocalTimeFromString(rs.getString("dateCompleted")));
                    }
                    if (rs.getString("dateDue") != null) {
                        task.setDue(TimeUtil.getLocalTimeFromString(rs.getString("dateDue")));
                    }
                    task.setStatus(rs.getString("status"));
                    task.setNotes(rs.getString("notes"));
                    tasks.add(task);
                }
                return tasks;
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
