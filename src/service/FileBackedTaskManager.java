package service;

import exceptions.ManagerLoadExeption;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    //новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле
    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        List<Task> taskList = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(
                new FileReader(file.toPath().toString(), StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                Task task = manager.fromString(fileReader.readLine());
                if (task != null) taskList.add(task);
            }
        } catch (IOException e) {
            //Исключения вида IOException нужно отлавливать внутри метода save
            // и выкидывать собственное непроверяемое исключение ManagerSaveException.
            // Благодаря этому можно не менять сигнатуру методов интерфейса менеджера.
            throw new ManagerLoadExeption("Произошла ошибка во время чтения файла.");
        }
        manager.memmoryBuild(taskList);
//        manager.fileReaderTest(file);
        return manager;
    }

//    public static FileBackedTaskManager loadFromCSVFile(File file) {
//        FileBackedTaskManager manager = new FileBackedTaskManager(file);
//        List<Task> taskList = new ArrayList<>();
//
//        try (BufferedReader fileReader = new BufferedReader(
//                new FileReader(file.toPath().toString(), StandardCharsets.UTF_8))) {
//            while (fileReader.ready()) {
//                Task task = manager.fromString(fileReader.readLine());
//                if (task != null) taskList.add(task);
//            }
//        } catch (IOException e) {
//            System.out.println("Произошла ошибка во время чтения файла.");
//        }
//
////        for (Task task : taskList) {
////            System.out.println(task);
////            manager.buildCSV(task);
////        }
////        System.out.println(taskList);
//
//        manager.memmoryBuildCSV(taskList);
//
//        return manager;
//    }
//
////    public <T extends Task> void buildCSV(T task) {
////        add(task);
////    }
//
//    public <T extends Task> void memmoryBuildCSV(List<T> taskList) {
//        for (T task : taskList) {
//
////            if (task instanceof Subtask subtask) {
////                System.out.println(TaskType.SUBTASK);
////            } else if (task instanceof Epic epic) {
////                System.out.println(TaskType.EPIC);
////            } else {
////                System.out.println(TaskType.TASK);
////            }
//
//            add(task);
//        }
//
//    }

    public void memmoryBuild(List<Task> tasklist) {
        for (Task task : tasklist) {
//            System.out.println(task);
            if (task instanceof Subtask subtask) {
                add(subtask);
            } else if (task instanceof Epic epic) {
                add(epic);
            } else {
                add(task);
            }
        }
    }

    public Task fromString(String value) { //метод разбора строки в поля конструктора задач
        String[] taskfields = value.split(",");
        switch (taskfields[1]) {
            case "TASK":
                return new Task(taskfields[2], (Integer.parseInt(taskfields[0])),
                        taskfields[4], TaskStatus.valueOf(taskfields[3]));
            case "SUBTASK":
                return new Subtask(taskfields[2], (Integer.parseInt(taskfields[0])),
                        taskfields[4], TaskStatus.valueOf(taskfields[3]),
                        (Integer.parseInt(taskfields[5])));
            case "EPIC":
                return new Epic(taskfields[2], (Integer.parseInt(taskfields[0])),
                        taskfields[4]);
            default:
                return null;
        }
    }

//    public void fileReaderTest(File file) {
//        Path filepath = file.toPath();
//        try {
//            if (Files.exists(filepath)) {
//                String fileString = Files.readString(filepath);
//                System.out.println(fileString);
//                String[] strings = fileString.split(",");
//                System.out.println();
//                System.out.println(strings[0]);
//                System.out.println(strings[10]);
//                System.out.println(strings[2]);
////                for (String string : strings) {
////                    System.out.println(string);
////                }
//            }
//
//        } catch (IOException e) {
//            System.out.println("Произошла ошибка во время чтения файла.");
//        }
//
//    }


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
        } catch (IOException e) {
            //Исключения вида IOException нужно отлавливать внутри метода save
            // и выкидывать собственное непроверяемое исключение ManagerSaveException.
            // Благодаря этому можно не менять сигнатуру методов интерфейса менеджера.
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
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

// --------------------------------------------------------------------------------------------------
        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;

        TaskManager managerStart = new FileBackedTaskManager(new File("FileBackedTaskManager.csv"));

        managerStart.add(new Task("написать cписок дел",
                "простая-обычная-задача", statusNew));
        managerStart.add(new Task("погулять с собакой еще раз",
                "простая-обычная-задача", statusNew));

        managerStart.add(new Epic("Переезд", "Это задача -Эпик №1"));
        managerStart.add(new Epic("Проект", "Это задача -Эпик №2"));

        managerStart.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusInProgress, 3));
        managerStart.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3));
        managerStart.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusDone, 4));
// --------------------------------------------------------------------------------------------------


// --------------------------------------------------------------------------------------------------
//        TaskManager managerCSV =
//                FileBackedTaskManager.loadFromCSVFile(new File("FileBackedTaskManager.csv"));
//
//        System.out.println("Задачи:");
//        for (Task task : managerCSV.getTasksList()) {
////            System.out.println(task);
//            System.out.println(managerCSV.getTaskByID(task.getId()));
//        }
//
//        System.out.println("Эпики:");
//        for (Task epic : managerCSV.getEpicsList()) {
////            System.out.println(epic);
//            System.out.println(managerCSV.getEpicByID(epic.getId()));
//
//            for (Task subtask : managerCSV.getSubTasksListByEpic(epic.getId())) {
//                System.out.println("--> " + subtask);
//                managerCSV.getSubTaskByID(subtask.getId());
//            }
//        }
//        System.out.println("История:");
//        for (Task task : managerCSV.getHistory()) {
//            System.out.println(task);
//        }
//
//
//        System.out.println();
//        System.out.println("-".repeat(20));
//        System.out.println();
// --------------------------------------------------------------------------------------------------


// --------------------------------------------------------------------------------------------------
        TaskManager manager =
                FileBackedTaskManager.loadFromFile(new File("FileBackedTaskManager.csv"));

        System.out.println("Задачи:");
        for (Task task : manager.getTasksList()) {
//            System.out.println(task);
            System.out.println(manager.getTaskByID(task.getId()));
        }

        System.out.println("Эпики:");
        for (Task epic : manager.getEpicsList()) {
//            System.out.println(epic);
            System.out.println(manager.getEpicByID(epic.getId()));

            for (Task subtask : manager.getSubTasksListByEpic(epic.getId())) {
                System.out.println("--> " + subtask);
                manager.getSubTaskByID(subtask.getId());
            }
        }
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
// --------------------------------------------------------------------------------------------------


    }
}