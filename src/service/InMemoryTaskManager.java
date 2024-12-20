package service;// класс для объекта - менеджера, для управления всеми "задачами"

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {
    // поля класса - коллекции HashMap для организации хранения задач всех типов - model.Task, SubTask, model.Epic
    // хранилища не должны быть доступны извне класса, поэтому нужен модификатор private
    //теперь и эти хранилища можно объявить через интерфейс Map, а не класс HashMap

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subTasks = new HashMap<>();

    private int nextId = 1; //Идентификатор задача - уникальное число для сквозной нумерации всех типов задач
    //private List<model.Task> historyList = new ArrayList<>(); - перернесен в service.InMemoryHistoryManager
    HistoryManager historyManager = Managers.getDefaultHistory();


    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //а. Получение списка всех задач.
    @Override
    public List<Task> getTasksList() {
        //Метод должен возвращать список, т.е. ArrayList типа model.Task
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer i : tasks.keySet()) {
            tasksList.add(tasks.get(i));
        }
        return tasksList;
    }

    @Override
    public List<Subtask> getSubTasksList() {
        //Метод должен возвращать список, т.е. ArrayList типа SubTask
        ArrayList<Subtask> subTasksList = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            subTasksList.add(subTasks.get(i));
        }
        return subTasksList;
    }

    @Override
    public List<Epic> getEpicsList() {
        //Метод должен возвращать список, т.е. ArrayList типа model.Epic
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            epicsList.add(epics.get(i));
        }
        return epicsList;
    }

    //б. Удаление всех задач
    @Override
    public void delTasks() {
        tasks.clear();
    }

    @Override
    public void delSubTasks() {
        //метод удаления всех подзадач
        for (Integer i : epics.keySet()) {
            Epic epic = epics.get(i);
            epic.delAllSubTasksIDs(); //также очищает внутренние хранилища всех эпиков
            calculateEpicStatus(epic);//и выставляет статус очищенных эпиков в NEW
        }
        subTasks.clear();
    }

    @Override
    public void delEpics() {
        //метод удаления всех эпиков,
        epics.clear();
        subTasks.clear();//удаляет все подзадачи, они сами по себе не существуют
    }

    //истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    //с. Получение по идентификатору.
    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubTaskByID(int id) {
        Subtask subtask = subTasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    // d. Создание задачи. Объект передается в качестве параметра.
    @Override
    public void add(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void add(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) { //если соответсвующий подзадачи эпик нашелся
            subtask.setId(nextId++); // то подзадача получает id
            subTasks.put(subtask.getId(), subtask); //и сохраняется в subTasks
            Epic epic = epics.get(subtask.getEpicID()); //вытаскиваем необходимый эпик для добавления подзадачи
            epic.addSubTaskID(subtask.getId()); // и добавляется в список эпика
            calculateEpicStatus(epic); // добавилась новая подзадача, рассчитаем статус эпика
        }
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }


    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void update(Task task) {
        // обновляется только та задача, которая ранее была в tasks
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void update(Subtask subtask) {
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

//    @Override
//    public void update(Epic epic) {
//        //Метод может обновить всего два поля: name и description.
//        // Поэтому нужно вручную переложить значения этих полей в Эпик
//        if (epics.containsKey(epic.getId())) { //обновляем только существующий эпик
//            Epic updatedEpic = epics.get(epic.getId());
//            updatedEpic.setName(epic.getName());
//            updatedEpic.setDescription(epic.getDescription());
//        }
//    }

    @Override
    public void update(Epic epic) {
        //Метод может обновить всего два поля: name и description, при этом эпик остается прежним,
        // в отличии от задач и подзадач
        // Поэтому можно выполнить обертку эпика и замену его в списке-хранилище
        if (epics.containsKey(epic.getId())) { //обновляем только существующий эпик
            Epic updatedEpic = epics.get(epic.getId());
            Epic newEpic = new Epic(updatedEpic); //оборачиваем полученный эпик
            newEpic.setName(epic.getName());
            newEpic.setDescription(epic.getDescription());
            epics.put(epic.getId(), newEpic); //полностью новый эпик в хранилище
        }
    }


    //f. Удаление по идентификатору.
    @Override
    public void delTaskByID(int id) {
        tasks.remove(id);
    }

    @Override
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

    @Override
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
    @Override
    public List<Subtask> getSubTasksListByEpic(int epicId) {
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
            epic.setStatus(TaskStatus.NEW);
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
