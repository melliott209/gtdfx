package com.melliott.gtdfx.gtdclient.ui;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.util.TimeUtil;
import javafx.scene.control.TableCell;

import java.time.Duration;

public class ElapsedTimeTableCell extends TableCell<Task, Duration> {

    @Override
    protected void updateItem(Duration item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(TimeUtil.elapsed(item));
        }
    }
}
