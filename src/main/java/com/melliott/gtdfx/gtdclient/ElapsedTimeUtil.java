package com.melliott.gtdfx.gtdclient;

import java.time.Duration;
import java.time.OffsetDateTime;

public class ElapsedTimeUtil {
    private ElapsedTimeUtil() {
    }
    public static String timeAgo(OffsetDateTime past, OffsetDateTime now) {
        if (past == null || now == null) throw new IllegalArgumentException("Must have two values to calculate the elapsed time");
        if (past.isAfter(now)) throw new IllegalArgumentException("Past time must come before now");

        Duration elapsed = Duration.between(past, now);
        long seconds = elapsed.getSeconds();
        if (seconds < 60) return seconds + " second" + (seconds == 1 ? "" : "s") + " ago";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        long months = hours / 24;
        if (months < 12) return months + " month" + (months == 1 ? "" : "s") + " ago";
        long years = months / 12;
        return years + " year" + (years == 1 ? "" : "s") + " ago";
    }
}
