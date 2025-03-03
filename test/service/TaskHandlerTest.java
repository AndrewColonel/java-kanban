package service;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskHandlerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
//     TaskManager manager = Managers.getDefault();

    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public TaskHandlerTest() throws IOException {
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
    public void testAddUpdateTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 2",
                "Testing task 2", TaskStatus.NEW, "11.10.2026-00:00", 10L);
        Task task2 = new Task("Test 3", 1,
                "Testing task 3", TaskStatus.NEW, "12.10.2026-00:00", 10L);
        // конвертируем её в JSON
        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder().uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());

            taskJson = gson.toJson(task2);
            request = HttpRequest.newBuilder().uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());


            // проверяем, что создалась одна задача с корректным именем
            List<Task> tasksFromManager = manager.getTasksList();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test 3", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }
    }

}