// класс для объекта - менеджера, для управления всеми "задачами"

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    // поля класса - коллекции HashMap для организации хранения задач всех типов - Task, SubTask, Epic
    // хранилища не должны быть доступны извне класса, поэтому нужен модификатор private
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private int nextId = 1; //Идентификатор задача - уникальное число для сквозной нумерации всех типов задач

    // для всех атрибутов класса нужны геттеры и сеттеры
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(HashMap<Integer, Epic> epics) {
        this.epics = epics;
    }

    public HashMap<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(HashMap<Integer, Subtask> subTasks) {
        this.subTasks = subTasks;
    }

    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //а. Получение списка всех задач.
    public ArrayList<Task> getTasksList() {
        //Метод должен возвращать список, т.е. ArrayList типа Task
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer i : tasks.keySet()) {
            tasksList.add(tasks.get(i));
        }
        return tasksList;
    }

    public ArrayList<Subtask> getSubTasksList() {
        //Метод должен возвращать список, т.е. ArrayList типа SubTask
        ArrayList<Subtask> subTasksList = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            subTasksList.add(subTasks.get(i));
        }
        return subTasksList;
    }

    public ArrayList<Epic> getEpicsList() {
        //Метод должен возвращать список, т.е. ArrayList типа Epic
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            epicsList.add(epics.get(i));
        }
        return epicsList;
    }

    //б. Удаление всех задач
    public void delTasks() {
        tasks.clear();
    }

    public void delSubTasks() {
        //метод удаления всех подзадач
        for (Integer i : epics.keySet()) {
            Epic epic = epics.get(i);
            epic.delAllSubTasksIDs(); //также очищает внутренние хранилища всех эпиков
            epic.setStatus(TaskStatus.NEW);//и выставляет статус очищенных эпиков в NEW
        }
        subTasks.clear();
    }

    public void delEpics() {
        //метод удаления всех эпиков,
        epics.clear();
        subTasks.clear();//удаляет все подзадачи, они сами по себе не существуют
    }

    //с. Получение по идентификатору.
    public Task getTaskByID(int id) {
        return tasks.get(id);
    }

    public Subtask getSubTaskByID(int id) {
        return subTasks.get(id);
    }

    public Epic getEpicByID(int id) {
        return epics.get(id);
    }

    // d. Создание задачи. Объект передается в качестве параметра.
    public void addTasks(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void addSubTasks(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) { //если соответсвующий подзадачи  эпик нашелся
            subtask.setId(nextId++); // то подзадача получает id
            subTasks.put(subtask.getId(), subtask); //и сохраняется в subTasks
            Epic epic = epics.get(subtask.getEpicID()); //вытаскиваем необходимый эпик для добавления подзадачи
            epic.addSubTaskID(subtask.getId()); // и добавляется в список эпика
            calculateEpicStatus(epic); // добавилась новая подзадача, рассчитаем статус эпика
        }
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }


    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTasks(Task task) {
        // обновляется только та задача, которая ранее была в tasks
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubTasks(Subtask subtask) {
        // проверяем есть ли такая подзадача, не существующую подзадачу не обновляем
        if (subTasks.containsKey(subtask.getId())) {
            Subtask updatedSubTask = subTasks.get(subtask.getId());
            if (subtask.getEpicID() == updatedSubTask.getEpicID()) {
                // проверяем равенство epicID старой и новой подзадачи
                subTasks.put(subtask.getId(), subtask);//Обновляем подзадачу в хранилище subTasks
                //Пересчитываем статус Эпика, к которой прилинкована эта подзадача
                calculateEpicStatus(epics.get(subtask.getEpicID()));
            }
        }
    }

    public void updateEpic(Epic epic) {
        //Метод может обновить всего два поля: name и description.
        // Поэтому нужно вручную переложить значения этих полей в Эпик
        if (epics.containsKey(epic.getId())) { //обновляем только существующий эпик
            Epic updatedEpic = epics.get(epic.getId());
            updatedEpic.setName(epic.getName());
            updatedEpic.setDescription(epic.getDescription());
        }
    }

    //f. Удаление по идентификатору.
    public void delTaskByID(int id) {
        tasks.remove(id);
    }

    public void delSubTasksByID(int id) { // удаляя SubTask по ID
        // нужно еще удалить подзадачу из внутреннего хранилища эпика и пересчитать ему статус
        if (subTasks.containsKey(id)) {
            Subtask subTask = subTasks.get(id);
            Epic epic = epics.get(subTask.getEpicID());
            epic.delSubTaskID(id);
            calculateEpicStatus(epic); //удалили подзадачу, пересчитали статус
            subTasks.remove(id);
        }
    }

    public void delEpicByID(int id) {
        //еще удаляем из subTasks все связанные с эпиком подзадачи
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subTasksID : epic.getSubTasksIDs()) {
                subTasks.remove(subTasksID);
            }
        }
        epics.remove(id);
    }

    //Дополнительные методы - получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getSubTasksListByEpic(int epicId) {
        //сигнатуру метода нужно сделать такой public ArrayList<SubTask> getSubTasksList (int epicId)
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            for (Integer subTasksID : epic.getSubTasksIDs()) {
                Subtask subtask = subTasks.get(subTasksID);
                subTaskList.add(subtask);
            }
        }
        return subTaskList;
    }

    // метод расчета статуса эпика в зависимости от статусов подзадач
    private void calculateEpicStatus(Epic epic) {
        // метод не должен быть публичным, так как является частью реализации
        // и не должен быть виден или доступен для внешних потребителей
        if (epic != null) { //если такой эпик нашелся
            int countNew = 0;
            int countDone = 0;
            ArrayList<Integer> subTasksIDs = epic.getSubTasksIDs(); // вытаскиваем список подзадач данного эпика
            for (Integer i : subTasksIDs) { //перебираем этот список подзадач
                Subtask subtask = subTasks.get(i); //вытаскиваем каждую подзадачу
                switch (subtask.getStatus()) { // проверяем статус каждой подзадачи
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
                    epic.setStatus(TaskStatus.NEW);
                } else if (countDone == subTasksIDs.size()) { // если все задачи в списке DONE, статус эпика - DONE
                    epic.setStatus(TaskStatus.DONE);
                } else {
                    // не все задачи в эпике имеют статус только NEW или DONE, эпик имеет статус - IN_PROGRESS
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                }
            }
        }
    }

}
