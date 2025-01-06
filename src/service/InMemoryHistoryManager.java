package service;

import model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyList;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
    }

    //истории просмотров задач
    @Override
    public List<Task> getHistory() {
        //чтобы не открывать доступ к private переменной, можно historyList обернуть в new ArrayList<>()
        return new ArrayList<>(historyList);
    }

    @Override
    public void add(Task task) {
        if (historyList.size() == 10) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }
}
