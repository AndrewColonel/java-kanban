// публичный не абстрактный базовый класс Task, который представляет "простую" задачу.
// Базовый класс для позадач и эпика.

import java.util.Objects;

public class Task {
    // поля базового класса с модификатором protected для предоставления доступа из классов-наследников
    protected String name;
    protected int id;
    protected String description;
    protected TaskStatus status;

    //констурктор для задач и подзадач без учета id
    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //констурктор для переопределения задач и подзадач, с учетом id
    public Task(String name, int id, String description, TaskStatus status) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
    }

    //констурктор для создания эпиков, без учета id
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //констурктор для переопределения эпиков, с учетом id
    public Task(String name, int id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
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