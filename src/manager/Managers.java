package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        // return new InMemoryTaskManager();
        return FileBackedTaskManager.loadFromFile(new File("FileBackedTaskManager.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
