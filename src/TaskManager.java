// класс для объекта - менеджера, для управления всеми "задачами"

import java.util.ArrayList;
import java.util.HashMap;
//TODO общий комментарий к классу: обращайся к полям классов через геттеры и сеттеры


public class TaskManager {
    // поля класса - коллекции HashMap для организации хранения задач всех типов - Task, SubTask, Epic
    // TODO хранилища не должны быть доступны извне класса, поэтому нужен модификатор private
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private int nextId = 1; //Идентификатор задача - уникальное число для сквозной нумерации всех типов задач


    //Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    //a. Получение списка всех задач.
//TODO метод должен возвращать список, т.е. ArrayList типа Task
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubTasks() {
        //TODO метод должен возвращать список, т.е. ArrayList типа SubTask
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        //TODO метод должен возвращать список, т.е. ArrayList типа Epic
        return epics;
    }

    //b. Удаление всех задач
    public void delTasks() {
        tasks.clear();
    }

    public void delSubTasks() {
        //TODO метод удаления всех подзадач, должен привести к
        // очистке внутренних хранилищ всех эпиков и пересчету их статусов
        subTasks.clear();
    }

    public void delEpics() {
        //TODO метод удаления всех эпиков, должен удалить также и подзадачи,
        // поскольку они сами по себе не существуют
        epics.clear();
    }

    //c. Получение по идентификатору.
    public Task getByIDtasks(int id) {
        Task task = null;
        if (!tasks.isEmpty()) {
            //TODO можно убрать эту проверку, если в мапе нет значения этого ключа,
            // то она возвращает тот же null  и не разваливается
            task = tasks.get(id);
        }
        return task;
    }

    public Subtask getByIDsubTasks(int id) {
        Subtask subTask = null;
        if (!subTasks.isEmpty()) {
            //TODO можно убрать эту проверку, если в мапе нет значения этого ключа,
            // то она возвращает тот же null  и не разваливается
            subTask = subTasks.get(id);
        }
        return subTask;
    }

    public Epic getByIDepics(int id) {
        Epic epic = null;
        if (!epics.isEmpty()) {
            //TODO можно убрать эту проверку, если в мапе нет значения этого ключа,
            // то она возвращает тот же null  и не разваливается
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
            //TODO подзадачи создаются только как декомпозиция эпика, поэтому проверку на
            // существование эпика нужно сделать первым делом в методе. Если эпика нет,
            // то подзадача не получает id и не сохраняется в subTasks
            if (epic != null) { //если такой эпик нашелся
                ArrayList<Integer> subTasksIDs = epic.getSubTasksIDs();
                subTasksIDs.add(subtask.id);
                //TODO для этого действия предложил добавить специальный метод в классе Epic,
                // а здесь им воспользоваться

                // добавляем в список новый уникальный ID подзадачи
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
        //TODO обновляется только та задача, которая ранее была в tasks
        tasks.put(task.id, task);
    }

    public void updateSubTasks(Subtask subtask) {
        /*TODO давай в виде алгоритма в целом опишу логику обновления подзадачи, думаю так будет понятнее
           1) проверяем есть ли такая подзадача, не существующую подзадачу не обновляем
           2) По замыслу сервиса подзадачи не переходят между Эпиками (это и логично, подзадача из Эпика ген уборка,
           вряд ли пригодится Эпику получение высшего образования). поэтому проверяем равен ли epicID в новой подзадаче
           со значением epicID подзадачи, которая уже была ранее в хранилище subTasks
           3) Обновляем подзадачу в хранилище subTasks
           4) Пересчитываем статус Эпика, к которой прилинкована эта подзадача */


        subTasks.put(subtask.id, subtask);

        if (!epics.isEmpty()) {
            Epic epic = epics.get(subtask.getEpicID());//ищем необходимый эпик для обновления подзадачи
            if (epic != null) { //если такой эпик нашелся
                calculateEpicStatus(epic); // подзадача была обновлена, расчитаем статус эпика
            }
        }
    }

    public void updateEpic(Epic epic) {
        /*TODO для обновления Эпика просто заменить в мапе недостаточно. У эпика такие атрибуты как Status и
           список связанных с ним подзадач (subTasksIDs) определяется не самим эпиком, а его подзадачами.
           Поэтому фактически метод updateEpic(Epic epic) может обновить всего два поля: name и description.
           Поэтому нужно вручную переложить значения этих полей в Эпик, который уже есть в хранилище.
           Соответственно, если там такого id нет, то ничего не делаем*/


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
            //TODO нужно еще удалить подзадачу из внутреннего хранилища эпика и пересчитать ему статус
            subTasks.remove(id);
        }
    }

    public void delByIDepics(int id) {
        //TODO еще нужно удалить из subTasks все связанные с эпиком подзадачи
        if (!epics.isEmpty()) {
            epics.remove(id);
        }
    }

    //Дополнительные методы - получение списка всех подзадач определённого эпика
    public ArrayList<Integer> getSubTasksList(Epic epic) {
        //TODO сигнатуру метода нужно сделать такой public ArrayList<SubTask> getSubTasksList (int epicId)
        return epic.getSubTasksIDs();
    }

    // метод расчета статуса эпика в зависимости от статусов подзадач
    public void calculateEpicStatus(Epic epic) {
        //TODO метод не должен быть публичным, так как является частью реализации
        // и не должен быть виден или доступен для внешних потребителей
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
