package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.service.AlertService;
import com.melliott.gtdfx.gtdclient.service.HttpService;
import com.melliott.gtdfx.gtdclient.service.JsonService;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    @FXML
    TabPane tabPane;
    @FXML
    Tab tabInbox;
    @FXML
    TableColumn<Task, OffsetDateTime> colCreated;
    @FXML
    TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbInbox;

    public GtdController(HttpService httpService, AlertService alertService, JsonService jsonService) {
        this.httpService = httpService;
        this.alertService = alertService;
        this.jsonService = jsonService;
    }

    @FXML
    public void initialize() {
        logger.info("initializing...");

        colCreated.setCellValueFactory(col -> col.getValue().createdProperty());
        colDesc.setCellValueFactory(col -> col.getValue().descriptionProperty());

        loadTasks();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue == tabInbox) loadTasks();
        });
    }

    public void loadTasks() {
        try {
            var response = httpService.get("http://localhost:8080/tasks");
            if (response.statusCode() == 200) {
                tbInbox.getItems().setAll(jsonService.parseTaskList(response.body()));
            }
        } catch (HttpTimeoutException te) {
            alertService.error("The request timed out");
        } catch (IOException e) {
            alertService.error("Something went wrong");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

}