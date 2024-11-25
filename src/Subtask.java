// класс описывающий подзадау, наследует базовый класс Task

public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description, TaskStatus status, int epicID) {
        //TODO не нужно из Task делать Subtask. Параметризуй конструктор Subtask теми же полями + epicID
        super(name, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
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




