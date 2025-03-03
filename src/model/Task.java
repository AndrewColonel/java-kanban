package model;//Публичный не абстрактный базовый класс model.Task, который представляет "простую" задачу.

// Базовый класс для подзадач и эпика.

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    // поля базового класса
    private String name;
    private int id;
    private String description;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;


//    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");

    //Конструктор для создания новых задач и подзадач
    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    //Конструктор для создания новых задач и подзадач
    public Task(String name, String description, TaskStatus status, String startTime, Long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm"));
    }

    //Конструктор для создания новых задач и подзадач
    public Task(String name, int id, String description, TaskStatus status, String startTime, Long duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm"));
    }

    //Конструктор для создания новых задач и подзадач после восстановления из файла
    public Task(String name, int id, String description, TaskStatus status, String startTime, String duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
        this.duration = getParsedDuration(duration);
        this.startTime = getParsedStartTime(startTime);
    }

    //конструктор для обновления существующих задач и подзадач
    public Task(String name, int id, String description, TaskStatus status) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
    }

    //конструктор используется для создания эпиков
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        //для Эпиков при создании status должен быть NEW, а не оставаться null
        status = TaskStatus.NEW;
    }

    //конструктор используется для обновления существующих эпиков
    public Task(String name, int id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
        //для Эпиков при создании status должен быть NEW, а не оставаться null
        status = TaskStatus.NEW;
    }

    public Boolean isStartTimeValid() {
        return (startTime != null);
    }

    public Boolean isEndTimeValid() {
        return (getEndTime() != null);
    }

    public Boolean isDurationValid() {
        return (duration != null);
    }

    public LocalDateTime getEndTime() {
        if (isStartTimeValid() && isDurationValid()) return startTime.plus(duration);
        return null;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getFormattedStartTime() { // метод для корректного преобразования toString
        if (isStartTimeValid()) return getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm"));
        return null;
    }

    public String getFormattedEndTime() { // метод для корректного преобразования toString
        if (isEndTimeValid()) return getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm"));
        return null;
    }

    public LocalDateTime getParsedStartTime(String parsedStartTime) {
        // метод для корректного парсинга значений null в объект LocalTimeDate
        if (!parsedStartTime.equals("null")) return LocalDateTime.parse(parsedStartTime, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm"));
        return null;
    }

    public Duration getParsedDuration(String parsedDuration) {
        // метод для корректного парсинга значений null в  Long
        if (!parsedDuration.equals("null"))
            return Duration.ofMinutes(Long.parseLong(parsedDuration));
    return null;
    }

    public Long getDuration() {
        if (isDurationValid()) return duration.toMinutes();
        return null;
    }

    public void setDuration(Long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    //переопределяем метод toString() для организации вывода информации об объекте, будет переопределен в каждом
    //классе отдельно
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,", getId(),
                TaskType.TASK, getName(), getStatus(), getDescription(),
                getFormattedStartTime(), getDuration());
    }

    //методы equals() и hashCode() будут переопределены в базовом классе и будут переданы всем наследникам
    //переопределяем метод для сравнения объектов (задач) - задачи с одинаковыми id будут считаться равными
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    //Переопределяем метод расчета Хэш кода объекта. В расчете будут принимать участие все поля.
    @Override
    public int hashCode() {
        return Objects.hash(name, id, description, status);
    }
}