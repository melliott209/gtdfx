package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.Constants;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.service.StatusService;
import com.melliott.gtdfx.gtdclient.service.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Logger;

public class GtdController {
    private final Logger logger = Logger.getLogger(GtdController.class.getName());
    private final AlertService alertService;
    private final StatusService statusService;
    private final ViewLoader viewLoader;
    private final String connectMode;

    @FXML AnchorPane inboxContainer;
    @FXML AnchorPane nextContainer;
    @FXML AnchorPane waitingContainer;
    @FXML AnchorPane projectsContainer;
    @FXML AnchorPane somedayContainer;
    @FXML AnchorPane completeContainer;
    @FXML TabPane tabPane;
    @FXML Tab tabInbox;
    @FXML Tab tabNext;
    @FXML Tab tabWaiting;
    @FXML Tab tabProjects;
    @FXML Tab tabSomeday;
    @FXML Tab tabComplete;
    @FXML Label lbLeft;
    @FXML Label lbRight;

    public GtdController(AppServices services, TaskRepository taskRepository) {
        this.alertService = services.alertService;
        this.statusService = services.statusService;
        this.viewLoader = new ViewLoader(services, taskRepository);
        this.connectMode = services.config.remote() ? services.config.serverIP() : "Local";
        logger.info("Created GTD Controller");
    }

    public void initialize() {
        logger.info("initializing...");
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue == tabInbox) {
                loadTab(inboxContainer, Constants.INBOX_SCREEN);
            } else if (newValue == tabNext) {
                loadTab(nextContainer, Constants.NEXT_SCREEN);
            } else if (newValue == tabWaiting) {
                loadTab(waitingContainer, Constants.WAITING_SCREEN);
            } else if (newValue == tabProjects) {
                loadTab(projectsContainer, Constants.PROJECTS_SCREEN);
            } else if (newValue == tabSomeday) {
                loadTab(somedayContainer, Constants.SOMEDAY_SCREEN);
            } else if (newValue == tabComplete) {
                loadTab(completeContainer, Constants.COMPLETE_SCREEN);
            }
        });
        loadTab(inboxContainer, Constants.INBOX_SCREEN);

        lbLeft.setText("Connected to: " + connectMode);
        lbRight.textProperty().bind(statusService.statusProperty());
    }

    private void loadTab(Pane root, String fxmlPath) {
        try {
            Node screen = viewLoader.load(fxmlPath);
            root.getChildren().setAll(screen);
        } catch (IOException e) {
            alertService.error("Failed to load screen: " + e.getMessage());
        }
    }
}