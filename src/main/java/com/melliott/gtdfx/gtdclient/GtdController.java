package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;
import com.melliott.gtdfx.gtdclient.service.SpringLoader;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

@Component
@Scope("prototype")
public class GtdController {
    private final Logger logger = Logger.getLogger(GtdController.class.getName());
    private final HttpService httpService;
    private final AlertService alertService;
    private final JsonService jsonService;
    private final SpringLoader springLoader;

    @FXML AnchorPane inboxContainer;
    @FXML TabPane tabPane;
    @FXML Tab tabInbox;
    @FXML Tab tabNext;

    @FXML TableView<Task> tbNext;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;

    ListProperty<Task> next = new SimpleListProperty<>(FXCollections.observableArrayList());

    public GtdController(HttpService httpService, AlertService alertService, JsonService jsonService, SpringLoader springLoader) {
        this.httpService = httpService;
        this.alertService = alertService;
        this.jsonService = jsonService;
        this.springLoader = springLoader;
    }

    @FXML
    public void initialize() {
        logger.info("initializing...");

        colSinceNext.setCellFactory(col -> new TimeAgoTableCell());
        colSinceNext.setCellValueFactory(col -> col.getValue().createdProperty());
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());

        loadNextTasks();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue == tabInbox) {
                loadInboxTab();
            } else if (newValue == tabNext) loadNextTasks();
        });
        tbNext.itemsProperty().bind(next);
        loadInboxTab();
    }

    private void loadInboxTab() {
        try {
            Node screen = springLoader.loadScreen("/com/melliott/gtdfx/gtdclient/inbox.fxml");
            inboxContainer.getChildren().setAll(screen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadNextTasks() {
        try {
            var response = httpService.get("http://localhost:8080/tasks?status=NEXT");
            if (response.statusCode() == 200) {
                next.setAll(jsonService.parseTaskList(response.body()));
            }
        } catch (HttpTimeoutException te) {
            alertService.error("The request timed out");
        } catch (IOException e) {
            alertService.error("Error while loading tasks: " + e.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void completeTask() {
        logger.info("completing task...");
    }
}