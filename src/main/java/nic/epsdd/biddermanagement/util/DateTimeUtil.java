package nic.epsdd.biddermanagement.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class DateTimeUtil {

    // Method to get the current time in the desired format
    public static LocalDateTime getFormattedDateTime(LocalDateTime localDateTime) {
        // Create a DateTimeFormatter with the required pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");

        // Format the current time to the desired pattern
        return LocalDateTime.parse(localDateTime.format(formatter));
    }
}

