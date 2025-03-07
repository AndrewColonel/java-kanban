// Для выполнения разных операций с данными для одного и того же пути используют разные HTTP-методы.
// для запросов на чтение данных (например, для получения списка задач) используют метод GET,
// для модификации данных (например, для добавления новой задачи или изменения существующей) — POST,
// для удаления данных (например, для удаления задачи) — DELETE.
package service;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerLoadException;
import exceptions.ManagerNotAcceptableException;
import exceptions.ManagerNotFoundException;
import exceptions.ManagerSaveException;
import model.Task;
import service.handlerbase.BaseHttpHandler;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASK:
                handleGetTasksList(exchange);
                break;
            case GET_TASK_BYID:
                handleGetTasksById(exchange);
                break;
            case POST_TASK:
                handlePostTask(exchange);
                break;
            case DELETE_TASK_BYID:
                handleDeleteTaskById(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_TASK;
                case "POST" -> Endpoint.POST_TASK;
                default -> Endpoint.GET_TASK;
            };
        } else if ((pathParts.length == 3 && pathParts[1].equals("tasks"))) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_TASK_BYID;
                case "DELETE" -> Endpoint.DELETE_TASK_BYID;
                default -> Endpoint.GET_TASK_BYID;
            };
        }
        return Endpoint.UNKNOWN;
    }

    // обработка запросов GET, возвращает задачу по ее ID в JSON (коды статуса 200, 404 и 500)
    private void handleGetTasksById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                String taskToJson = gson.toJson(manager.getTaskByID(mayBeTaskId.get()));
                sendText(exchange, taskToJson);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                     | ManagerSaveException | ManagerLoadException e) {
                sendRequestError(exchange);
            }
        }
    }

    // обработка запросов GET, возвращает список задач в JSON (коды статуса 200 и 500)
    private void handleGetTasksList(HttpExchange exchange) throws IOException {
        try {
            String jsonTasksList = gson.toJson(manager.getTasksList());
            sendText(exchange, jsonTasksList);
        } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                 | ManagerSaveException | ManagerLoadException e) {
            sendRequestError(exchange);
        }
    }

    // обработка запросов POST, обновляет или создает задачу из JSON (коды статуса 200, 404, 406 и 500)
    private void handlePostTask(HttpExchange exchange) throws IOException {
        String requestToPost = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            Task task = gson.fromJson(requestToPost, Task.class);
            // пересекающиеся задачи могут быть перезаписаны, если они обновляются, т.е.равны их ID
            if (task.getId() != 0) {
                manager.update(task); //ID не 0, обновляем
                sendPostOk(exchange, "POST request complete");
            } else {
                manager.add(task);
                String jsonRespId = "{\"id\": " + task.getId() + "}";
                sendPostOk(exchange, jsonRespId);
            }
        } catch (ManagerNotAcceptableException e) {
            sendHasInteractions(exchange);
        } catch (ManagerNotFoundException e) {
            sendNotFound(exchange);
        } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                 | ManagerSaveException | ManagerLoadException e) {
            sendRequestError(exchange);
        }
    }

    // обработка запросов DELETE, удаляет задачу по ID (коды статуса 200, 404 и 500)
    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                manager.delTaskByID(mayBeTaskId.get());
                sendRequestOk(exchange);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (ManagerSaveException | ManagerLoadException e) {
                sendRequestError(exchange);
            }
        }
    }
}