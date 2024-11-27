// класс описывающий подзадачу, наследует базовый класс Task

public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description, TaskStatus status, int epicID) {
        //Параметризуем конструктор  для создания Subtask полями базового класса + epicID
        super(name,description, status);
        this.epicID = epicID;
    }


    public Subtask(String name, int id, String description, TaskStatus status, int epicID) {
        //Параметризуем конструктор Subtask полями базового класса + epicID + новый id для обновления
        super(name, id, description, status);
        this.epicID = epicID;
    }

    // для всех атрибутов класса нужны геттеры и сеттеры
    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
    //переопределяем метод toString() для организации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", id=" + getId() +
                ", epicID=" + getEpicID() +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}




