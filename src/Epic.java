// Большая задача, которая делится на подзадачи, называется эпиком. Наследует обычную Задачу - Task

import java.util.ArrayList;

public class Epic extends Task {
    //    private int epicId;
//    private String epicName;
    private ArrayList<Integer> subtasksIds;
    private TaskStatus epicStatus;

    public Epic(Task task) {
        super(task.name, task.description);
//        epicId = task.id;
//        epicName = task.name;
        epicStatus = TaskStatus.NEW;
        subtasksIds = new ArrayList<>();
    }



    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public TaskStatus getEpicStatus() {
        return epicStatus;
    }

    public void setEpicStatus(TaskStatus epicStatus) {
        this.epicStatus = epicStatus;
    }

    @Override
    public String toString() {

        String result;
        result = "Epic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'';
        if (!subtasksIds.isEmpty()) {
            result = result + ", subtasksIds=" + subtasksIds;
        } else {
            result = result + ", subtasksIds=NULL";
        }
        result = result + ", epicStatus=" + epicStatus + "}";
        return result;
    }


}
