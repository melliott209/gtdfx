package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.AppServices;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;
import com.melliott.gtdfx.gtdclient.ui.TimeAgoTableCell;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

public class WaitingController {
    private static final Logger logger = Logger.getLogger(WaitingController.class.getName());
    private final HttpService httpService;
    private final AlertService alertService;
    private final JsonService jsonService;
    @FXML TableView<Task> tbNext;
    @FXML TableColumn<Task, OffsetDateTime> colSinceNext;
    @FXML TableColumn<Task, String> colDescNext;

    ListProperty<Task> next = new SimpleListProperty<>(FXCollections.observableArrayList());

    public WaitingController(AppServices appServices) {
        this.httpService = appServices.httpService;
        this.alertService = appServices.alertService;
        this.jsonService = appServices.jsonService;
    }

    public void initialize() {
        logger.info("initializing...");
        colSinceNext.setCellFactory(col -> new TimeAgoTableCell());
        colSinceNext.setCellValueFactory(col -> col.getValue().createdProperty());
        colDescNext.setCellValueFactory(col -> col.getValue().descriptionProperty());
        tbNext.itemsProperty().bind(next);

        loadNextTasks();
    }

    public void loadNextTasks() {
        try {
            var response = httpService.get("http://localhost:8080/tasks?status=WAITING");
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
}
