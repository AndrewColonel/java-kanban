// Для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask).

public class Subtask extends Task {
//    private int subTeskId;
//    private String subTaskName;


    public Subtask(Task task) {//констурктор для тестирования
//        super(task.name, task.id, task.description, task.status);
        super(task.name, task.description, task.status);
//        subTeskId = task.id;
//        subTaskName = task.name;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}




