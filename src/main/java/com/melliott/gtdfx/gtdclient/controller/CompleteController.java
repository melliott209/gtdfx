package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;
import com.melliott.gtdfx.gtdclient.ui.ElapsedTimeTableCell;
import com.melliott.gtdfx.gtdclient.ui.SimpleDateTableCell;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

public class CompleteController {
    private static final Logger logger = Logger.getLogger(CompleteController.class.getName());
    private final HttpService httpService;
    private final AlertService alertService;
    private final JsonService jsonService;
    @FXML TableView<Task> tbNext;
    @FXML TableColumn<Task, OffsetDateTime> colCreated;
    @FXML TableColumn<Task, OffsetDateTime> colFinished;
    @FXML TableColumn<Task, Duration> colElapsed;
    @FXML TableColumn<Task, String> colDescNext;
    @FXML Button btnUndo;

    ListProperty<Task> next = new SimpleListProperty<>(FXCollections.observableArrayList());

    public CompleteController(AppServices appServices) {
        this.httpService = appServices.httpService;
        this.alertService = appServices.alertService;
        this.jsonService = appServices.jsonService;
    }

    public void initialize() {
        logger.info("initializing...");
        colCreated.setCellValueFactory(col -> col.getValue().createdProperty());
        colFinished.setCellValueFactory(col -> col.getValue().completedProperty());
        colElapsed.setCellValueFactory(col -> new SimpleObjectProperty<>(Duration.between(col.getValue().getCreated(), col.getValue().getCompleted())));
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());

        colCreated.setCellFactory(col -> new SimpleDateTableCell());
        colFinished.setCellFactory(col -> new SimpleDateTableCell());
        colElapsed.setCellFactory(col -> new ElapsedTimeTableCell());

        tbNext.itemsProperty().bind(next);

        loadCompleteTasks();
    }

    public void loadCompleteTasks() {
        try {
            var response = httpService.get("http://localhost:8080/tasks?status=COMPLETE");
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

    public void undoTask() {
        logger.info("completing task...");
        try {
            httpService.patch("http://localhost:8080/tasks/" + tbNext.getSelectionModel().getSelectedItem().getId(),
                    "{\"status\":\"NEXT\",\"dateCompleted\":null}");
            loadCompleteTasks();
        } catch (IOException ioe) {
            alertService.error("Error while completing task: " + ioe.getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
