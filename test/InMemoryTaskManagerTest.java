//убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
//проверьте, что service.InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
//проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
//создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class InMemoryTaskManagerTest {

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
                "Это подзадача для Эпика", statusNew, 3);
        subTask2 = new Subtask("Тест создания Подзадачи 2",
                "Это подзадача для Эпика", statusNew, 3);
        subTask3 = new Subtask("Тест создания Подзадачи 3", 0,
                "Это подзадача для Эпика", statusNew, 4);

    }

    @Test
    void utilityClassMakeObjects() {
        //утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
        assertNotNull(manager, "Менеджер не создан"); //возвращает проинициализированныq экземпляр
        assertNotNull(historyManager, "Менеджер не создан");//возвращает проинициализированныq экземпляр
        manager.add(task1);
        manager.add(task2);
        assertNotNull(manager.getTasksList(), "Список задач не получен");//методы экзепляра работают

        for (Task task : manager.getTasksList()) {//наполняем спсиок истории
            manager.getTaskByID(task.getId());
        }
        assertNull(historyManager.getHistory(), "История задач  получена");
        //прямой вызов метода возвращает NULL
        assertNotNull(manager.getHistory(), "История задач не  получена");
        //метод экзепляра класса типа service.HistoryManager, рабюотает и возвращает непустой список
    }

    @Test
    void InMemoryTaskManagerAddTasksFindByID() {
        //проверяем, что service.InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        manager.add(task1);
        manager.add(epic1);
        manager.add(subTask1);
        for (Task task : manager.getTasksList()) {
            assertNotNull(manager.getTaskByID(task.getId()), "Список задач не получен");
        }
        for (Subtask subtask : manager.getSubTasksList()) {
            assertNotNull(manager.getSubTaskByID(subtask.getId()), "Список подзадач не получен");
        }
        for (Epic epic : manager.getEpicsList()) {
            assertNotNull(manager.getEpicByID(epic.getId()), "Список эпиков не получен");
        }
    }

    @Test
    void addNewTask() {
        //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
        manager.add(task1); //добавляем первую задачу через менеджер
        task2.setId(1); // для второй задачи задаем id=1
        manager.add(task2); //добавляем вторую задачу через менеджер
        List<Task> tasks = manager.getTasksList();
        Task firstTask = tasks.get(0);
        Task secondTask = tasks.get(1);
        System.out.println(secondTask);
        assertNotNull(firstTask, "Задача не найдена."); //задачи добавлены
        assertNotNull(secondTask, "Задача не найдена.");
        assertNotEquals(firstTask, secondTask, "Задачи совпадают");//
    }

    @Test
    void taskFieldsCheck() {
        //создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
        String nameBefore = task1.getName();
        String descriptionBefore = task1.getDescription();
        int idBefore = task1.getId();
        TaskStatus taskStatusBefore = task1.getStatus();
        manager.add(task1);
        Task task = manager.getTasksList().getFirst();
        assertEquals(nameBefore, task.getName(), "Имена не совпали!");
        assertEquals(descriptionBefore, task.getDescription(), "Описание не совпало!");
        assertNotEquals(idBefore, task.getId(), "id совпали!");
        assertEquals(taskStatusBefore, task.getStatus(), "Статусы различаются!");
    }

    @Test
    void updateTest() {
        //проверяем работу мотодов обновления
        manager.add(task1); //добавляем через менеджеор задачи, эпики и подзадачи
        Task task = manager.getTasksList().getFirst();
        manager.add(epic1);
        Epic epic = manager.getEpicsList().getFirst(); //достаем первый эпик

        subTask1.setEpicID(epic.getId());//получаем его id и передаем его в подзадачу
        manager.add(subTask1); //добавляем в ввыбранный эпик подзадачу

        Subtask subtask = manager.getSubTasksList().getFirst();
        assertNotNull(manager.getTasksList(), "Задача не найдена");
        assertNotNull(manager.getEpicsList(), "Эпик не найдет");
        assertNotNull(manager.getSubTasksList(), "Подзадача не найдета");
        //меняем ID у новых, недобавленных задач на id уже прошедших задач через менеджер
        task2.setId(task.getId());
        epic2.setId(epic.getId());
        subTask2.setId(subtask.getId());
        subTask2.setEpicID(epic.getId());//для подзадачи копируем еще и id эпика
        //обновляем имеющиеся задачи через менеджер

        manager.update(task2);
        manager.update(epic2);
        manager.update(subTask2);

        //вытаскиваем обновленные задачи
        Task taskNew = manager.getTasksList().getFirst();
        Epic epicNew = manager.getEpicsList().getFirst();
        Subtask subtaskNew = manager.getSubTasksList().getFirst();
        //сравниваем задачи - должны быть равны, т.к. равны их id
        assertEquals(task, taskNew, "задачи не равны");
        assertEquals(epic, epicNew, "эпики не равны");
        assertEquals(subtask, subtaskNew, "подзадачи не равны");
        //сравниваем по полям - name

        assertNotEquals(task.getName(), taskNew.getName(), "имена совпамли");
        assertNotEquals(subtask.getName(), subtaskNew.getName(), "имена совпамли");
        //отдельно проверяем обновление эпика по всем полям
        //имя и описание должны быть различны и не совпадать
        assertNotEquals(epic.getName(), epicNew.getName(), "имена совпамли");
        assertNotEquals(epic.getDescription(), epicNew.getDescription(), "описание совпамли");
        //id, список подзадач и статус после обновления эпика должны оставаться без измений
        assertEquals(epic.getId(), epicNew.getId(), "id не совпали");
        assertEquals(epic.getSubTasksIDs(), epicNew.getSubTasksIDs(), "списки не совпадают");
        assertEquals(epic.getStatus(), epicNew.getStatus(), "статусы не совпали");
    }
}