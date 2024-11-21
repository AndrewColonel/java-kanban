// Для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask).

public class Subtask extends Task {
   private int epicId;

    public Subtask(int id, String description, String status, int epicId) {
        super(id, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}

