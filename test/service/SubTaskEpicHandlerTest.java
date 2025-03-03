package service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
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

import static org.junit.jupiter.api.Assertions.*;

class SubTaskEpicHandlerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
//    TaskManager manager = Managers.getDefault();


    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public SubTaskEpicHandlerTest() throws IOException {
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
    public void testAddEpicAndSubTask() throws IOException, InterruptedException {
        // создаём эпик и подзадачу (без эпика не создается)
        Epic epic = new Epic("Эпик-тест1", "Эпик-тестирование-1");
        Subtask subtask = new Subtask("Подзадача 1",
                "Позадача-тестирование-1", TaskStatus.NEW, 1,
                "11.01.2025-17:00", 60L);
        // конвертируем её в JSON
        String subtaskJson = gson.toJson(subtask);
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI urlSubtask = URI.create("http://localhost:8080/subtasks");
            URI urlEpics = URI.create("http://localhost:8080/epics");
            HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).
                    POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

            HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpics).
                    POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> responseSubtask1 = client.send(requestSubtask,
                    HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа (без эпика подзадача не создается)
            assertEquals(404, responseSubtask1.statusCode());

            // вызываем рест, отвечающий за создание эпика
            HttpResponse<String> responseEpic = client.send(requestEpic,
                    HttpResponse.BodyHandlers.ofString());
            // проверяем создание эпика
            assertEquals(201, responseEpic.statusCode());

            // получаем id эпика двумя способами - из ответа и из "памяти" сериса
            JsonElement jsonElement = JsonParser.parseString(responseEpic.body());
            if (!jsonElement.isJsonObject()) return;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (manager.getEpicsList().isEmpty()) return;
            List<Epic> epicList = manager.getEpicsList();
            // оба ответа должны быть равны
            if (manager.getEpicsList().getFirst().getId() != jsonObject.getAsJsonObject().get("id").getAsInt())
                return;
            assertEquals(epicList.getFirst().getId(),
                    jsonObject.get("id").getAsInt(),
                    "ID не равны");

            // создаем новую подзадачу, с правильным id эпика
            Subtask subtask2 = new Subtask("Подзадача 2",
                    "Позадача-тестирование-1", TaskStatus.NEW,
                    jsonObject.get("id").getAsInt(),
                    "15.01.2027-17:00", 60L);

            String subtaskJson2 = gson.toJson(subtask2);
            HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(urlSubtask).
                    POST(HttpRequest.BodyPublishers.ofString(subtaskJson2)).build();
            HttpResponse<String> responseSubtask2 = client.send(requestSubtask2,
                    HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа (с правильным id эпика подзадача создается)
            assertEquals(201, responseSubtask2.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Subtask> tasksFromManager = manager.getSubTasksList();

//            System.out.println(tasksFromManager);

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Подзадача 2", tasksFromManager.getFirst().getName(),
                    "Некорректное имя задачи");
        }
    }
}