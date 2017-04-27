package org.githubservice.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@JsonComponent
public class DateSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        DateTimeFormatter localFormatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        localFormatter = localFormatter.withLocale(LocaleContextHolder.getLocale());
        generator.writeString(localDateTime.format(localFormatter));
    }
}
