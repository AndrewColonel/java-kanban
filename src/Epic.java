// Большая задача, которая делится на подзадачи, называется эпиком. Наследует Task

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIDs;
    //пользуемся родительским полем status, определенном в конструкторе базового класса как NEW

    public Epic(String name, String description) {
        //Параметризуем конструктор Epic полями name и description
        // статус уже в родительском конструкторе заполнится значением NEW
        super(name, description);// вызов соотвесвтующего контсруткора базового класса
        subTasksIDs = new ArrayList<>(); // создание массива хранения id подзадач
    }

    // для всех атрибутов класса нужны геттеры и сеттеры
    public ArrayList<Integer> getSubTasksIDs() {
        //чтобы не открывать доступ к private переменной, можно subTasksIDs обернуть в new ArrayList<>()
        ArrayList<Integer> subTasksIDs;
        subTasksIDs = new ArrayList<>(this.subTasksIDs);
        return subTasksIDs;
    }

    public void setSubTasksIDs(ArrayList<Integer> subTasksIDs) {
        this.subTasksIDs = subTasksIDs;
    }

    //сеттер для статуса эпика - в родительском классе


    //в этот класс нужно добавить три метода
    // 1) удалит единичную подзадачу из хранилища subTasksIDs
    public void delSubTaskID(int id) {
        subTasksIDs.remove(id);
    }

    // 2) очистит все данные хранилища subTasksIDs
    public void delAllSubTasksIDs() {
        subTasksIDs.clear();
    }

    // 3) добавит единичную подзадачу в хранилище subTasksIDs
    public void addSubTaskID(int id) {
        subTasksIDs.add(id);
    }

    //переопределяем метод toString() для олрганизации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        String result;
        result = "Epic{" +
                "name='" + getName() + '\'' +
                ", id=" + getId() +
                ", description='" + getDescription() + '\'';
        if (!subTasksIDs.isEmpty()) {
            result = result + ", subtasksIds=" + subTasksIDs;
        } else {
            result = result + ", subtasksIds=NULL";
        }
        result = result + ", epicStatus=" + getStatus() + "}";
        return result;
    }
}
