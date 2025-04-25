package com.melliott.gtdfx.gtdclient.ui;

import com.melliott.gtdfx.gtdclient.model.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TaskDescriptionTableCell extends TableCell<Task, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            Task task = getTableView().getItems().get(getIndex());

            // Check if task is due soon
            if (task.getDue() != null || task.getScheduled() != null) {
                HBox box = new HBox();
                Label description = new Label(item);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Label scheduled = new Label();
                Label due = new Label();
                if (task.getDue() != null) {
                    due.setText(String.format("due in %d days", Duration.between(OffsetDateTime.now(), task.getDue()).toDays()));
                    due.setTextFill(Color.RED);
                }
                if (task.getScheduled() != null) {
                    String time = task.getScheduled().format(DateTimeFormatter.ofPattern("MM/dd"));
                    if (task.getScheduled().toLocalDate().atStartOfDay().isEqual(LocalDate.now().atStartOfDay())) {
                        time = "today";
                    } else if (Duration.between(OffsetDateTime.now(), task.getScheduled()).toDays() < 2) {
                        time = "tomorrow";
                    }
                    scheduled.setText("scheduled " + time);
                    scheduled.setTextFill(Color.GREEN);
                }
                scheduled.setStyle("-fx-min-width: 10em");
                due.setStyle("-fx-min-width: 10em; -fx-text-alignment: right");
                box.getChildren().addAll(description, spacer, scheduled, due);
                setText(null);
                setGraphic(box);
            } else {
                setText(item);
            }
        }
    }
}
