import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

import service.*;

abstract class TaskManagerTest<T extends TaskManager> {

    static TaskStatus statusNew;
    static TaskStatus statusInProgress;
    static TaskStatus statusDone;
    static InMemoryTaskManager manager;
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
        manager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();

        task1 = (new Task("написать cписок дел",
                "простая, обычная, задача", statusNew, "10.10.2024-00:00", 10));
        task2 = (new Task("погулять с собакой еще раз",
                "простая, обычная, задача - обновлена", statusNew, "10.10.2024-09:00", 30));

        epic1 = (new Epic("Переезд", "Это задача -Эпик №1"));
        epic2 = (new Epic("Проект", "Это задача -Эпик №2"));

        subTask1 = (new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusNew, 3,
                "10.01.2025-17:00", 60));
        subTask2 = (new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3,
                "10.01.2024-17:55", 5));
        subTask3 = (new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusNew, 4,
                "15.02.2025-10:00", 1000));

    }
}

