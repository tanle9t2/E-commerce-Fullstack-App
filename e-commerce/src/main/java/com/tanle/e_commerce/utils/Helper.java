package com.tanle.e_commerce.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static LocalDateTime convertDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.parse(localDateTime.toString(), formatter);
    }

    public static String convertDatetoString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
