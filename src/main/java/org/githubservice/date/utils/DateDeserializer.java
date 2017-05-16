package org.githubservice.date.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@JsonComponent
public class DateDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        String dateTime = parser.getText();
        DateTimeFormatter localFormatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        localFormatter = localFormatter.withLocale(LocaleContextHolder.getLocale());
        return LocalDateTime.parse(dateTime, localFormatter);
    }

}
