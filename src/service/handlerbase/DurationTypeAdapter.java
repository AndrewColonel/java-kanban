package service.handlerbase;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

// класс адаптер для сериализации\десериализации полей типа Duration
public class DurationTypeAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) jsonWriter.nullValue();
        else jsonWriter.value(duration.toMinutes());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        if (JsonToken.NULL.equals(jsonReader.peek())) {
            // если токен NULL
            jsonReader.nextNull();
            return null;
        } else return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
        // nextString считывает и цифру, и строку
    }
}