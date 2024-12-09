//проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
//проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
//создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
//убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskStatus statusNew;
    static TaskStatus statusInProgress;
    static TaskStatus statusDone;
    static TaskManager manager;
    static Task task1;
    static Task task2;
    static Epic epic1;
    static Epic epic2;
    static Subtask subTask1;
    static Subtask subTask2;
    static Subtask subTask3;

    @BeforeAll
    static void setUp() {
        statusNew = TaskStatus.NEW;
        statusInProgress = TaskStatus.IN_PROGRESS;
        statusDone = TaskStatus.DONE;
        manager = Managers.getDefault();

        task1 = new Task("Тест создания Таск 1",
                "описание Таск 1", statusNew);
        task2 = new Task("Тест создания Таск 2", 1,
                "описание Таск 1", statusNew);

        epic1 = new Epic("Тест создания Эпик 1", "Это задача -Эпик 1");
        epic2 = new Epic("Тест создания Эпик 2", 1, "Это задача -Эпик 2");

        subTask1 = new Subtask("Тест создания Подзадачи 1",
                "Это подзадача для Эпика", statusNew, 3);
        subTask2 = new Subtask("Тест создания Подзадачи 2",
                "Это подзадача для Эпика", statusNew, 3);
        subTask3 = new Subtask("Тест создания Подзадачи 3", 0,
                "Это подзадача для Эпика", statusNew, 4);

        manager.addTasks(task1);
        manager.addTasks(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubTasks(subTask1);
        manager.addSubTasks(subTask2);
        manager.addSubTasks(subTask3);

    }



//    @Test
//    void addNewTask() {
//        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
//        final int taskId = taskManager.addNewTask(task);
//
//        final Task savedTask = taskManager.getTask(taskId);
//
//        assertNotNull(savedTask, "Задача не найдена.");
//        assertEquals(task, savedTask, "Задачи не совпадают.");
//
//        final List<Task> tasks = taskManager.getTasks();
//
//        assertNotNull(tasks, "Задачи не возвращаются.");
//        assertEquals(1, tasks.size(), "Неверное количество задач.");
//        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
//
//    }
}