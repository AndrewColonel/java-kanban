// Это будет основной класс вашего приложения, будет слушать порт 8080 и принимать запросы
// При запуске программы будет стартовать экземпляр HttpServer.
package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/task", new TaskHandler());
        httpServer.createContext("/subtask", new SubTaskHandler());
        httpServer.createContext("/epic", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
