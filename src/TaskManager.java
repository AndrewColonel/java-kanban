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
    //a. Получение списка всех задач.
    public ArrayList<Task> getTasksList() {
        //метод должен возвращать список, т.е. ArrayList типа Task
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer i : tasks.keySet()) {
            tasksList.add(tasks.get(i));
        }
        return tasksList;
    }

    public ArrayList<Subtask> getSubTasksList() {
        //метод должен возвращать список, т.е. ArrayList типа SubTask
        ArrayList<Subtask> subTasksList = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            subTasksList.add(subTasks.get(i));
        }
        return subTasksList;
    }

    public ArrayList<Epic> getEpicsList() {
        //метод должен возвращать список, т.е. ArrayList типа Epic
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            epicsList.add(epics.get(i));
        }
        return epicsList;
    }

    //b. Удаление всех задач
    public void delTasks() {
        tasks.clear();
    }

    public void delSubTasks() {
        //метод удаления всех подзадач
        for (Integer i : epics.keySet()) {
            Epic epic = epics.get(i);
            epic.delAllSubTasksIDs(); //также очищает  внутренние хранилища всех эпиков
            epic.setStatus(TaskStatus.NEW);//и выставляет  статус очищенных эпиков в NEW
        }
        subTasks.clear();
    }

    public void delEpics() {
        //метод удаления всех эпиков,
        epics.clear();
        subTasks.clear();//удаляет все подзадачи, они сами по себе не существуют
    }

    //c. Получение по идентификатору.
    public Task getByIDtasks(int id) {
        return tasks.get(id);
    }

    public Subtask getByIDsubTasks(int id) {
        return subTasks.get(id);
    }

    public Epic getByIDepics(int id) {
        return epics.get(id);
    }

    // d. Создание задачи. Объект передается в качестве параметра.
    public void addTasks(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void addSubTasks(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) { //если такой эпик нашелся
            Epic epic = epics.get(subtask.getEpicID()); //ищем необходимый эпик для добавления подзадачи
            subtask.setId(nextId++); // то подзадача получает id
            subTasks.put(subtask.getId(), subtask); //и сохраняется в subTasks
            epic.addSubTaskID(subtask.getId()); // и добавляется в список эпика
            calculateEpicStatus(epic); // добавилась новая подзадача, расчитаем статус эпика
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

//        public void updateSubTasks (Subtask subtask){
//        /*TODO давай в виде алгоритма в целом опишу логику обновления подзадачи, думаю так будет понятнее
//           1) проверяем есть ли такая подзадача, не существующую подзадачу не обновляем
//           2) По замыслу сервиса подзадачи не переходят между Эпиками (это и логично, подзадача из Эпика ген уборка,
//           вряд ли пригодится Эпику получение высшего образования). поэтому проверяем равен ли epicID в новой подзадаче
//           со значением epicID подзадачи, которая уже была ранее в хранилище subTasks
//           3) Обновляем подзадачу в хранилище subTasks
//           4) Пересчитываем статус Эпика, к которой прилинкована эта подзадача */
//
//
//            subTasks.put(subtask.id, subtask);
//
//            if (!epics.isEmpty()) {
//                Epic epic = epics.get(subtask.getEpicID());//ищем необходимый эпик для обновления подзадачи
//                if (epic != null) { //если такой эпик нашелся
//                    calculateEpicStatus(epic); // подзадача была обновлена, расчитаем статус эпика
//                }
//            }
//        }

//        public void updateEpic (Epic epic){
//        /*TODO для обновления Эпика просто заменить в мапе недостаточно. У эпика такие атрибуты как Status и
//           список связанных с ним подзадач (subTasksIDs) определяется не самим эпиком, а его подзадачами.
//           Поэтому фактически метод updateEpic(Epic epic) может обновить всего два поля: name и description.
//           Поэтому нужно вручную переложить значения этих полей в Эпик, который уже есть в хранилище.
//           Соответственно, если там такого id нет, то ничего не делаем*/
//
//
//            epics.put(epic.id, epic);
//        }
//
//        //f. Удаление по идентификатору.
//        public void delByIDtasks ( int id){
//            if (!tasks.isEmpty()) {
//
//                tasks.remove(id);
//            }
//        }

    public void delSubTasksByID(int id) { // удаляя SubTask по ID
        // нужно еще удалить подзадачу из внутреннего хранилища эпика и пересчитать ему статус
        if (subTasks.containsKey(id)) {
            Subtask subTask = subTasks.get(id);
            Epic epic = epics.get(subTask.getEpicID());
            epic.delSubTaskID(id);
            calculateEpicStatus(epic);
            subTasks.remove(id);
        }
    }

//        public void delByIDepics ( int id){
//            //TODO еще нужно удалить из subTasks все связанные с эпиком подзадачи
//            if (!epics.isEmpty()) {
//                epics.remove(id);
//            }
//        }

    //Дополнительные методы - получение списка всех подзадач определённого эпика
//        public ArrayList<Integer> getSubTasksList (Epic epic){
//            //TODO сигнатуру метода нужно сделать такой public ArrayList<SubTask> getSubTasksList (int epicId)
//            return epic.getSubTasksIDs();
//        }

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
