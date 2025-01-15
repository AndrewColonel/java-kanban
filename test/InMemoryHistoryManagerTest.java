//Проверьте, что встроенный связный список версий,
// а также операции добавления и удаления работают корректно.


import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {

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

    @BeforeEach
    void setUp() {
        statusNew = TaskStatus.NEW;
        statusInProgress = TaskStatus.IN_PROGRESS;
        statusDone = TaskStatus.DONE;
        manager = Managers.getDefault();

        task1 = new Task("Тест создания Таск 1",
                "описание Таск 1", statusNew);
        task2 = new Task("Тест создания Таск 2", 1,
                "описание Таск 2", statusNew);

        epic1 = new Epic("Тест создания Эпик 1", "Это задача -Эпик 1");
        epic2 = new Epic("Тест создания Эпик 2", 1, "Это задача -Эпик 2");

        subTask1 = new Subtask("Тест создания Подзадачи 1",
                "Это подзадача для Эпика", statusNew, 3);
        subTask2 = new Subtask("Тест создания Подзадачи 2",
                "Это подзадача для Эпика", statusNew, 3);
        subTask3 = new Subtask("Тест создания Подзадачи 3", 0,
                "Это подзадача для Эпика", statusNew, 4);

        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);
        manager.add(epic2);
        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(subTask3);
    }

    @Test
    void addGetHistoryTest() {
        //тест реализации методов ведения журнала истории
        // при использовании соответсвующих методов add, delByID getByID для задач из TaskManager

        for (Task task : manager.getTasksList()) {
            manager.getTaskByID(task.getId());
        }
        for (Task epic : manager.getEpicsList()) {
            manager.getEpicByID(epic.getId());
            for (Task subtask : manager.getSubTasksListByEpic(epic.getId())) {
                manager.getSubTaskByID(subtask.getId());
            }
        }
        for (Task subtask : manager.getSubTasksList()) {
            manager.getSubTaskByID(subtask.getId());
        }

        //список истории доступа к задачам по ID не пустой
        assertNotNull(manager.getHistory(), "Журнал истории не NULL");
        assertFalse(manager.getHistory().isEmpty(), "Журнал истории пуст");
        Task curTask = null;
        for (Task task : manager.getHistory()) {
            //элементы списка не NULL и не равны друг другу (уникальны)
            assertNotEquals(curTask, task, "Записи равны");
            curTask = task;
        }
    }

    @Test
    void RemoveGetHistoryTest() {
        //тест реализации методов ведения журнала истории
        // при использовании соответсвующих методов add, delByID getByID для задач из TaskManager

        //удаляем задачи и эпики, журнал истории должен быть пуст
        for (Task task : manager.getTasksList()) {
            manager.delTaskByID(task.getId());
        }
        for (Task epic : manager.getEpicsList()) {
            manager.delEpicByID(epic.getId());
        }
        assertNotNull(manager.getHistory(), "Журнал истории  NULL");
        assertTrue(manager.getHistory().isEmpty(), "Журнал истории не пуст");
    }

    @Test
    void firstLastElementTest() {
        //тестирования порядка добавления истоии задач в списко

        for (Task epic : manager.getEpicsList()) {
            manager.getEpicByID(epic.getId());
            for (Task subtask : manager.getSubTasksListByEpic(epic.getId())) {
                manager.getSubTaskByID(subtask.getId());
            }
        }
        assertEquals(manager.getEpicsList().getFirst(),
                manager.getHistory().getFirst(), "Элементы не равны");
        assertEquals(manager.getSubTasksList().getLast(),
                manager.getHistory().getLast(), "Элементы не равны");

    }


}