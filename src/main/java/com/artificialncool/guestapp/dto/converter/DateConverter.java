package com.artificialncool.guestapp.dto.converter;

import java.time.LocalDateTime;

public class DateConverter {

    /**
     * Converts a date string of format yyyy-MM-dd to a LocalDate object
     *
     * @param dateString
     * which must be of pattern yyyy-MM-dd
     * @return parsed LocalDate object which corresponds to provided date string
     */
    public static LocalDateTime fromString(String dateString) {
        return LocalDateTime.parse(dateString);
    }

    /**
     * Returns a string parsed from a date, in yyyy-MM-dd format
     *
     * @param date a LocalDate object
     * @return a string in yyyy-MM-dd format
     */
    public static String toString(LocalDateTime date) {
        return date.toString();
    }
}
