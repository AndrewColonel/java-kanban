package service.handlerbase;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// класс адаптер для сериализации\десериализации полей типа LocalDateTime
public class LocalTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) jsonWriter.nullValue();
        else jsonWriter.value(localDateTime.format(dateTimeFormatter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (JsonToken.NULL.equals(jsonReader.peek())) {
            // если токен NULL
            jsonReader.nextNull();
            return null;
        } else return LocalDateTime.parse(jsonReader.nextString(), dateTimeFormatter);
    }
}