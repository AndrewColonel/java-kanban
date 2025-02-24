// Для выполнения разных операций с данными для одного и того же пути используют разные HTTP-методы.
// для запросов на чтение данных (например, для получения списка задач) используют метод GET,
// для модификации данных (например, для добавления новой задачи или изменения существующей) — POST,
// для удаления данных (например, для удаления задачи) — DELETE.
package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler  extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        service.Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_HISTORY:
                sendText(exchange, endpoint.toString());
                break;
            case POST_HISTORY:
                sendText(exchange, endpoint.toString());
                break;
            case DELETE_HISTORY:
                sendText(exchange, endpoint.toString());
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");

        }
    }

    private service.Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length >= 1 && pathParts[1].equals("history")) {
            switch (requestMethod) {
                case "GET":
                    return Endpoint.GET_HISTORY;
                case "POST":
                    return Endpoint.POST_HISTORY;
                case "DELETE":
                    return Endpoint.DELETE_HISTORY;
                default:
                    return Endpoint.GET_HISTORY;
            }
        }
        return Endpoint.UNKNOWN;
    }

}
