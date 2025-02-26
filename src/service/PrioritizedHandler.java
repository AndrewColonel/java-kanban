// Для выполнения разных операций с данными для одного и того же пути используют разные HTTP-методы.
// для запросов на чтение данных (например, для получения списка задач) используют метод GET,
// для модификации данных (например, для добавления новой задачи или изменения существующей) — POST,
// для удаления данных (например, для удаления задачи) — DELETE.
package service;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint == Endpoint.GET_PRIORITIZED) {
            handleGetHistory(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private service.Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_PRIORITIZED;
            }
        }
        return Endpoint.UNKNOWN;
    }

    // обработка запросов GET, возвращает отсортированный по времени
    // список задач в JSON (коды статуса 200 и 500)
    private void handleGetHistory(HttpExchange exchange) throws IOException {
        try {
            String jsonTasksList = gson.toJson(manager.getPrioritizedTasks());
            sendText(exchange, jsonTasksList);
        } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                 | ManagerSaveException | ManagerLoadException e) {
            sendRequestError(exchange);
        }
    }
}