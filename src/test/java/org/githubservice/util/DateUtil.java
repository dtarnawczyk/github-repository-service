package org.githubservice.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class DateUtil {

    public static LocalDateTime createDate(String dateTime){
        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        localFormatter = localFormatter.withLocale(LocaleContextHolder.getLocale());
        return LocalDateTime.parse(dateTime, localFormatter);
    }
}
