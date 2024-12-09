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
        return historyList;
    }


    @Override
    public void add(Task task) {
        if (historyList.size() == 10) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }
}
