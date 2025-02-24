// Для выполнения разных операций с данными для одного и того же пути используют разные HTTP-методы.
// для запросов на чтение данных (например, для получения списка задач) используют метод GET,
// для модификации данных (например, для добавления новой задачи или изменения существующей) — POST,
// для удаления данных (например, для удаления задачи) — DELETE.
package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {


//   Gson gson =new GsonBuilder()
//                .serializeNulls()
//                .setPrettyPrinting()
//                .create();

    Gson gson;

    public TaskHandler() {
        this.gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }

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
                sendNotFound(exchange, "Такого эндпоинта не существует");
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

    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private void handleGetTasksById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange, "Такоq задачи не существует");
        } else {
            String taskToJson = gson.toJson(managerFile.getTaskByID(mayBeTaskId.get()));
            sendText(exchange, taskToJson);
        }
    }

    private void handleGetTasksList(HttpExchange exchange) throws IOException {
        String jsonTasksList = gson.toJson(managerFile.getTasksList());
        sendText(exchange, jsonTasksList);
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String requestToPost = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        System.out.println(requestToPost);
        Task task = gson.fromJson(requestToPost, Task.class);
        System.out.println(task);
        if (task.getId() != 0) managerFile.update(task);
        else managerFile.add(task);
        sendPost(exchange);
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> mayBeTaskId = getTaskId(exchange);
        if (mayBeTaskId.isEmpty()) {
            sendNotFound(exchange, "Такоq задачи не существует");
        } else {
            managerFile.delTaskByID(mayBeTaskId.get());
            sendText(exchange, "Задача " + mayBeTaskId.get() + " удалена");
        }
    }

}


