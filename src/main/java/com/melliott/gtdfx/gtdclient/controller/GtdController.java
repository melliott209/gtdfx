package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.Constants;
import com.melliott.gtdfx.gtdclient.model.TaskRepository;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.service.StatusService;
import com.melliott.gtdfx.gtdclient.service.ViewLoader;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GtdController {
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
        log.info("Created GTD Controller");
    }

    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue == tabInbox) {
                log.info("Loading Inbox");
                loadTab(inboxContainer, Constants.INBOX_SCREEN);
            } else if (newValue == tabNext) {
                log.info("Loading Next");
                loadTab(nextContainer, Constants.NEXT_SCREEN);
            } else if (newValue == tabWaiting) {
                log.info("Loading Waiting");
                loadTab(waitingContainer, Constants.WAITING_SCREEN);
            } else if (newValue == tabProjects) {
                log.info("Loading Projects");
                loadTab(projectsContainer, Constants.PROJECTS_SCREEN);
            } else if (newValue == tabSomeday) {
                log.info("Loading Someday");
                loadTab(somedayContainer, Constants.SOMEDAY_SCREEN);
            } else if (newValue == tabComplete) {
                log.info("Loading Complete");
                loadTab(completeContainer, Constants.COMPLETE_SCREEN);
            }
        });
        loadTab(inboxContainer, Constants.INBOX_SCREEN);

        lbLeft.setText("Connected to: " + connectMode);
        lbRight.textProperty().bind(statusService.statusProperty());
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), lbRight);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(Interpolator.EASE_IN);
        lbRight.textProperty().addListener((obs, old, newText) -> fadeOut.playFromStart());
    }

    private void loadTab(Pane root, String fxmlPath) {
        try {
            Node screen = viewLoader.load(fxmlPath);
            root.getChildren().setAll(screen);
        } catch (IOException e) {
            alertService.error("Failed to load screen: " + e.getMessage());
            log.error("Error: ", e);
        }
    }
}