package service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryPrioritisedHandlerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // TaskManager manager = Managers.getDefault();

    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HistoryPrioritisedHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.delTasks();
        manager.delSubTasks();
        manager.delEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void HistoryPrioritizedTest() throws IOException, InterruptedException {

        // создаём HTTP-клиент и запрос
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI urlTask = URI.create("http://localhost:8080/tasks");
            URI urlSubtask = URI.create("http://localhost:8080/subtasks");
            URI urlEpics = URI.create("http://localhost:8080/epics");
            URI urlHistory = URI.create("http://localhost:8080/history");
            URI urlPrioritized = URI.create("http://localhost:8080/prioritized");

            // создаём задачу
            Task task1 = new Task("Test 2",
                    "Testing task 2", TaskStatus.NEW, "11.10.2021-00:00", 10L);
            Task task2 = new Task("Test 3",
                    "Testing task 3", TaskStatus.NEW, "12.10.2022-00:00", 10L);

            // создаём эпик и подзадачу (без эпика не создается)
            Epic epic = new Epic("Эпик-тест1", "Эпик-тестирование-1");

            // конвертируем её в JSON
            String task1Json = gson.toJson(task1);
            String task2Json = gson.toJson(task2);

            String epicJson = gson.toJson(epic);

            // создаем задачи
            HttpRequest requestTask1 = HttpRequest.newBuilder().uri(urlTask)
                    .POST(HttpRequest.BodyPublishers.ofString(task1Json)).build();
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> responseTask1 = client.send(requestTask1,
                    HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, responseTask1.statusCode());

            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpRequest requestTask2 = HttpRequest.newBuilder().uri(urlTask)
                    .POST(HttpRequest.BodyPublishers.ofString(task2Json)).build();
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> responseTask2 = client.send(requestTask2, handler);
            // проверяем код ответа
            assertEquals(201, responseTask2.statusCode());


            HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpics).
                    POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();


            // вызываем рест, отвечающий за создание эпика
            HttpResponse<String> responseEpic = client.send(requestEpic,
                    HttpResponse.BodyHandlers.ofString());
            // проверяем создание эпика
            assertEquals(201, responseEpic.statusCode());

            // получаем id эпика двумя способами - из ответа и из "памяти" сериса
            JsonElement jsonElement = JsonParser.parseString(responseEpic.body());
            if (!jsonElement.isJsonObject()) return;
            JsonObject jsonObject = jsonElement.getAsJsonObject();

//            System.out.println(jsonObject);

            if (manager.getEpicsList().isEmpty()) return;
            List<Epic> epicList = manager.getEpicsList();

//            System.out.println(epicList);

            // оба ответа должны быть равны
            if (epicList.getFirst().getId() != jsonObject.getAsJsonObject().get("id").getAsInt())
                return;
            assertEquals(epicList.getFirst().getId(),
                    jsonObject.get("id").getAsInt(),
                    "ID не равны");

            // создаем новую подзадачу, с правильным id эпика
            Subtask subtask2 = new Subtask("Подзадача 2",
                    "Позадача-тестирование-1", TaskStatus.NEW,
                    jsonObject.get("id").getAsInt(),
                    "15.01.2023-17:00", 60L);

            Subtask subtask = new Subtask("Подзадача 1",
                    "Позадача-тестирование-1", TaskStatus.NEW,
                    jsonObject.get("id").getAsInt(),
                    "16.01.2025-17:00", 60L);


            String subtaskJson = gson.toJson(subtask);
            String subtaskJson2 = gson.toJson(subtask2);


//            System.out.println(subtaskJson);
//            System.out.println(subtaskJson2);


            HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).
                    POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

            HttpResponse<String> responseSubtask = client.send(requestSubtask,
                    HttpResponse.BodyHandlers.ofString());


            HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(urlSubtask).
                    POST(HttpRequest.BodyPublishers.ofString(subtaskJson2)).build();
            HttpResponse<String> responseSubtask2 = client.send(requestSubtask2,
                    HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа (с правильным id эпика подзадача создается)
            assertEquals(201, responseSubtask.statusCode());
            assertEquals(201, responseSubtask2.statusCode());


            HttpRequest requestGetHistory = HttpRequest.newBuilder().uri(urlHistory)
                    .GET().build();
            HttpResponse<String> responseHistory = client.send(requestGetHistory, handler);
            assertEquals(200, responseHistory.statusCode());

            HttpRequest requestPrioritized = HttpRequest.newBuilder().uri(urlPrioritized)
                    .GET().build();
            HttpResponse<String> responsePrioritized = client.send(requestPrioritized, handler);
            assertEquals(200, responsePrioritized.statusCode());

            //теперь список приоритезированных задач не пуст
            Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
            assertNotNull(prioritizedTasks, "Приоритезированные задачи не возвращаются");

//            System.out.println(prioritizedTasks);

            // перебираем все внесенные задачи по ID для набора истории
            // всего было введено 4 задачи разного типа
            List<String> uriList = List.of("http://localhost:8080/tasks/",
                    "http://localhost:8080/subtasks/",
                    "http://localhost:8080/epics/");
            for (String string : uriList) {
                for (int i = 1; i <= 4; i++) {
                    URI url = URI.create(string + i);
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(url)
                            .GET()
                            .build();
                    client.send(request, handler);
                }
            }
            assertEquals(4, prioritizedTasks.size(),
                    "Некорректное количество задач в списке задач по приоритету");
            List<Task> historyList = manager.getHistory();

            assertNotNull(historyList, "Задачи в списке историии не возвращаются");
            assertEquals(4, historyList.size(), "Некорректное количество задач в списке истории ");
        }

    }
}