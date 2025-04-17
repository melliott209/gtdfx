package com.melliott.gtdfx.gtdclient.service;

import com.melliott.gtdfx.gtdclient.controller.*;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class ViewLoader {
    private final AppServices services;
    private final TaskRepository taskRepository;

    public ViewLoader(AppServices appServices, TaskRepository taskRepository) {
        this.services = appServices;
        this.taskRepository = taskRepository;
    }

    public Node load(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        fxmlLoader.setControllerFactory(param -> {
            if (param == InboxController.class) return new InboxController(services, taskRepository);
            else if (param == CompleteController.class) return new CompleteController(services, taskRepository);
            else if (param == NextController.class) return new NextController(services, taskRepository);
            else if (param == GtdController.class) return new GtdController(services, taskRepository);
            else if (param == WaitingController.class) return new WaitingController(services, taskRepository);
            else if (param == SomedayController.class) return new SomedayController(services, taskRepository);
            else if (param == ProjectsController.class) return new ProjectsController();

            throw new IllegalArgumentException("Unknown controller type: " + param);
        });
        return fxmlLoader.load();
    }
}
