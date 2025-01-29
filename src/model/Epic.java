package model;// Большая задача, которая делится на подзадачи, называется эпиком. Наследует model.Task

import java.util.List;
import java.util.ArrayList;

public class Epic extends Task {
    private List<Integer> subTasksIDs;
    //пользуемся родительским полем status, определенном в конструкторе базового класса как NEW

    public Epic(String name, String description) {
        //Параметризуем конструктор model.Epic полями name и description
        // статус уже в родительском конструкторе заполнится значением NEW
        super(name, description);// вызов соответствующего конструктора базового класса
        subTasksIDs = new ArrayList<>(); // создание массива хранения id подзадач
    }

    public Epic(String name, int id, String description) {
        //Параметризуем конструктор model.Epic полями name и description + id ля обновления
        // статус уже в родительском конструкторе заполнится значением NEW
        super(name, id, description);// вызов соответствующего контсруткора базового класса
        subTasksIDs = new ArrayList<>(); // создание массива хранения id подзадач
    }

    public Epic(Epic epic) {
        //еще один конструктор для обертки эпиков
        super(epic.getName(), epic.getId(), epic.getDescription(), epic.getStatus());
        subTasksIDs = epic.getSubTasksIDs();
    }

    // для всех атрибутов класса нужны геттеры и сеттеры
    public List<Integer> getSubTasksIDs() {
        //чтобы не открывать доступ к private переменной, можно subTasksIDs обернуть в new ArrayList<>()
        ArrayList<Integer> subTasksIDs;
        subTasksIDs = new ArrayList<>(this.subTasksIDs);
        return subTasksIDs;
    }

    //в этот класс нужно добавить три метода
    // 1) удалит единичную подзадачу из хранилища subTasksIDs
    public void delSubTaskID(Integer id) {
        subTasksIDs.remove(id);
    }

    // 2) очистит все данные хранилища subTasksIDs
    public void delAllSubTasksIDs() {
        subTasksIDs.clear();
    }

    // 3) добавит единичную подзадачу в хранилище subTasksIDs
    public void addSubTaskID(Integer id) {
        if (!subTasksIDs.contains(id) && (id != this.getId())) {
            //добавлена условие для коррекции ошибки - совпадения id эпика и подзадачи
            //чтобы эпик нельзя было добавить в себя же как подзадачу
            subTasksIDs.add(id);
        }
    }

    //переопределяем метод toString() для организации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,", getId(),
                TaskType.EPIC, getName(), getStatus(),getDescription());
    }
}
