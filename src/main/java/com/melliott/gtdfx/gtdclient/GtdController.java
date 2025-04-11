package com.melliott.gtdfx.gtdclient;

import com.melliott.gtdfx.gtdclient.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class GtdController {
    private final Logger logger = Logger.getLogger(GtdController.class.getName());

    @FXML
    TableColumn<Task, OffsetDateTime> colCreated;
    @FXML
    TableColumn<Task, String> colDesc;
    @FXML TableView<Task> tbInbox;

    @FXML
    public void initialize() {
        logger.info("initializing...");

        colCreated.setCellValueFactory(col -> col.getValue().createdProperty());
        colDesc.setCellValueFactory(col -> col.getValue().descriptionProperty());

        loadTasks();
    }

    public void loadTasks() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tasks"))
                    .timeout(Duration.ofMillis(500))
                    .build();
            HttpResponse<String> response;
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                parseJson(response);
            }
        } catch (HttpTimeoutException te) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("The request timed out");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Something went wrong");
            alert.showAndWait();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void parseJson(HttpResponse<String> response) {
        JSONArray jsonArray = new JSONArray(response.body());
        jsonArray.forEach(json -> {
            JSONObject jsonObject = (JSONObject) json;
            String dateString = jsonObject.getString("dateRecorded");
            OffsetDateTime date = OffsetDateTime.parse(dateString);
            logger.info("Date created: " + date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            String description = jsonObject.getString("description");
            logger.info("Description: " + description);

            Task newTask = new Task(date, description);
            tbInbox.getItems().add(newTask);
        });
    }
}