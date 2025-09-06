package com.example.dicegame.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {

    private DateTimeUtils() {}

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(ZoneOffset.UTC);

    // Returns the current timestamp in ISO 8601 format (UTC)
    public static String getIsoTimestamp() {
        return Instant.now()
                .truncatedTo(ChronoUnit.MILLIS)
                .atOffset(ZoneOffset.UTC)
                .format(ISO_FORMATTER);
    }

    // Converts a given epoch second timestamp to ISO 8601 format (UTC)
    public static String convertToIsoTimestamp(long epochSeconds) {
        return Instant.ofEpochSecond(epochSeconds)
                .truncatedTo(ChronoUnit.MILLIS)
                .atOffset(ZoneOffset.UTC)
                .format(ISO_FORMATTER);
    }

    // Overloaded method to convert an Instant to ISO 8601 format (UTC)
    public static String convertToIsoTimestamp(Instant instant) {
        return instant.truncatedTo(ChronoUnit.MILLIS)
                .atOffset(ZoneOffset.UTC)
                .format(ISO_FORMATTER);
    }
}
