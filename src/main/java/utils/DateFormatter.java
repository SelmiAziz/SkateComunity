package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(customFormatter);
    }
}
