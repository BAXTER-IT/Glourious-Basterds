package com.eurhufapp.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for date and time operations.
 */
public class DateTimeUtils {
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Converts a LocalDateTime to a Calendar.
     * 
     * @param dateTime The LocalDateTime to convert
     * @return The Calendar
     */
    public static Calendar toCalendar(LocalDateTime dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(toMillis(dateTime));
        return calendar;
    }
    
    /**
     * Converts a Calendar to a LocalDateTime.
     * 
     * @param calendar The Calendar to convert
     * @return The LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        return Instant.ofEpochMilli(calendar.getTimeInMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    /**
     * Converts a Date to a LocalDateTime.
     * 
     * @param date The Date to convert
     * @return The LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    /**
     * Converts a LocalDateTime to milliseconds since epoch.
     * 
     * @param dateTime The LocalDateTime to convert
     * @return The milliseconds since epoch
     */
    public static long toMillis(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * Parses a string to a LocalDateTime using the ISO formatter.
     * 
     * @param dateTimeStr The string to parse
     * @return The LocalDateTime
     * @throws DateTimeParseException if the string cannot be parsed
     */
    public static LocalDateTime parseIsoDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, ISO_FORMATTER);
    }
    
    /**
     * Formats a LocalDateTime to a string using the ISO formatter.
     * 
     * @param dateTime The LocalDateTime to format
     * @return The formatted string
     */
    public static String formatIsoDateTime(LocalDateTime dateTime) {
        return ISO_FORMATTER.format(dateTime);
    }
}