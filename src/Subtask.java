// класс описывающий подзадау, наследует базовый класс Task

public class Subtask extends Task {
   private int epicID;

    public Subtask(Task task, int epicID) {
        super(task.name, task.id, task.description, task.status); // вызов соотвесвтующего контсруткора базового класса
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    //переопределяем метод toString() для олрганизации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", epicID=" + epicID +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}




