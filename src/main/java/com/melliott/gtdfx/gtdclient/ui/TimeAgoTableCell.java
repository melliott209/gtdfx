package com.melliott.gtdfx.gtdclient.ui;

import com.melliott.gtdfx.gtdclient.model.Task;
import com.melliott.gtdfx.gtdclient.util.TimeUtil;
import javafx.scene.control.TableCell;

import java.time.OffsetDateTime;

public class TimeAgoTableCell extends TableCell<Task, OffsetDateTime> {

    @Override
    protected void updateItem(OffsetDateTime item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(TimeUtil.timeAgo(item, OffsetDateTime.now()));
        }
    }
}
