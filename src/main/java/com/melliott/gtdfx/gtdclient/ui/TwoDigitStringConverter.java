package com.melliott.gtdfx.gtdclient.ui;

import javafx.util.StringConverter;

public class TwoDigitStringConverter extends StringConverter<Integer> {

    @Override
    public String toString(Integer integer) {
        return String.format("%02d", integer);
    }

    @Override
    public Integer fromString(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
