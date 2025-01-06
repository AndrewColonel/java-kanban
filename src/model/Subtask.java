package model;// класс описывающий подзадачу, наследует базовый класс model.Task
public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description, TaskStatus status, int epicID) {
        //Параметризуем конструктор  для создания model.Subtask полями базового класса + epicID
        super(name, description, status);
        this.epicID = epicID;
    }

    public Subtask(String name, int id, String description, TaskStatus status, int epicID) {
        //Параметризуем конструктор model.Subtask полями базового класса + epicID + новый id для обновления
        super(name, id, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    //переопределяем метод toString() для организации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        return "model.Subtask{" +
                "name='" + getName() + '\'' +
                ", id=" + getId() +
                ", epicID=" + getEpicID() +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}




