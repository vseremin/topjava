package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DatesUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DatesUtil() {
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return FORMATTER.format(localDateTime);
    }
}