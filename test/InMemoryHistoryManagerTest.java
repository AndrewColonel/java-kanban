//убедитесь, что задачи, добавляемые в service.HistoryManager, сохраняют предыдущую версию задачи и её данных.

import java.util.List;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static TaskStatus statusNew;
    static TaskStatus statusInProgress;
    static TaskStatus statusDone;
    static TaskManager manager;
    static HistoryManager historyManager;
    static Task task1;
    static Task task2;
    static Epic epic1;
    static Epic epic2;
    static Subtask subTask1;
    static Subtask subTask2;
    static Subtask subTask3;

    @BeforeEach
    void setUp() {
        statusNew = TaskStatus.NEW;
        statusInProgress = TaskStatus.IN_PROGRESS;
        statusDone = TaskStatus.DONE;
        manager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();

        task1 = new Task("Тест создания Таск 1",
                "описание Таск 1", statusNew);
        task2 = new Task("Тест создания Таск 2", 1,
                "описание Таск 2", statusNew);

        epic1 = new Epic("Тест создания Эпик 1", "Это задача -Эпик 1");
        epic2 = new Epic("Тест создания Эпик 2", 1, "Это задача -Эпик 2");

        subTask1 = new Subtask("Тест создания Подзадачи 1",
                "Это подзадача для Эпика", statusNew, 1);
        subTask2 = new Subtask("Тест создания Подзадачи 2",
                "Это подзадача для Эпика", statusNew, 1);
        subTask3 = new Subtask("Тест создания Подзадачи 3", 0,
                "Это подзадача для Эпика", statusNew, 1);

    }

    @Test
    void addHistoryCheck() {
        //убедитесь, что задачи, добавляемые в service.HistoryManager, сохраняют предыдущую версию задачи и её данных.
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(subTask1);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История  пустая.");
        assertEquals(3, history.size(), "История  пустая.");

        Task taskFromHistoryLast = history.get(2);
        Task taskFromHistoryMiddle = history.get(1);
        Task taskFromHistoryFirst = history.get(0);

        assertEquals(subTask1,taskFromHistoryLast, "Задачи не совпали");
        assertEquals(task2,taskFromHistoryMiddle, "Задачи не совпали");
        assertEquals(task1,taskFromHistoryFirst, "Задачи не совпали");
    }

}