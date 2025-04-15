package com.melliott.gtdfx.gtdclient.util;

import java.time.Duration;
import java.time.OffsetDateTime;

public class ElapsedTimeUtil {
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static final int DAYS_PER_WEEK = 7;

    private ElapsedTimeUtil() {
    }

    public static String timeAgo(OffsetDateTime past, OffsetDateTime now) {
        if (past == null || now == null) throw new IllegalArgumentException("Must have two values to calculate the elapsed time");
        if (past.isAfter(now)) throw new IllegalArgumentException("Past time must come before now");

        Duration elapsed = Duration.between(past, now);
        long seconds = elapsed.getSeconds();
        long minutes = elapsed.toMinutes();
        long hours = elapsed.toHours();
        long days = elapsed.toDays();
        long weeks = elapsed.toDays() / 7;

        if (seconds < SECONDS_PER_MINUTE) return seconds + " s" + " ago";
        else if (minutes < MINUTES_PER_HOUR) return minutes + " m" + " ago";
        else if (hours < HOURS_PER_DAY) return hours + " h" + " ago";
        else if (days < DAYS_PER_WEEK) return days + " d" + " ago";
        else return weeks + " w" + " ago";
    }

    public static String elapsed(Duration duration) {
        long seconds = duration.getSeconds();
        long minutes = seconds / SECONDS_PER_MINUTE;
        long hours = minutes / MINUTES_PER_HOUR;
        long days = hours / HOURS_PER_DAY;
        long weeks = days / DAYS_PER_WEEK;

        if (seconds < SECONDS_PER_MINUTE) return seconds + " s";
        else if (minutes < MINUTES_PER_HOUR) return minutes + " m";
        else if (hours < HOURS_PER_DAY) return hours + " h";
        else if (days < DAYS_PER_WEEK) return days + " d";
        else return weeks + " w";
    }
}
