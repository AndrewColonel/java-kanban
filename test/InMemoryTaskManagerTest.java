//убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
//проверьте, что service.InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
//проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
//создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @Test
    void utilityClassMakeObjects() {
        //утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
        assertNotNull(manager, "Менеджер не создан"); //возвращает проинициализированныq экземпляр
        assertNotNull(historyManager, "Менеджер не создан"); //возвращает проинициализированныq экземпляр
        manager.add(task1);
        manager.add(task2);
        assertNotNull(manager.getTasksList(), "Список задач не получен"); //методы экзепляра работают

        for (Task task : manager.getTasksList()) { //наполняем спсиок истории
            manager.getTaskByID(task.getId());
        }
        assertNotNull(historyManager.getHistory(), "История задач NULL");
        //прямой вызов метода возвращает NULL
        assertNotNull(manager.getHistory(), "История задач не  получена");
        //метод экзепляра класса типа service.HistoryManager, рабюотает и возвращает непустой список
    }

    @Test
    void InMemoryTaskManagerAddTasksFindByID() {
        //проверяем, что service.InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        manager.add(task1);
        manager.add(epic1);
        manager.add(epic2);
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

        subTask1.setEpicID(epic.getId()); //получаем его id и передаем его в подзадачу
        manager.add(subTask1); //добавляем в ввыбранный эпик подзадачу

        Subtask subtask = manager.getSubTasksList().getFirst();
        assertNotNull(manager.getTasksList(), "Задача не найдена");
        assertNotNull(manager.getEpicsList(), "Эпик не найдет");
        assertNotNull(manager.getSubTasksList(), "Подзадача не найдета");
        //меняем ID у новых, недобавленных задач на id уже прошедших задач через менеджер
        task2.setId(task.getId());
        epic2.setId(epic.getId());
        subTask2.setId(subtask.getId());
        subTask2.setEpicID(epic.getId()); //для подзадачи копируем еще и id эпика
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

        //id, список подзадач и статус после обновления эпика должны оставаться без измений
        assertEquals(epic.getId(), epicNew.getId(), "id не совпали");
        assertEquals(epic.getSubTasksIDs(), epicNew.getSubTasksIDs(), "списки не совпадают");
        assertEquals(epic.getStatus(), epicNew.getStatus(), "статусы не совпали");
    }

    @Test
    void delTasksTest() {
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);
        manager.add(epic2);
        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(subTask3);

        //удаляем задачи и проверяем пустые списки в соответсвии с заданой логикой
        manager.delTasks();
        assertTrue(manager.getTasksList().isEmpty(), "Список Tasks не пуст");
        manager.delSubTasks();
        assertTrue(manager.getSubTasksList().isEmpty(), "Список SubTasks не пуст");
        assertFalse(manager.getEpicsList().isEmpty(), "Список Epics пуст после удаления SubTasks");
        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(subTask3);
        assertFalse(manager.getSubTasksList().isEmpty(), "Список SubTasks пуст");
        manager.delEpics();
        assertTrue(manager.getSubTasksList().isEmpty(), "Список SubTasks не пуст");
        assertTrue(manager.getEpicsList().isEmpty(), "Список Epics не пуст");

        //повтороно вызываем методы удаления (зачистка уже пустых списков выполняется корректно,
        // без ошибок, проверяем список истории
        manager.delTasks();
        manager.delSubTasks();
        manager.delEpics();
        assertTrue(manager.getHistory().isEmpty(), "Список истории не пустой");

    }
}