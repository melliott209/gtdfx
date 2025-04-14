package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.SpringLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
@Scope("prototype")
public class GtdController {
    private final Logger logger = Logger.getLogger(GtdController.class.getName());
    private final AlertService alertService;
    private final SpringLoader springLoader;

    @FXML AnchorPane inboxContainer;
    @FXML AnchorPane nextContainer;
    @FXML TabPane tabPane;
    @FXML Tab tabInbox;
    @FXML Tab tabNext;

    public GtdController(AlertService alertService, SpringLoader springLoader) {
        this.alertService = alertService;
        this.springLoader = springLoader;
    }

    public void initialize() {
        logger.info("initializing...");
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue == tabInbox) {
                loadTab(inboxContainer, Constants.INBOX_SCREEN);
            } else if (newValue == tabNext) {
                loadTab(nextContainer, Constants.NEXT_SCREEN);
            }
        });
        loadTab(inboxContainer, Constants.INBOX_SCREEN);
    }

    private void loadTab(Pane root, String fxmlPath) {
        try {
            Node screen = springLoader.loadScreen(fxmlPath);
            root.getChildren().setAll(screen);
        } catch (IOException e) {
            alertService.error("Failed to load screen: " + e.getMessage());
        }
    }
}