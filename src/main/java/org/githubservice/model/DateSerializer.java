package org.githubservice.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
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

        // From ISO 8601
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(date.toInstant().toString(), isoDateTimeFormatter);
        // To localized format
        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String localizedDateTime = localDateTime.format(localFormatter);

        gen.writeString(localizedDateTime);
    }
}