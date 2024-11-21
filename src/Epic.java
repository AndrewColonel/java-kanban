// Большая задача, которая делится на подзадачи, называется эпиком. Наследует обычную Задачу - Task

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> sutasksIds = new ArrayList<>();


    public Epic(int id, String description, String status, ArrayList<Integer> sutasksIds) {
        super(id, description, status);
        this.sutasksIds = sutasksIds;
    }


    public ArrayList<Integer> getSutasksIds() {
        return sutasksIds;
    }

    public void setSutasksIds(ArrayList<Integer> sutasksIds) {
        this.sutasksIds = sutasksIds;
    }
}
