// Большая задача, которая делится на подзадачи, называется эпиком. Наследует Task

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIDs;
    //TODO нужно пользоваться родительским полем status
    // private TaskStatus epicStatus;

    public Epic(String name,String description) {
        //TODO не нужно из Task делать Epic. Параметризуй конструктор Epic полями name и description,
        // статус уже в родительском конструкторе заполнится значением NEW
        super(name, description);// вызов соотвесвтующего контсруткора базового класса
        //epicStatus = TaskStatus.NEW; // при создании эпик получает статус - NEW
        subTasksIDs = new ArrayList<>(); // создание массива хранения id подзадач
    }

    public ArrayList<Integer> getSubTasksIDs() {
        //TODO чтобы не открывать доступ к private переменной, можно subTasksIDs обернуть в new ArrayList<>()
        ArrayList<Integer> subTasksIDs;
        subTasksIDs = new ArrayList<>(this.subTasksIDs);
        return subTasksIDs;
    }

//    public void setSubTasksIDs(ArrayList<Integer> subTasksIDs) {
//        this.subTasksIDs = subTasksIDs;
//    }

    //    public void setEpicStatus(TaskStatus epicStatus) { //TODO сеттер будет в родительском классе
//        this.epicStatus = epicStatus;
//    }

    //TODO в этот класс нужно добавить три метода
    // 1) удалит единичную подзадачу из хранилища subTasksIDs
    // 2) очистит все данные хранилища subTasksIDs
    // 3) добавит единичную подзадачу в хранилище subTasksIDs

    public void delSubTaskID (int id) {
        subTasksIDs.remove(id);
    }

    public void delAllSubTasksIDs() {
        subTasksIDs.clear();
    }

    public void addSubTaskID(int id) {
        subTasksIDs.add(id);
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
