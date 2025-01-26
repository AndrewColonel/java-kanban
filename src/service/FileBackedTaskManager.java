package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    //новый менеджер получает файл для автосохранения в своём конструкторе и сохраняет его в поле
    String filepath;

    public FileBackedTaskManager() {
        this.filepath = "FileBackedTaskManager.csv";
        //создаем файл, если его еще нет и записываем строку заголовка полей
        if (!Files.exists(Paths.get(filepath))) {
            try (BufferedWriter fileWriter = new BufferedWriter(
                    new FileWriter(filepath, StandardCharsets.UTF_8))) {
                fileWriter.write("id,type,name,status,description,epic" + "\n");
            } catch (IOException e) { //TODO заменить на ManagerSaveException
                System.out.println("Произошла ошибка во время записи файла.");
            }
        }
    }


    public static void main(String[] args) {

        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;

        TaskManager manager = new FileBackedTaskManager();

        manager.add(new Task("написать cписок дел",
                "простая, обычная, задача", statusNew));
        manager.add(new Task("погулять с собакой еще раз",
                "простая, обычная, задача - обновлена", statusNew));

        manager.add(new Epic("Переезд", "Это задача -Эпик №1"));
        manager.add(new Epic("Проект", "Это задача -Эпик №2"));

        manager.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusNew, 3));
        manager.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3));
        manager.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusNew, 4));

    }

    public void save() {
        //создаем файл, если его еще нет и записываем строку заголовка полей
        try (BufferedWriter fileWriter = new BufferedWriter(
                new FileWriter(filepath, StandardCharsets.UTF_8, true))) {
            fileWriter.write("aaaaaaaaaaaaaa" + "\n");
        } catch (IOException e) { //TODO заменить на ManagerSaveException
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    public String toString(Task task) {

        return task.toString();
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
}
