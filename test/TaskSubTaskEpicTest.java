//проверьте, что экземпляры класса Task равны друг другу, если равен их id;
//проверьте, что наследники класса Task равны друг другу, если равен их id;
//проверьте, что объект Subtask нельзя сделать своим же эпиком;
//проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskSubTaskEpicTest {

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
                "описание Таск 2", statusNew);

        epic1 = new Epic("Тест создания Эпик 1", "Это задача -Эпик 1");
        epic2 = new Epic("Тест создания Эпик 2", 1, "Это задача -Эпик 2");

        subTask1 = new Subtask("Тест создания Подзадачи 1",
                "Это подзадача для Эпика", statusNew, 3);
        subTask2 = new Subtask("Тест создания Подзадачи 2",
                "Это подзадача для Эпика", statusNew, 3);
        subTask3 = new Subtask("Тест создания Подзадачи 3", 0,
                "Это подзадача для Эпика", statusNew, 4);

    }

    @Test
    void taskShouldBeEqualsByID() { //проверяем, что экземпляры класса Task равны друг другу, если равен их id;
        final int taskID = 1;
        task1.setId(taskID);
        task2.setId(taskID);
        assertNotNull(task1, "Задачи не возвращаются.");
        assertNotNull(task2, "Задачи не возвращаются.");
        assertEquals(task1, task2, "Задачи не совпадают.");
    }

    @Test
    void epicsShouldBeEqualsByID() { //проверяем, что наследники класса Task - Epic равны друг другу, если равен их id;
        final int epicID = 1;
        epic1.setId(epicID);
        epic2.setId(epicID);
        assertNotNull(epic1, "Эпики не возвращаются.");
        assertNotNull(epic2, "Эпики не возвращаются.");
        assertEquals(epic1, epic2, "Эпики не совпадают.");
    }

    @Test
    void subTaskShouldBeEqualsByID() { //проверьте, что наследники класса Task - SubTask равны, если равен их id;
        final int subTaskID = 1;
        subTask1.setId(subTaskID);
        subTask2.setId(subTaskID);
        assertNotNull(subTask1, "Задачи не возвращаются.");
        assertNotNull(subTask2, "Задачи не возвращаются.");
        assertEquals(subTask1, subTask2, "Задачи не совпадают.");
    }

    @Test
    void subTaskShouldNOTBeEpic() { //проверяем, что объект Subtask нельзя сделать своим же эпиком;
        //по условиям ТЗ4 - для каждой подзадачи известно, в рамках какого эпика она выполняется,
        //на основании заполнения поля EpicID
        // Subtask станет своим же эпиком, если его id будет равно id его же эпика
        int epicID = subTask1.getEpicID(); // получаем ID эпика данного Subtask
        subTask1.setId(epicID); // принудительно меняем id самого Subtask на ID ее же эпика
        manager.addSubTasks(subTask1);//добавляем Подзадачу через менеджер в общее хранилище подзадач
        Subtask subtask = manager.getSubTaskByID(epicID);
        //запрашиваемый по id = epicid, subtask не должен быть получен, потому что
        // его id должен быть изменен в менеджере и только потом записан в общее хранилище подзадач
        assertNull(subtask, "Задача найдена!");
    }

    @Test
    void epicShouldNOTBeAddedLikeEpic() { //проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи;
        // по условиям ТЗ4 - Каждый эпик знает, какие подзадачи в него входят.
        //добавить Epic сам в себя в виде подзадачи, это добавить его id в хранилище subTasksIDs
        int epicID = epic1.getId(); //получаем id Эпика
        epic1.addSubTaskID(epicID); // передаем его в хранилище subTasksIDs и пытаемся получить обратно
        assertFalse(epic1.getSubTasksIDs().contains(epicID), "Задача найдена!");
    }

}