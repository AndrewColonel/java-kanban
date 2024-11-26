// публичный не абстрактный базовый класс Task, который представляет "простую" задачу.
// Базовый класс для позадач и эпика.

import java.util.Objects;

public class Task {
    // поля базового класса
    private String name;
    private int id;
    private String description;
    private TaskStatus status;

    //констурктор для задач и подзадач без учета id
    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //констурктор для создания эпиков, без учета id
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW; //для Эпиков при создании status должен быть NEW, а не оставаться null
    }

    // для всех атрибутов класса нужны геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    //переопределяем метод toString() для олрганизации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    //методы equals() и hashCode() будут переопределены в базовом классе и будут переданы всем наследникам
    //переопределяем метод для сравнения объектов (задач) - задачи с одинаковыми id будут считаться равными
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        //return id == task.id && Objects.equals(name, task.name) && Objects.equals(description,
        //task.description) && status == task.status;
        return id == task.id;
    }

    // переопределяем метод расчета Хэш кода объекта. В расчетет будуи принимать участие все поля.
    @Override
    public int hashCode() {
        return Objects.hash(name, id, description, status);
    }
}