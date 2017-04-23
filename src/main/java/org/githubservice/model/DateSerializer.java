package org.githubservice.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        LocalDateTime localDateTime = getDateToLocalDateTimeIso8601Formatter(date.toInstant().toString());
        String localizedDateTimeString = getLocalizedDateTime(localDateTime);
        gen.writeString(localizedDateTimeString);
    }

    private LocalDateTime getDateToLocalDateTimeIso8601Formatter(String dateString) {
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(dateString, isoDateTimeFormatter);
    }

    private String getLocalizedDateTime(LocalDateTime localDateTime){
        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return localDateTime.format(localFormatter);
    }
}