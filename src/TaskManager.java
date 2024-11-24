// класс для объекта - менеджера, для управления всеми "задачами"

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    // поля класса - коллекции HashMap для организации хранения задач всех типов - Task, SubTask, Epic
    // модификаторы доступа - по-умолчанию
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private int nextId = 1; //Идентификатор задача - уникальное число для сквозной нумерации всех типов задач


    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //a. Получение списка всех задач.
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    //b. Удаление всех задач
    public void delTasks() {
        tasks.clear();
    }

    public void delSubTasks() {
        subTasks.clear();
    }

    public void delEpics() {
        epics.clear();
    }

    //c. Получение по идентификатору.
    public Task getByIDtasks(int id) {
        Task task = null;
        if (!tasks.isEmpty()) {
            task = tasks.get(id);
        }
        return task;
    }

    public Subtask getByIDsubTasks(int id) {
        Subtask subTask = null;
        if (!subTasks.isEmpty()) {
            subTask = subTasks.get(id);
        }
        return subTask;
    }

    public Epic getByIDepics(int id) {
        Epic epic = null;
        if (!epics.isEmpty()) {
            epic = epics.get(id);
        }
        return epic;
    }

    // d. Создание задачи. Объект передается в качестве параметра.
    public void addTasks(Task task) {
        task.id = nextId++;
        tasks.put(task.id, task);
    }

    public void addSubTasks(Subtask subtask) {
        subtask.id = nextId++;
        subTasks.put(subtask.id, subtask);
        if (!epics.isEmpty()) {
            Epic epic = epics.get(subtask.getEpicID()); //ищем необходимый эпик для добавления подзадачи
            if (epic != null) { //если такой эпик нашелся
                ArrayList<Integer> subTasksIDs = epic.getSubTasksIDs();
                subTasksIDs.add(subtask.id); // добавляем в список новый уникальный ID подзадачи
                calculateEpicStatus(epic); // добавилась новая подзадача, расчитаем статус эпика
            }
        }
    }

    public void addEpic(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.id, epic);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTasks(Task task) {
        tasks.put(task.id, task);
    }

    public void updateSubTasks(Subtask subtask) {
        subTasks.put(subtask.id, subtask);

        if (!epics.isEmpty()) {
            Epic epic = epics.get(subtask.getEpicID());//ищем необходимый эпик для обновления подзадачи
            if (epic != null) { //если такой эпик нашелся
                calculateEpicStatus(epic); // подзадача была обновлена, расчитаем статус эпика
            }
        }
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.id, epic);
    }

    //f. Удаление по идентификатору.
    public void delByIDtasks(int id) {
        if (!tasks.isEmpty()) {
            tasks.remove(id);
        }
    }

    public void delByIDsubTasks(int id) {
        if (!subTasks.isEmpty()) {
            subTasks.remove(id);
        }
    }

    public void delByIDepics(int id) {
        if (!epics.isEmpty()) {
            epics.remove(id);
        }
    }

    //Дополнительные методы - получение списка всех подзадач определённого эпика
    public ArrayList<Integer> getSubTasksList(Epic epic) {
        return epic.getSubTasksIDs();
    }

    // метод расчета статуса эпика в зависимости от статусов подзадач
    public void calculateEpicStatus(Epic epic) {
        if (epic != null) { //если такой эпик нашелся
            int countNew = 0;
            int countDone = 0;
            ArrayList<Integer> subTasksIDs = epic.getSubTasksIDs(); // вытаскиваем списко подзадач данного эпика
            for (Integer subTasksID : subTasksIDs) { //перебираем этот список подзадач
                Subtask subtask = subTasks.get(subTasksID); //вытаскиваем каждую подзадачу
                switch (subtask.status) { // проверяем статус каждой подзадачи
                    case NEW:
                        countNew++; //считаем кол-во подзадач со статусом NEW
                        break;
                    case DONE:
                        countDone++;//считаем кол-во подзадач со статусом DONE
                        break;
                    default:
                        break;
                }
                if (countNew == subTasksIDs.size()) { // если все задачи в списке NEW, статус эпика - NEW
                    epic.setEpicStatus(TaskStatus.NEW);
                } else if (countDone == subTasksIDs.size()) { // если все задачи в списке DONE, статус эпика - DONE
                    epic.setEpicStatus(TaskStatus.DONE);
                } else {
                    // не все задачи в эпике имеют статус только NEW или DONE, эпик имеет статус - IN_PROGRESS
                    epic.setEpicStatus(TaskStatus.IN_PROGRESS);
                }
            }
        }
    }
}
