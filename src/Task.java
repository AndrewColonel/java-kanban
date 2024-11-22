// публичного не абстрактного класса Task, который представляет отдельно стоящую задачу.

import java.util.Objects;

public class Task {
    protected String name;
    protected int id;
    protected String description;
    protected TaskStatus status;

    public Task(String name, int id, String description, TaskStatus status) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public Task(String name, int id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
        status = TaskStatus.NEW;
    }



    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
/*        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description,
                task.description) && status == task.status;*/
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, description, status);
    }
}