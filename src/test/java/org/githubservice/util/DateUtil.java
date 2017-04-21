package org.githubservice.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static Date createDateFromIsoString(String dateString){

        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, isoDateTimeFormatter);
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
