// Это общий для всех HTTP-обработчиков класс, для каждого из пяти базовых путей будет свой  свой обработчик.
// Этот класс будет содержать общие методы для чтения и отправки данных:
// sendText — для отправки общего ответа в случае успеха;
// sendNotFound — для отправки ответа в случае, если объект не был найден;
// sendHasInteractions — для отправки ответа, если при создании или обновлении задача пересекается с уже существующими.
package service;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BaseHttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected TaskManager manager;
    protected TaskManager managerFile;

    public BaseHttpHandler() {
        this.manager = Managers.getDefault();
        this.managerFile = FileBackedTaskManager.loadFromFile(new File("FileBackedTaskManager.csv"));
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
//        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendPost(HttpExchange exchange) throws IOException {
//        byte[] resp = text.getBytes(DEFAULT_CHARSET);
//        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
//        exchange.sendResponseHeaders(201, resp.length);
        exchange.sendResponseHeaders(201, 0);
//        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange,String text) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(text.getBytes(DEFAULT_CHARSET));
        exchange.close();
    }


    protected void sendHasInteractions() {

    }
}



class TaskListTypeToken extends TypeToken<List<Task>> {
}

class LocalTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.format(dateTimeFormatter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), dateTimeFormatter);
    }
}

class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        jsonWriter.value(duration.toMinutes());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }

}

