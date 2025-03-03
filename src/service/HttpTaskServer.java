// Это будет основной класс вашего приложения, будет слушать порт 8080 и принимать запросы
// При запуске программы будет стартовать экземпляр HttpServer.
package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.*;
import service.handlerbase.DurationTypeAdapter;
import service.handlerbase.LocalTimeTypeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

    // статическое поле, контсруткорр и статический метод - все для возможности запуска сервера
    // и контекстного тестирования из других классов
    protected static TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        HttpTaskServer.manager = manager;
    }

    public static TaskManager getHttpServerManager() {
        return manager;
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }

    public void start() {
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/subtasks", new SubTaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() { // метод для тестов
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        // TaskManager manager = new InMemoryTaskManager();
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }
}
