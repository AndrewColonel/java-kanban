// Большая задача, которая делится на подзадачи, называется эпиком. Наследует обычную Задачу - Task

import java.util.ArrayList;

public class Epic extends Task {
    private int epicId;
    private String epicName;
    private ArrayList<Integer> subtasksIds;
    private TaskStatus epicStatus;

    public Epic(Task task) {
        super(task.name, task.id, task.description);
        epicId = task.id;
        epicName = task.name;
        epicStatus = TaskStatus.NEW;
        subtasksIds = new ArrayList<>();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
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


   /* @Override
    public String toString() {
        String result;
        result = "Epic{" +
                "epicName='" + epicName + '\'' +
                ", epicId=" + epicId +
                ", description='" + description + '\'';
        if (!subtasksIds.isEmpty()) {
            result = result + ", subtasksIds=" + subtasksIds;
        } else {
            result = result + ", subtasksIds=NULL";
        }
        result = result + ", epicStatus=" + epicStatus + "}";
        return result;
    }*/
}
