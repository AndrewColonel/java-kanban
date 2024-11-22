/*
* Для генерации идентификаторов можно использовать числовое поле-счётчик внутри класса TaskManager,
* увеличивая его на 1, когда нужно получить новое значение.
Также советуем применить знания о методах equals() и hashCode(), чтобы реализовать идентификацию задачи по её id.
* При этом две задачи с одинаковым id должны выглядеть для менеджера как одна и та же.
*Эти методы нежелательно переопределять в наследниках. Ваша задача — подумать, почему.
* */


import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private int nextId = 1;

    public void addTasks(Task task) {
        task.id = nextId++;
        tasks.put(task.id, task);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void addSubTasks(Subtask subtask) {
        subtask.id = nextId++;
        subTasks.put(subtask.id, subtask);
    }

    public HashMap<Integer, Subtask> getSubTasks() {
        return subTasks;
    }

    public void addEpic(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.id, epic);
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }







// Как вставляют эпик
// Создали epic без подзадач
// .add(epic) // <- выставился ему id
// Создали подзадачу с subtask.epicId = epic.id
// .add(subtask)


/*    public void add(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.id, epic);
    }

    public void add(Subtask subtask) {
        subtask.id = nextId++;
        subtasks.put(subtask.id, subtask);

        String epicStatus = null; // заменить
        Epic epic = epics.get(subtask.epicId);
        for (Integer sutasksId : epic.sutasksIds) {
            Subtask aSubtask = subtasks.get(sutasksId);
            //..
        }
        epic.status = epicStatus;
    }

    public void update(Task task) {
        tasks.put(task.id, task);
    }


    public void update(Epic epic) {
        epics.put(epic.id, epic);
    }

    public void update(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
    }*/


}
