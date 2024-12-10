package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //а. Получение списка всех задач.

    //при объявлении методов лучше использовать интерфейсы,
    // а не их классы реализации. Т.е. интерфейс List, а не класс ArrayList
    List<Task> getTasksList();

    List<Subtask> getSubTasksList();

    List<Epic> getEpicsList();

    //б. Удаление всех задач
    void delTasks();

    void delSubTasks();

    void delEpics();


    List<Task> getHistory();

    //с. Получение по идентификатору.
    Task getTaskByID(int id);

    Subtask getSubTaskByID(int id);

    Epic getEpicByID(int id);

    // d. Создание задачи. Объект передается в качестве параметра.
    void addTasks(Task task);

    void addSubTasks(Subtask subtask);

    void addEpic(Epic epic);

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTasks(Task task);

    void updateSubTasks(Subtask subtask);

    void updateEpic(Epic epic);

    //f. Удаление по идентификатору.
    void delTaskByID(int id);

    void delSubTasksByID(int id);

    void delEpicByID(int id);

    //Дополнительные методы - получение списка всех подзадач определённого эпика
    List<Subtask> getSubTasksListByEpic(int epicId);
}
