// Это общий для всех HTTP-обработчиков класс, для каждого из пяти базовых путей будет свой  свой обработчик.
// Этот класс будет содержать общие методы для чтения и отправки данных:
// sendText — для отправки общего ответа в случае успеха;
// sendNotFound — для отправки ответа в случае, если объект не был найден;
// sendHasInteractions — для отправки ответа, если при создании или обновлении задача пересекается с уже существующими.
package service;

import com.google.gson.Gson;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;

import manager.*;
import model.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class BaseHttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // final ststic - все handlers наследуют одиг=н и тот же экземпляр менеджера
    // protected final static TaskManager manager = Managers.getDefault();
    protected final static TaskManager manager = HttpTaskServer.getHttpServerManager();

    protected Gson gson = HttpTaskServer.getGson();

    // метод для отправки кода состояния и сообщения клиенту в виде JSOn строки
    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    // метод для отправки кода состояния
    protected void sendRequestOk(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write("Request complete".getBytes(DEFAULT_CHARSET));
        exchange.close();
    }

    // метод для отправки кода состояния
    protected void sendPostOk(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0);
        exchange.getResponseBody().write("POST request complete".getBytes(DEFAULT_CHARSET));
        exchange.close();
    }

    // метод для отправки кода состояния
    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write("Not Found".getBytes(DEFAULT_CHARSET));
        exchange.close();
    }

    // метод для отправки кода состояния
    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write("Not Acceptable".getBytes(DEFAULT_CHARSET));
        exchange.close();
    }

    // метод для отправки кода состояния
    protected void sendRequestError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, 0);
        exchange.getResponseBody().write("Internal Server Error".getBytes(DEFAULT_CHARSET));
        exchange.close();
    }

    // метод получения ID задач из строки запроса
    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}

// класс адаптер для сериализации\десериализации дженериков
class TaskListTypeToken extends TypeToken<List<Task>> {
}

// класс адаптер для сериализации\десериализации полей типа LocalDateTime
class LocalTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
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

// класс адаптер для сериализации\десериализации полей типа Duration
class DurationTypeAdapter extends TypeAdapter<Duration> {
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

