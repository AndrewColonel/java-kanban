// Большая задача, которая делится на подзадачи, называется эпиком. Наследует Task

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIDs;
    private TaskStatus epicStatus;

    public Epic(Task task) {
        super(task.name, task.id, task.description);// вызов соотвесвтующего контсруткора базового класса
        epicStatus = TaskStatus.NEW; // при создании эпик получает статус - NEW
        subTasksIDs = new ArrayList<>(); // создание массива хранения id подзадач
    }

    public ArrayList<Integer> getSubTasksIDs() {
        return subTasksIDs;
    }

    public void setEpicStatus(TaskStatus epicStatus) {
        this.epicStatus = epicStatus;
    }

    //переопределяем метод toString() для олрганизации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {

        String result;
        result = "Epic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'';
        if (!subTasksIDs.isEmpty()) {
            result = result + ", subtasksIds=" + subTasksIDs;
        } else {
            result = result + ", subtasksIds=NULL";
        }
        result = result + ", epicStatus=" + epicStatus + "}";
        return result;
    }
}
