package service;

import model.Epic;
import model.Subtask;
import model.Task;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    @Override
    public void add (Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add (Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void add (Epic epic) {
        super.add(epic);
        save();
    }

public void save() {

}


}
