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
import model.*;
import service.handlerbase.BaseHttpHandler;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_SUBTASK:
                handleGetSubTasksList(exchange);
                break;
            case GET_SUBTASK_BYID:
                handleGetSubTasksById(exchange);
                break;
            case POST_SUBTASK:
                handlePostSubTask(exchange);
                break;
            case DELETE_SUBTASK_BYID:
                handleDeleteSubTaskById(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_SUBTASK;
                case "POST" -> Endpoint.POST_SUBTASK;
                default -> Endpoint.GET_SUBTASK;
            };
        } else if ((pathParts.length == 3 && pathParts[1].equals("subtasks"))) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_SUBTASK_BYID;
                case "DELETE" -> Endpoint.DELETE_SUBTASK_BYID;
                default -> Endpoint.GET_SUBTASK_BYID;
            };
        }
        return Endpoint.UNKNOWN;
    }

    // обработка запросов GET, возвращает список задач в JSON (коды статуса 200 и 500)
    private void handleGetSubTasksById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                String taskToJson = gson.toJson(manager.getSubTaskByID(mayBeTaskId.get()));
                sendText(exchange, taskToJson);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                     | ManagerSaveException | ManagerLoadException e) {
                sendRequestError(exchange);
            }
        }
    }

    // обработка запросов POST, обновляет или создает задачу из JSON (коды статуса 200, 404, 406 и 500)
    private void handleGetSubTasksList(HttpExchange exchange) throws IOException {
        try {
            String jsonTasksList = gson.toJson(manager.getSubTasksList());
            sendText(exchange, jsonTasksList);
        } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                 | ManagerSaveException | ManagerLoadException e) {
            sendRequestError(exchange);
        }
    }

    private void handlePostSubTask(HttpExchange exchange) throws IOException {
        String requestToPost = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        try {
            Subtask subTask = gson.fromJson(requestToPost, Subtask.class);
            // пересекающиеся задачи могут быть перезаписаны, если они обновляются, т.е.равны их ID )))
            if (subTask.getId() != 0) {
                manager.update(subTask); //ID не 0, обновляем
                sendPostOk(exchange, "POST request complete");
            } else {
                manager.add(subTask);
                String jsonRespId = "{\"id\": " + subTask.getId() + "}";
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
    private void handleDeleteSubTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                manager.delSubTasksByID(mayBeTaskId.get());
                sendRequestOk(exchange);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (ManagerSaveException | ManagerLoadException e) {
                sendRequestError(exchange);
            }
        }
    }
}