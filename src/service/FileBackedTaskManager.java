package service;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager {
    // менеджер, который после каждой операции будет автоматически сохранять все задачи и их состояние в специальный файл
    File file;

    public FileBackedTaskManager(File file) {
        //новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        // статический метод, который будет восстанавливать данные менеджера из файла при запуске программы.
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load(file);
        return manager;
    }

    private void save() {
        // метод save без параметров — он будет сохранять текущее состояние менеджера в файл
        // Он должен сохранять все задачи, подзадачи и эпики.
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
            // Исключения вида IOException нужно отлавливать внутри метода save
            // и выкидывать собственное непроверяемое исключение ManagerSaveException.
            // Благодаря этому можно не менять сигнатуру методов интерфейса менеджера.
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    // компоратор с использованием лябда-функции для сортировки списка задач на восстановление по id
//    Comparator<Task> taskCompareByID = (o1, o2) -> (o1.getId() - o2.getId());

    private void load(File file) {
        // метод выполняет загрузку считанных из файла перечня задач разных типов в память
        List<Task> taskList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(
                new FileReader(file.toPath().toString(), StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                // Открываем Optional коробку
                Optional<Task> maybeTask = fromString(fileReader.readLine());
                // if (maybeTask.isPresent()) taskList.add(maybeTask.get());
                maybeTask.ifPresent(taskList::add);
                // Task task = fromString(fileReader.readLine());
                // if (task != null) taskList.add(task);
            }
        } catch (IOException e) {
            // Исключения вида IOException нужно отлавливать внутри метода save
            // и выкидывать собственное непроверяемое исключение ManagerSaveException.
            // Благодаря этому можно не менять сигнатуру методов интерфейса менеджера.
            throw new ManagerLoadException("Произошла ошибка во время чтения файла.");
        }
        taskList.sort((o1, o2) -> (o1.getId() - o2.getId()));
        // после сортировки последний выданный счетчиком id принадлежит крайнему элементу,
        // необходимо для продолжения корректной работы счетчика менеджера установить
        // следующее за крайним id стартовое значение счетчика, т.е. LastID+1
        // для восстановления "памяти" и счетчика (private поля класса InMemmoryTaskManage)
        // используем protected методы того же класса
        if (!taskList.isEmpty()) {
            setNextId(taskList.getLast().getId() + 1);
            for (Task task : taskList) {
                recovery(task);
            }
        }
    }

    private Optional<Task> fromString(String value) {
        // метод создания задачи разных типов из строки
        // отсутствие значения является валидным результатом работы метода, сипользуем Optional
        String[] taskfields = value.split(",");
        switch (taskfields[1]) {
            case "TASK":
                return Optional.of(new Task(taskfields[2], (Integer.parseInt(taskfields[0])),
                        taskfields[4], TaskStatus.valueOf(taskfields[3]),
                        Integer.parseInt(taskfields[6]), taskfields[5]));
            case "SUBTASK":
                return Optional.of(new Subtask(taskfields[2], (Integer.parseInt(taskfields[0])),
                        taskfields[4], TaskStatus.valueOf(taskfields[3]),
                        Integer.parseInt(taskfields[7]),taskfields[6],
                        (Integer.parseInt(taskfields[5]))));
            case "EPIC":
                return Optional.of(new Epic(taskfields[2], (Integer.parseInt(taskfields[0])),
                        taskfields[4]));
            default:
                return Optional.empty();
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

    public static void main(String[] args) {
        // дополнительное задание - реализуем пользовательский сценарий
        System.out.println();
        System.out.println("Обычная работа менеджера");
        System.out.println("-".repeat(20));

        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;

        TaskManager managerSave = new FileBackedTaskManager(new File("FileBackedTaskManager.csv"));

        managerSave.add(new Epic("Переезд", "Это задача -Эпик №1"));
        managerSave.add(new Epic("Проект", "Это задача -Эпик №2"));

        managerSave.add(new Task("написать cписок дел",
                "простая-обычная-задача", statusNew,10, "10.10.2024-00:00"));
        managerSave.add(new Task("погулять с собакой еще раз",
                "простая-обычная-задача", statusNew,30, "10.10.2024-09:00"));

        managerSave.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusInProgress,60,
                "10.01.2025-17:00", 1));
        managerSave.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew,5,
                "10.01.2025-17:55", 1));
        managerSave.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusDone, 1000,
                "15.02.2025-10:00",2));

        managerSave.delTaskByID(4);
        managerSave.delEpicByID(2);

        System.out.println("Задачи:");
        for (Task task : managerSave.getTasksList()) {
//            System.out.println(task);
            System.out.println(managerSave.getTaskByID(task.getId()));
        }

        System.out.println("Эпики:");
        for (Task epic : managerSave.getEpicsList()) {
//            System.out.println(epic);
            System.out.println(managerSave.getEpicByID(epic.getId()));

            for (Task subtask : managerSave.getSubTasksListByEpic(epic.getId())) {
                System.out.println("--> " + subtask);
                managerSave.getSubTaskByID(subtask.getId());
            }
        }
        System.out.println("История:");
        for (Task task : managerSave.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println();
        System.out.println("Работа менеджера после восстановления");
        System.out.println("-".repeat(20));


        // загрузка данных для восстановления работы менеджера из файла
        TaskManager managerLoad =
                FileBackedTaskManager.loadFromFile(new File("FileBackedTaskManager.csv"));

        managerLoad.add(new Epic("Проект", "Это задача -Эпик №2"));
        managerLoad.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusDone,30,
                "17.02.2025-11:30", 7));

        System.out.println("Задачи:");
        for (Task task : managerLoad.getTasksList()) {
//            System.out.println(task);
            System.out.println(managerLoad.getTaskByID(task.getId()));
        }

        System.out.println("Эпики:");
        for (Task epic : managerLoad.getEpicsList()) {
//            System.out.println(epic);
            System.out.println(managerLoad.getEpicByID(epic.getId()));

            for (Task subtask : managerLoad.getSubTasksListByEpic(epic.getId())) {
                System.out.println("--> " + subtask);
                managerLoad.getSubTaskByID(subtask.getId());
            }
        }
        System.out.println("История:");
        for (Task task : managerLoad.getHistory()) {
            System.out.println(task);
        }
    }
}