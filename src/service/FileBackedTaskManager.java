package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.*;

import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    //новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле
    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromCSVFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader fileReader = new BufferedReader(
                new FileReader(file.toPath().toString(), StandardCharsets.UTF_8))) {

            while (fileReader.ready()) {
                Task task = manager.fromString(fileReader.readLine());
                if (task != null )    manager.add(task);

            }
        } catch (IOException e) {

        }


        return manager;
    }


    public void save() {
        //создаем файл, если его еще нет и записываем строку заголовка полей
//        Path filepath = file.toPath();
//        try {
//            if (!Files.exists(filepath)) {
//                Files.createFile(filepath);
//            } else {
//                Files.delete(filepath);
//            }
//            Files.writeString(filepath, "id,type,name,status,description,epic" + "\n", StandardCharsets.UTF_8);
//            for (Task task : getTasksList()) {
//                Files.writeString(filepath, task + "\n", StandardCharsets.UTF_8);
//            }
//            } catch (IOException e) {
//       }
        try (BufferedWriter fileWriter = new BufferedWriter(
                new FileWriter(file.toPath().toAbsolutePath().toString(),
                        StandardCharsets.UTF_8, false))) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getTasksList()) {
                fileWriter.write(task + "\n");
            }
            for (Epic epic : getEpicsList()) {
                fileWriter.write(epic + "\n");
            }
            for (Subtask subtask : getSubTasksList()) {
                fileWriter.write(subtask + "\n");
            }
        } catch (IOException e) { //TODO заменить на ManagerSaveException
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    public Task fromString(String value) { //метод разбора строки в поля конструктора задач
        String[] taskfields = value.split(",");
        switch (taskfields[1]) {
            case "TASK":
                return new Task(taskfields[2], (Integer.getInteger(taskfields[0])),
                        taskfields[4], TaskStatus.valueOf(taskfields[3]));

            case "SUBTASK":
                return new Subtask(taskfields[2], (Integer.getInteger(taskfields[0])),
                        taskfields[4], TaskStatus.valueOf(taskfields[3]),
                        (Integer.getInteger(taskfields[5])));

            case "EPIC":
                return new Epic(taskfields[2], (Integer.getInteger(taskfields[0])),
                        taskfields[4]);

            default:
                return null;
        }
    }

    @Override
    public void delTasks() {
        super.delTasks();
        save();
    }

    @Override
    public void delSubTasks() {
        super.delSubTasks();
        save();
    }

    @Override
    public void delEpics() {
        super.delEpics();
        save();

    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void delTaskByID(int id) {
        super.delTaskByID(id);
        save();
    }

    @Override
    public void delSubTasksByID(int id) {
        super.delSubTasksByID(id);
        save();
    }

    @Override
    public void delEpicByID(int id) {
        super.delEpicByID(id);
        save();
    }

    public static void main(String[] args) throws IOException {

        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;

        TaskManager manager = new FileBackedTaskManager(new File("FileBackedTaskManager.csv"));

        manager.add(new Task("написать cписок дел",
                "простая-обычная-задача", statusNew));
        manager.add(new Task("погулять с собакой еще раз",
                "простая-обычная-задача", statusNew));

        manager.add(new Epic("Переезд", "Это задача -Эпик №1"));
        manager.add(new Epic("Проект", "Это задача -Эпик №2"));

        manager.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusInProgress, 3));
        manager.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3));
        manager.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusDone, 4));


        TaskManager manager1 =
                FileBackedTaskManager.loadFromCSVFile(new File("FileBackedTaskManager.csv"));


        System.out.println("Задачи:");
        for (Task task : manager1.getTasksList()) {
//            System.out.println(task);
            System.out.println(manager1.getTaskByID(task.getId()));
        }

        System.out.println("Эпики:");
        for (Task epic : manager1.getEpicsList()) {
//            System.out.println(epic);
            System.out.println(manager1.getEpicByID(epic.getId()));

            for (Task subtask : manager1.getSubTasksListByEpic(epic.getId())) {
                System.out.println("--> " + subtask);
                manager.getSubTaskByID(subtask.getId());

            }
        }
        System.out.println("История:");
        for (Task task : manager1.getHistory()) {
            System.out.println(task);
        }


    }


}


