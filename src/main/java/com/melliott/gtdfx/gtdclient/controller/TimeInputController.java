package com.melliott.gtdfx.gtdclient.controller;

import com.melliott.gtdfx.gtdclient.ui.MeridiemTimeField;
import com.melliott.gtdfx.gtdclient.ui.MinuteTimeField;
import com.melliott.gtdfx.gtdclient.ui.TwelveHourTimeField;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeInputController {
    @FXML public DatePicker date;
    @FXML public TwelveHourTimeField hours;
    @FXML public MinuteTimeField minutes;
    @FXML public MeridiemTimeField meridiem;

    public String getTimeStamp() {
        return String.format("%04d-%02d-%02dT%02d:%02d:00%s",
                date.getValue().getYear(),
                date.getValue().getMonthValue(),
                date.getValue().getDayOfMonth(),
                meridiem.getText().equals("PM") ? (Integer.parseInt(hours.getText()) + 12) % 24 : Integer.parseInt(hours.getText()),
                Integer.parseInt(minutes.getText()),
                ZonedDateTime.now().getOffset());
    }

    public void setFromOffsetDateTime(OffsetDateTime dateScheduled) {
        if (dateScheduled == null) return;
        date.setValue(dateScheduled.toLocalDate());
        hours.setText(dateScheduled.format(DateTimeFormatter.ofPattern("hh")));
        minutes.setText(dateScheduled.format(DateTimeFormatter.ofPattern("mm")));
        meridiem.setText(dateScheduled.format(DateTimeFormatter.ofPattern("a")));
    }
}
