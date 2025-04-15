package com.melliott.gtdfx.gtdclient.ui;

import com.melliott.gtdfx.gtdclient.model.Task;
import javafx.scene.control.TableCell;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleDateTableCell extends TableCell<Task, OffsetDateTime> {

    @Override
    protected void updateItem(OffsetDateTime item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
    }
}
