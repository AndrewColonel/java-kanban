//проверьте, что экземпляры класса model.Task равны друг другу, если равен их id;
//проверьте, что наследники класса model.Task равны друг другу, если равен их id;
//проверьте, что объект model.Subtask нельзя сделать своим же эпиком;
//проверьте, что объект model.Epic нельзя добавить в самого себя в виде подзадачи;

import model.*;
import org.junit.jupiter.api.*;
import manager.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TaskSubTaskEpicTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    void taskShouldBeEqualsByID() { //проверяем, что экземпляры класса model.Task равны друг другу, если равен их id;
        final int taskID = 1;
        task1.setId(taskID);
        task2.setId(taskID);
        assertNotNull(task1, "Задачи не возвращаются.");
        assertNotNull(task2, "Задачи не возвращаются.");
        assertEquals(task1, task2, "Задачи не совпадают.");
    }

    @Test
    void epicsShouldBeEqualsByID() {
        //проверяем, что наследники класса model.Task - model.Epic равны друг другу, если равен их id;
        final int epicID = 1;
        epic1.setId(epicID);
        epic2.setId(epicID);
        assertNotNull(epic1, "Эпики не возвращаются.");
        assertNotNull(epic2, "Эпики не возвращаются.");
        assertEquals(epic1, epic2, "Эпики не совпадают.");
    }

    @Test
    void subTaskShouldBeEqualsByID() {
        //проверьте, что наследники класса model.Task - SubTask равны, если равен их id;
        final int subTaskID = 1;
        subTask1.setId(subTaskID);
        subTask2.setId(subTaskID);
        assertNotNull(subTask1, "Задачи не возвращаются.");
        assertNotNull(subTask2, "Задачи не возвращаются.");
        assertEquals(subTask1, subTask2, "Задачи не совпадают.");
    }

    @Test
    void subTaskShouldNOTBeEpic() { //проверяем, что объект model.Subtask нельзя сделать своим же эпиком;
        //по условиям ТЗ4 - для каждой подзадачи известно, в рамках какого эпика она выполняется,
        //на основании заполнения поля EpicID
        // model.Subtask станет своим же эпиком, если его id будет равно id его же эпика
        int epicID = subTask1.getEpicID(); // получаем ID эпика данного model.Subtask
        subTask1.setId(epicID); // принудительно меняем id самого model.Subtask на ID ее же эпика
        manager.add(subTask1); //добавляем Подзадачу через менеджер в общее хранилище подзадач
        Subtask subtask = manager.getSubTaskByID(epicID);
        //запрашиваемый по id = epicid, subtask не должен быть получен, потому что
        // его id должен быть изменен в менеджере и только потом записан в общее хранилище подзадач
        assertNull(subtask, "Задача найдена!");
    }

    @Test
    void epicShouldNOTBeAddedLikeEpic() {
        //проверяем, что объект model.Epic нельзя добавить в самого себя в виде подзадачи;
        // по условиям ТЗ4 - Каждый эпик знает, какие подзадачи в него входят.
        //добавить model.Epic сам в себя в виде подзадачи, это добавить его id в хранилище subTasksIDs
        int epicID = epic1.getId(); //получаем id Эпика
        epic1.addSubTaskID(epicID); // передаем его в хранилище subTasksIDs и пытаемся получить обратно
        assertFalse(epic1.getSubTasksIDs().contains(epicID), "Задача найдена!");
    }

    @Test
    void nodeShouldBeEqualswithSameTask() {
        //проверяем корректность операции сравнения двух узлов связного списка
        Node<Task> nodeTask1 = new Node<>(task1);
        Node<Task> nodeTask2 = new Node<>(task1);
        Node<Task> nodeSubTask1 = new Node<>(subTask1);
        assertEquals(nodeTask1, nodeTask2, "Узлы не совпадают");
        assertNotEquals(nodeTask1, nodeSubTask1, "Узлы совпадают");
    }

    @Test
    void subTaskTest() {
        // Для подзадач необходимо дополнительно убедиться в наличии связанного эпика.
        // epic -> subTaskId -> subTask -> epicId -> epic - assertEqual
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);
        manager.add(epic2);
        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(subTask3);
        assertEquals(manager.getEpicsList().getFirst().getId(),
                manager.getSubTaskByID(
                        manager.getEpicsList().getFirst()
                                .getSubTasksIDs().getFirst()).getEpicID(),
                "Эти два Эпика не равны");
    }

    @Test
    void epicTest() {
        // Для эпиков нужно проверить корректность расчёта статуса на основании состояния подзадач.
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);
        manager.add(epic2);
        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(subTask3);

        // нахожу эпик, содержащий более чем 1 subtask
        manager.getEpicsList().stream()
                .filter(e -> e.getSubTasksIDs().size() > 1)
                .findFirst() // поиск первого же эпика, имеющего более 1 подзадачи
                .ifPresent(epic ->
                {
                    //все subtask - со статусом NEW
                    manager.getSubTasksListByEpic(epic.getId())
                            .forEach(subtask -> {
                                subtask.setStatus(TaskStatus.NEW);
                                manager.update(subtask);
                            });

                    assertEquals(epic.getStatus(), TaskStatus.NEW,
                            "Статус не равен NEW");

                    //все subtask - со статусом DONE
                    manager.getSubTasksListByEpic(epic.getId())
                            .forEach(subtask -> {
                                subtask.setStatus(TaskStatus.DONE);
                                manager.update(subtask);
                            });
                    assertEquals(epic.getStatus(), TaskStatus.DONE,
                            "Статус не равен DONE");

                    //все subtask - со статусом INPROGRESS
                    manager.getSubTasksListByEpic(epic.getId())
                            .forEach(subtask -> {
                                subtask.setStatus(TaskStatus.IN_PROGRESS);
                                manager.update(subtask);
                            });
                    assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS,
                            "Статус не равен INPROGRESS когда все sabtask INPROGRESS");

                    //один subtask - со статусом NEW, другой DONE
                    manager.getSubTasksListByEpic(epic.getId()).getFirst().setStatus(TaskStatus.NEW);
                    manager.getSubTasksListByEpic(epic.getId()).getLast().setStatus(TaskStatus.DONE);
                    manager.update(epic);
                    assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS,
                            "Статус не равен INPROGRESS");
                });
    }

    @Test
    void overlapTest() {
        // Убедиться, что реализован корректный расчёт пересечения временных интервалов задач,
        // чтобы предотвратить конфликтные ситуации.

        // нахожу эпик, содержащий более чем 1 subtask
        manager.getEpicsList().stream()
                .filter(e -> e.getSubTasksIDs().size() > 1)
                .findFirst() // поиск первого же эпика, имеющего более 1 подзадачи
                .ifPresent(epic -> {
                            //Установлены олинаковые время старта и продолжительсность
                            manager.getSubTasksListByEpic(epic.getId()).getFirst()
                                    .setStartTime(LocalDateTime.now());
                            manager.getSubTasksListByEpic(epic.getId()).getFirst()
                                    .setDuration(60L);
                            manager.getSubTasksListByEpic(epic.getId()).getLast()
                                    .setStartTime(LocalDateTime.now());
                            manager.getSubTasksListByEpic(epic.getId()).getLast()
                                    .setDuration(60L);
                            assertTrue(manager.isOverlapsed(
                                            manager.getSubTasksListByEpic(epic.getId()).getFirst()),
                                    "Интервалы не пересекаются");
                            assertTrue(manager.isOverlapsed(
                                            manager.getSubTasksListByEpic(epic.getId()).getLast()),
                                    "Интервалы не пересекаются");

                            //интервалы у двух событий точно не пересекаются
                            manager.getSubTasksListByEpic(epic.getId()).getFirst()
                                    .setStartTime(LocalDateTime.now());
                            manager.getSubTasksListByEpic(epic.getId()).getFirst()
                                    .setDuration(60L);
                            manager.getSubTasksListByEpic(epic.getId()).getLast()
                                    .setStartTime(LocalDateTime.now().plusMinutes(100));
                            manager.getSubTasksListByEpic(epic.getId()).getLast()
                                    .setDuration(60L);
                            assertFalse(manager.isOverlapsed(
                                            manager.getSubTasksListByEpic(epic.getId()).getFirst()),
                                    "Интервалы пересекаются");
                            assertFalse(manager.isOverlapsed(
                                            manager.getSubTasksListByEpic(epic.getId()).getLast()),
                                    "Интервалы пересекаются");

                            //Установлены пересекающиеся интервалы
                            manager.getSubTasksListByEpic(epic.getId()).getFirst()
                                    .setStartTime(LocalDateTime.now());
                            manager.getSubTasksListByEpic(epic.getId()).getFirst()
                                    .setDuration(60L);
                            manager.getSubTasksListByEpic(epic.getId()).getLast()
                                    .setStartTime(LocalDateTime.now().plusMinutes(30));
                            manager.getSubTasksListByEpic(epic.getId()).getLast()
                                    .setDuration(60L);
                            assertTrue(manager.isOverlapsed(
                                            manager.getSubTasksListByEpic(epic.getId()).getFirst()),
                                    "Интервалы не пересекаются");
                            assertTrue(manager.isOverlapsed(
                                            manager.getSubTasksListByEpic(epic.getId()).getLast()),
                                    "Интервалы не пересекаются");
                        }
                );


    }
}