import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import manager.Managers;
import manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;

        //для выбора реализации Менеджера, использую метод утилитарного класса
        TaskManager manager = new InMemoryTaskManager();
//        TaskManager manager = Managers.getDefault();

                //Создание задач, эпиков и подзадач. Объекта передается в качестве параметра
        manager.add(new Task("написать cписок дел",
                "простая, обычная, задача", statusNew, "10.10.2024-00:00", 10L));
        manager.add(new Task("погулять с собакой еще раз",
                "простая, обычная, задача - обновлена", statusNew, "10.11.2024-09:00", 30L));

        manager.add(new Epic("Переезд", "Это задача -Эпик №1"));
        manager.add(new Epic("Проект", "Это задача -Эпик №2"));

        manager.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3,
                "10.01.2025-17:00", 60L));
        manager.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3,
                "11.01.2025-17:55", 5L));
        manager.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusNew, 4,
                "15.02.2025-10:00", 1000L));

        printAllTasks(manager);

    }

    private static void printAllTasks(TaskManager manager) {
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

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        System.out.println("Еще Подзадачи:");
        for (Task subtask : manager.getSubTasksList()) {
//            System.out.println(subtask);
            System.out.println(manager.getSubTaskByID(subtask.getId()));
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        System.out.println("Удаляем задачи подзадачи и эпики:");
        manager.delTasks();
        manager.delSubTasks();
        manager.delEpics();
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
    }
}


//
//            System.out.println("Поехали!");
//            //печать списка всех задач.
//            System.out.println("-".repeat(20));
//            System.out.println("получаем списки всех задач");
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getTasksList());
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getSubTasksList());
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getEpicsList());
//
//
//            //добавляем еще подзадачу
//            manager.addSubTasks(new model.Subtask("написать и согласовать еще ТЗ2",
//                    "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress, 4));
//
//            //обновление и изменение статусов
//            manager.updateTasks(new model.Task("написать cписок  дел", 1,
//                    "простая, обычная, задача", statusDone));
//            manager.updateTasks(new model.Task("погулять с собакой еще раз", 2,
//                    "простая, обычная, задача - обновлена", statusInProgress));
//
//            manager.updateEpic(new model.Epic("Новый Проект", 4, "Это задача для обновленного Эпика №2"));
//
//
//            manager.updateSubTasks(new model.Subtask("упаковать коробки", 5,
//                    "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3));
//            manager.updateSubTasks(new model.Subtask("не забыть кошку", 6,
//                    "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3));
//            manager.updateSubTasks(new model.Subtask("написать и согласовать ТЗ", 7,
//                    "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress, 4));
//
//
//            //печать списка всех задач после смены статусов.
//            System.out.println();
//            System.out.println();
//            System.out.println("-".repeat(20));
//            System.out.println("поменяли статус задач и добавили подзадачу в Эпик4");
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getTasksList());
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getSubTasksList());
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getEpicsList());
//
//
//            //История просмотров
//            manager.getTaskByID(1);
//            manager.getSubTaskByID(5);
//            manager.getEpicByID(3);
//            manager.getTaskByID(1);
//            manager.getSubTaskByID(5);
//            manager.getEpicByID(3);
//            manager.getTaskByID(1);
//            manager.getSubTaskByID(5);
//            manager.getEpicByID(3);
//            manager.getTaskByID(1);
//            manager.getSubTaskByID(5);
//            manager.getEpicByID(3);
//            manager.getTaskByID(1);
//            manager.getSubTaskByID(5);
//            manager.getEpicByID(3);
//
//            System.out.println();
//            System.out.println();
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getHistory());
//
//
//            //f. Удаление по идентификатору.
//            System.out.println();
//            System.out.println();
//            System.out.println("-".repeat(20));
//            System.out.println("удаляем объекты по идентификатору");
//            System.out.println("-".repeat(20));
//            System.out.println("Удаляем model.Task id 1");
//            System.out.println(manager.getTasksList());
//            System.out.println(manager.getTaskByID(1));
//            manager.delTaskByID(1);
//            System.out.println("проверяем удаление model.Task id 1 ");
//            System.out.println(manager.getTaskByID(1));
//            System.out.println(manager.getTasksList());
//
//            System.out.println("-".repeat(20));
//            System.out.println("удаляем SubTask 5 ");
//            System.out.println(manager.getEpicByID(3));
//            System.out.println(manager.getSubTasksListByEpic(3));
//            System.out.println(manager.getSubTaskByID(5));
//            manager.delSubTasksByID(5);
//            System.out.println("проверяем удаление SubTask 5 и очистку Эпика");
//            System.out.println(manager.getSubTaskByID(5));
//            System.out.println(manager.getEpicByID(3));
//            System.out.println(manager.getSubTasksListByEpic(3));
//            System.out.println("Все оставшиеся подзадачи:");
//            System.out.println(manager.getSubTasksList());
//
//            System.out.println("-".repeat(20));
//            System.out.println("Удаляем model.Epic 4:");
//            System.out.println(manager.getEpicByID(4));
//            System.out.println("и соответственно его подзадачи:");
//            System.out.println(manager.getSubTasksListByEpic(4));
//            manager.delEpicByID(4);
//            System.out.println("проверяем удаление model.Epic 4 и его подзадач");
//            System.out.println(manager.getEpicByID(4));
//            System.out.println(manager.getSubTasksListByEpic(4));
//            System.out.println(manager.getSubTasksList());
//
//            //печать списка всех задач после удаления.
//            System.out.println();
//            System.out.println();
//            System.out.println("-".repeat(20));
//            System.out.println("проверяем общее удаление ");
//            System.out.println("-".repeat(20));
//            System.out.println("удаляем все задачи");
//            System.out.println(manager.getTasksList());
//            manager.delTasks();
//            System.out.println(manager.getTasksList());
//            System.out.println("-".repeat(20));
//            System.out.println("удаляем все подзадачи и очищаем список в Эпиках");
//            System.out.println(manager.getSubTasksList());
//            System.out.println(manager.getEpicsList());
//            manager.delSubTasks();
//            System.out.println("проверяем удаление");
//            System.out.println(manager.getSubTasksList());
//            System.out.println(manager.getEpicsList());
//            System.out.println("-".repeat(20));
//            System.out.println("удаляем Эпики и соответствующие подзадачи");
//            System.out.println(manager.getEpicsList());
//            System.out.println(manager.getSubTasksList());
//            manager.delEpics();
//            System.out.println(manager.getEpicsList());
//            System.out.println(manager.getSubTasksList());
//
//
//            System.out.println();
//            System.out.println();
//            System.out.println("-".repeat(20));
//            System.out.println(manager.getHistory());



