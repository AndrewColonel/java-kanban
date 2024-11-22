// Для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask).

public class Subtask extends Task {
    private int subTeskId;
    private String subTaskName;


    public Subtask(Task task) {
        super(task.name, task.id, task.description, task.status);
        subTeskId = task.id;
        subTaskName = task.name;
    }

    public int getSubTeskId() {
        return subTeskId;
    }

    public void setSubTeskId(int subTeskId) {
        this.subTeskId = subTeskId;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

   /* @Override
    public String toString() {
        return "Subtask{" +
                "subTaskName='" + subTaskName + '\'' +
                ", subTeskId=" + subTeskId +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }*/
}




