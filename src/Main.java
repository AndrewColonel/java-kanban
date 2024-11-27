public class Main {

    public static void main(String[] args) {
        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;


        //создаем экземпляр менеджера
        TaskManager taskManager = new TaskManager();


        //Создание задач, эпиков и подзадач. Объекта передается в качестве параметра
        taskManager.addTasks(new Task("написать cписок дел",
                "простая, обычная, задача", statusNew));
        taskManager.addTasks(new Task("погулять с собакой еще раз",
                "простая, обычная, задача - обновлена", statusNew));

        taskManager.addEpic(new Epic("Переезд", "Это задача -Эпик №1"));
        taskManager.addEpic(new Epic("Проект", "Это задача -Эпик №2"));

        taskManager.addSubTasks(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusNew, 3));
        taskManager.addSubTasks(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3));
        taskManager.addSubTasks(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusNew, 4));


        System.out.println("Поехали!");
        //печать списка всех задач.
        System.out.println("-".repeat(20));
        System.out.println("получаем списки всех задач");
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTasksList());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getSubTasksList());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getEpicsList());


        //добавляем еще подзадачу
        taskManager.addSubTasks(new Subtask("написать и согласовать еще ТЗ2",
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress, 4));

        //оьбновление  и изменение статусов
        taskManager.updateTasks(new Task("написать cписок  дел", 1,
                "простая, обычная, задача", statusDone));
        taskManager.updateTasks(new Task("погулять с собакой еще раз", 2,
                "простая, обычная, задача - обновлена", statusInProgress));

        taskManager.updateEpic(new Epic("Новый Проект", 4, "Это задача для обновленного Эпика №2"));


        taskManager.updateSubTasks(new Subtask("упаковать коробки", 5,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3));
        taskManager.updateSubTasks(new Subtask("не забыть кошку", 6,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3));
        taskManager.updateSubTasks(new Subtask("написать и согласовать ТЗ", 7,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress, 4));


        //печать списка всех задач после смены статусов.
        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println("поменяли статус задач и добавили позадачу в Эпик4");
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTasksList());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getSubTasksList());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getEpicsList());


        //f. Удаление по идентификатору.
        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println("удаляем объекты по идентификатору");
        System.out.println("-".repeat(20));
        System.out.println("Удаляем Task id 1");
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getTaskByID(1));
        taskManager.delTaskByID(1);
        System.out.println("проверяем удаление Task id 1 ");
        System.out.println(taskManager.getTaskByID(1));
        System.out.println(taskManager.getTasksList());

        System.out.println("-".repeat(20));
        System.out.println("удаляем SubTask 5 ");
        System.out.println(taskManager.getEpicByID(3));
        System.out.println(taskManager.getSubTasksListByEpic(3));
        System.out.println(taskManager.getSubTaskByID(5));
        taskManager.delSubTasksByID(5);
        System.out.println("проверяем удаление SubTask 5 и очистку Эпика");
        System.out.println(taskManager.getSubTaskByID(5));
        System.out.println(taskManager.getEpicByID(3));
        System.out.println(taskManager.getSubTasksListByEpic(3));
        System.out.println("Все оставшиеся подзадачи:");
        System.out.println(taskManager.getSubTasksList());

        System.out.println("-".repeat(20));
        System.out.println("Удаляем Epic 4:");
        System.out.println(taskManager.getEpicByID(4));
        System.out.println("и соответсвенно его подзадачи:");
        System.out.println(taskManager.getSubTasksListByEpic(4));
        taskManager.delEpicByID(4);
        System.out.println("проверяем удаление Epic 4 и его подзадач");
        System.out.println(taskManager.getEpicByID(4));
        System.out.println(taskManager.getSubTasksListByEpic(4));
        System.out.println(taskManager.getSubTasksList());

        //печать списка всех задач после удаления.
        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println("проверяем общее удаление ");
        System.out.println("-".repeat(20));
        System.out.println("удаляем все задачи");
        System.out.println(taskManager.getTasksList());
        taskManager.delTasks();
        System.out.println(taskManager.getTasksList());
        System.out.println("-".repeat(20));
        System.out.println("удаляем все подзадачи и очищаем список в Эпиках");
        System.out.println(taskManager.getSubTasksList());
        System.out.println(taskManager.getEpicsList());
        taskManager.delSubTasks();
        System.out.println("проверяем удаление");
        System.out.println(taskManager.getSubTasksList());
        System.out.println(taskManager.getEpicsList());
        System.out.println("-".repeat(20));
        System.out.println("удаляем Эпики и соответсвующие подзадачи");
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubTasksList());
        taskManager.delEpics();
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubTasksList());

    }
}