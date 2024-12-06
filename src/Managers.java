public class Managers<T extends TaskManager> {

    public T getDefault() {
        T taskManager = new TaskManager();
        return taskManager;
    }

}
