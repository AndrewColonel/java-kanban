//Проверьте, что встроенный связный список версий,
// а также операции добавления и удаления работают корректно.
// тесты для всех методов интерфейса. Граничные условия:
//a. Пустая история задач.
//b. Дублирование.
//c. Удаление из истории: начало, середина, конец.

import model.*;
import org.junit.jupiter.api.*;
import manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        super.setUp();

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

        manager.getTasksList().forEach(task -> manager.getTaskByID(task.getId()));

        manager.getEpicsList().forEach(epic -> {
            manager.getEpicByID(epic.getId());
            manager.getSubTasksListByEpic(epic.getId());
        });
        manager.getSubTasksList().forEach(subtask->
                manager.getSubTaskByID(subtask.getId()));

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
        manager.getTasksList().forEach(task ->
            manager.delTaskByID(task.getId()));

        manager.getEpicsList().forEach(epic ->
                manager.delEpicByID(epic.getId()));

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