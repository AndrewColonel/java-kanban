package service;// класс для объекта - менеджера, для управления всеми "задачами"

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    // поля класса - коллекции HashMap для организации хранения задач всех типов - model.Task, SubTask, model.Epic
    // хранилища не должны быть доступны извне класса, поэтому нужен модификатор private

    //теперь и эти хранилища можно объявить через интерфейс Map, а не класс HashMap
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subTasks;

    private int nextId; //Идентификатор задача - уникальное число для сквозной нумерации всех типов задач
    //private List<model.Task> historyList = new ArrayList<>(); - перенесен в service.InMemoryHistoryManager
    HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.nextId = 1;
        this.historyManager = Managers.getDefaultHistory();

    }

    protected void setNextId(int nextId) {
        this.nextId = nextId;
    }

    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //а. Получение списка всех задач.
    @Override
    public List<Task> getTasksList() {
        //Метод должен возвращать список, т.е. ArrayList типа model.Task
        // использую Stream API - есть источник потока данных, есть преобразование
        // и сбор результата в новую структуру данных
        // код при этом упрощяется
        return tasks.keySet().stream()
                .map(tasks::get)
                .toList();
    }

    @Override
    public List<Subtask> getSubTasksList() {
        //Метод должен возвращать список, т.е. ArrayList типа SubTask
        return subTasks.keySet().stream()
                .map(subTasks::get)
                .toList();
    }

    @Override
    public List<Epic> getEpicsList() {
        //Метод должен возвращать список, т.е. ArrayList типа model.Epic
        // использую Stream API - есть источник потока данных, есть преобразование
        // и сбор результата в новую структуру данных
        // код при этом упрощяется
        return epics.keySet().stream()
                .map(epics::get)
                .toList();

    }

    //б. Удаление всех задач
    @Override
    public void delTasks() {
        for (Integer taskId : tasks.keySet()) {
            //удаляем-чистим историю задач перед удалением списка всех задача
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void delSubTasks() {
        //метод удаления всех подзадач
        for (Integer epicId : epics.keySet()) {
            Epic epic = epics.get(epicId);
            epic.delAllSubTasksIDs(); //также очищает внутренние хранилища всех эпиков
            calculateEpicParams(epic); //и выставляет статус очищенных эпиков в NEW
        }
        for (Integer subTaskId : subTasks.keySet()) {
            historyManager.remove(subTaskId); //и чистим историю подзадач
        }
        subTasks.clear();
    }

    @Override
    public void delEpics() {
        //метод удаления всех эпиков
        for (Integer epicId : epics.keySet()) {
            //вытаскиваем список подзадач каждого эпика
            for (Integer subTasksID : epics.get(epicId).getSubTasksIDs()) {
                historyManager.remove(subTasksID); //чистим историю подзадач
            }
            historyManager.remove(epicId); //и чистим историю эпиков
        }
        epics.clear(); //удаляем список эпиков
        subTasks.clear(); //удаляем все подзадачи, они сами по себе не существуют
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
        // Эпик с пустым списком подзадач будет напечатан
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    // d. Создание задачи. Объект передается в качестве параметра.
    @Override
    public void add(Task task) {
        // если временной отрезок задачи не пересекается с остальными задачами
        if (isOverlapsed(task)) return;

        task.setId(nextId++);
        tasks.put(task.getId(), task);

    }

    @Override
    public void add(Subtask subtask) {
        // если временной отрезок задачи не пересекается с остальными задачами
        if (isOverlapsed(subtask)) return;

        if (epics.containsKey(subtask.getEpicID())) { //если соответсвующий подзадачи эпик нашелся
            subtask.setId(nextId++); // то подзадача получает id
            subTasks.put(subtask.getId(), subtask); //и сохраняется в subTasks
            Epic epic = epics.get(subtask.getEpicID()); //вытаскиваем необходимый эпик для добавления подзадачи
            epic.addSubTaskID(subtask.getId()); // и добавляется в список эпика
            calculateEpicParams(epic); // добавилась новая подзадача, рассчитаем статус эпика

        }
    }

    @Override
    public void add(Epic epic) {
        calculateEpicParams(epic);
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void update(Task task) {
        // если временной отрезок задачи не пересекается с остальными задачами
        if (isOverlapsed(task)) return;

        // обновляется только та задача, которая ранее была в tasks
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void update(Subtask subtask) {
        // если временной отрезок задачи не пересекается с остальными задачами
        if (isOverlapsed(subtask)) return;

        // проверяем есть ли такая подзадача, не существующую подзадачу не обновляем
        if (subTasks.containsKey(subtask.getId())) {
            Subtask updatedSubTask = subTasks.get(subtask.getId());
            if (subtask.getEpicID() == updatedSubTask.getEpicID()) {
                // проверяем равенство epicID старой и новой подзадачи
                subTasks.put(subtask.getId(), subtask);//Обновляем подзадачу в хранилище subTasks
                //Пересчитываем статус Эпика, к которой прилинкована эта подзадача
                calculateEpicParams(epics.get(subtask.getEpicID()));
            }
        }
    }

    @Override
    public void update(Epic epic) {
        //Метод может обновить всего два поля: name и description, при этом эпик остается прежним, как объект в памяти,
        // в отличие от задач и подзадач
        if (epics.containsKey(epic.getId())) { //обновляем только существующий эпик
            Epic updatedEpic = epics.get(epic.getId());
            updatedEpic.setName(epic.getName());
            updatedEpic.setDescription(epic.getDescription());
            epics.put(epic.getId(), updatedEpic);
        }
    }

    //f. Удаление по идентификатору.
    @Override
    public void delTaskByID(int id) {
        tasks.remove(id);
        historyManager.remove(id);//удаляем задачу из истории
    }

    @Override
    public void delSubTasksByID(int id) { // удаляя SubTask по ID
        // нужно еще удалить подзадачу из внутреннего хранилища эпика и пересчитать ему статус
        if (subTasks.containsKey(id)) {
            Subtask subTask = subTasks.get(id);
            Epic epic = epics.get(subTask.getEpicID());
            epic.delSubTaskID(id);
            calculateEpicParams(epic); //удалили подзадачу, пересчитали статус
            subTasks.remove(id);
            historyManager.remove(id);//удаляем задачу из истории
        }
    }

    @Override
    public void delEpicByID(int id) {
        //еще удаляем из subTasks все связанные с эпиком подзадачи
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subTasksID : epic.getSubTasksIDs()) {
                subTasks.remove(subTasksID);
                historyManager.remove(subTasksID); //удаляем задачу из истории
            }
        }
        epics.remove(id);
        historyManager.remove(id); //удаляем задачу из истории
    }

    //Дополнительные методы - получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getSubTasksListByEpic(int epicId) {
        return epics.get(epicId).getSubTasksIDs().stream()
                .filter(Objects::nonNull)
                .map(subTasks::get)
                .collect(Collectors.toList());
    }

    //Comparator<Task> taskCompareByDate = (t1, t2) -> t1.getStartTime().compareTo(t2.getStartTime());
// сохраняю компаратор задач по дате начала в переменную  taskCompareByDate для повторного использования
    Comparator<Task> taskCompareByDate = Comparator.comparing(Task::getStartTime);

    private void calculateEpicParams(Epic epic) {
        // метод не должен быть публичным, так как является частью реализации
        // и не должен быть виден или доступен для внешних потребителей
        if (epic != null) { //если такой эпик нашелся
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            // с помощью потока собираю отсортированный по дате начала список subtasks
            List<Subtask> statusSubTasksList =
                    epic.getSubTasksIDs().stream()
                            .map(subTasks::get)
                            .toList();
            // считаем количество статусов NEW
            long countNew = statusSubTasksList.stream()
                    .filter(subtask -> subtask.getStatus() == TaskStatus.NEW)
                    .count();
            // считаем количество статусов DONE
            long countDone = statusSubTasksList.stream()
                    .filter(subtask -> subtask.getStatus() == TaskStatus.DONE)
                    .count();
            if (countNew == statusSubTasksList.size()) { // если все задачи в списке NEW, статус эпика - NEW
                epic.setStatus(TaskStatus.NEW);
            } else if (countDone == statusSubTasksList.size()) { // если все задачи в списке DONE, статус эпика - DONE
                epic.setStatus(TaskStatus.DONE);
            } else {
                // не все задачи в эпике имеют статус только NEW или DONE, эпик имеет статус - IN_PROGRESS
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
            // передаем дату и время  начала и завершения,
            // первой и последней позадачи соотвесвенно в отсортированном списке в поля эпика
            List<Subtask> timedSubTasksList =
                    epic.getSubTasksIDs().stream()
                            .map(subTasks::get)
                            .filter(Task::isStartTimeValid)
                            .filter(Task::isDurationValid)
                            .sorted(taskCompareByDate)
                            .toList();
            if (!timedSubTasksList.isEmpty()) {
                startTime = statusSubTasksList.getFirst().getStartTime();
                endTime = statusSubTasksList.getLast().getEndTime();
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(timedSubTasksList.stream().mapToLong(Task::getDuration).sum());
        }
    }

    protected void recovery(Task task) {
        // метод восстановления в "память" менеджера задач из файла
        // считаем что "память" в файл сохранилась корректно и потерянных эпиков и подзадач нет
        if (task instanceof Subtask subtask) {
            subTasks.put(subtask.getId(), subtask);
        } else if (task instanceof Epic epic) {
            epics.put(epic.getId(), epic);
        } else {
            tasks.put(task.getId(), task);
        }
        //восстанавливаем внутренний список эпиков и их статусы
        for (Subtask subtask : subTasks.values()) {
            Epic epic = epics.get((subtask.getEpicID()));
            if (epic != null) {
                epic.addSubTaskID(subtask.getId());
                calculateEpicParams(epic);
            }
        }
    }

    // метод, возвращающий список задач и подзадач в заданном порядке.
    // для хранения остортированных данных метод добавляет остортированный списко в поле типа TreeSet,
    // сохраняющий уже отсоритированные данные
    public Set<Task> getPrioritizedTasks() {
        List<Task> prioritzedTasksList = new ArrayList<>();
        //собираем все задачи и подзадачи в один список
        prioritzedTasksList.addAll(getTasksList());
        prioritzedTasksList.addAll(getSubTasksList());
        // готовим множество уже с компаратором
        Set<Task> prioritizedTasksSet =
                new TreeSet<>(taskCompareByDate);
        prioritizedTasksSet.addAll(prioritzedTasksList.stream()
                // создаем поток и проверяю, что поле startTime заполнено
                .filter(Task::isStartTimeValid)
                .toList());
        return prioritizedTasksSet;
    }

    public Boolean isOverlapsed(Task task) {
        // Метод anyMatch() проверяет, соответствует ли хотя бы один элемент потока заданному условию (предикату).
        // лямбда внутри выдает true или false, если есть пересечение временных отрезков имеющихся и проверяемой задачи
        // Если хотя бы один элемент удовлетворяет предикату, возвращается true, иначе — false
        if (task.isStartTimeValid())
            return getPrioritizedTasks().stream()
                    // поскольку метод используется и при обновлении сущностей, нужно добавить фильтр по id,
                    // чтобы не сравнивать между собой старую и новую версии
                    .filter(pt -> task.getId() != pt.getId())
                    .anyMatch((pt) ->
                            (task.getStartTime().isAfter(pt.getStartTime())
                                    && (task.getStartTime().isBefore(pt.getEndTime())))
                                    || (task.getEndTime().isAfter(pt.getStartTime())
                                    && (task.getEndTime().isBefore(pt.getEndTime())))
                                    || (task.getStartTime().isBefore(pt.getStartTime())
                                    && (task.getEndTime().isAfter(pt.getEndTime()))));
        else return false;
    }
}
