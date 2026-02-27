package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.controller.GtdController;
import com.melliott.gtdfx.gtdclient.model.*;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.MessageFormat;

@Slf4j
public class GtdApplication extends Application {
    private AppServices services;
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;

    @Override
    public void init() {
        this.services = new AppServices();

        if (services.config.remote()) {
            String baseUrl = MessageFormat.format("http://{0}:{1}", services.config.serverIP(), services.config.serverPort());
            this.taskRepository = new RemoteTaskRepository(services.httpService, services.jsonService, baseUrl);
        } else {
            this.taskRepository = new LocalTaskRepository();
        }

        this.projectRepository = new LocalProjectRepository();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GtdApplication.class.getResource("gtd.fxml"));
        fxmlLoader.setControllerFactory(param -> new GtdController(services, taskRepository, projectRepository));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("gtdFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}