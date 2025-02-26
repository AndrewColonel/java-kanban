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
import model.Epic;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_EPIC:
                handleGetEpicsList(exchange);
                break;
            case GET_EPIC_BYID:
                handleGetEpicsById(exchange);
                break;
            case GET_EPIC_SUBTASKS:
                handleEpicSubtasks(exchange);
                break;
            case POST_EPIC:
                handlePostEpics(exchange);
                break;
            case DELETE_EPIC_BYID:
                handleDeleteEpicsById(exchange);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals("epics")) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_EPIC;
                case "POST" -> Endpoint.POST_EPIC;
                default -> Endpoint.GET_EPIC;
            };
        } else if ((pathParts.length == 3 && pathParts[1].equals("epics"))) {
            return switch (requestMethod) {
                case "GET" -> Endpoint.GET_EPIC_BYID;
                case "DELETE" -> Endpoint.DELETE_EPIC_BYID;
                default -> Endpoint.GET_EPIC_BYID;
            };
        } else if ((pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks"))) {
            if (requestMethod.equals("GET")) return Endpoint.GET_EPIC_SUBTASKS;
        }
        return Endpoint.UNKNOWN;
    }

    // обработка запросов GET, возвращает задачу по ее ID в JSON (коды статуса 200, 404 и 500)
    private void handleGetEpicsById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                String taskToJson = gson.toJson(manager.getEpicByID(mayBeTaskId.get()));
                sendText(exchange, taskToJson);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (NullPointerException | DateTimeParseException | ManagerSaveException | ManagerLoadException e) {
                sendRequestError(exchange);
            }
        }
    }

    // обработка запросов GET, возвращает список задач в JSON (коды статуса 200 и 500)
    private void handleGetEpicsList(HttpExchange exchange) throws IOException {
        try {
            String jsonTasksList = gson.toJson(manager.getEpicsList());
            sendText(exchange, jsonTasksList);
        } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                 | ManagerSaveException | ManagerLoadException e) {
            sendRequestError(exchange);
        }
    }

    // обработка запросов POST, обновляет или создает задачу из JSON (коды статуса 200, 404, 406 и 500)
    private void handlePostEpics(HttpExchange exchange) throws IOException {
        String requestToPost = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

        try {
            Epic epic = gson.fromJson(requestToPost, Epic.class);
            // пересекающиеся задачи могут быть перезаписаны, если они обновляются, т.е.равны их ID )))
            if (epic.getId() != 0) manager.update(epic);
            else manager.add(epic);
            sendPostOk(exchange);

//        }catch (RuntimeException e){
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }



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
    private void handleDeleteEpicsById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                manager.delEpicByID(mayBeTaskId.get());
                sendRequestOk(exchange);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (ManagerSaveException | ManagerLoadException e) {
                sendRequestError(exchange);
            }
        }
    }

    // обработка запросов GET, возвращает список подзадач по ID эпика (коды статуса 200, 404 и 500)
    private void handleEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange);
        } else {
            try {
                String subtaskListToJson = gson.toJson(manager.getSubTasksListByEpic(mayBeTaskId.get()));
                sendText(exchange, subtaskListToJson);
            } catch (ManagerNotFoundException e) {
                sendNotFound(exchange);
            } catch (JsonSyntaxException | NullPointerException | DateTimeParseException
                     | ManagerSaveException | ManagerLoadException e) {


                sendRequestError(exchange);
            }
        }

    }

}