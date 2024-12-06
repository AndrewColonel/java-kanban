public class Main {

    public static void main(String[] args) {
        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;


        //создаем экземпляр менеджераR
        InMemoryTaskManager InMemoryTaskManager = new InMemoryTaskManager();


        //Создание задач, эпиков и подзадач. Объекта передается в качестве параметра
        InMemoryTaskManager.addTasks(new Task("написать cписок дел",
                "простая, обычная, задача", statusNew));
        InMemoryTaskManager.addTasks(new Task("погулять с собакой еще раз",
                "простая, обычная, задача - обновлена", statusNew));

        InMemoryTaskManager.addEpic(new Epic("Переезд", "Это задача -Эпик №1"));
        InMemoryTaskManager.addEpic(new Epic("Проект", "Это задача -Эпик №2"));

        InMemoryTaskManager.addSubTasks(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusNew, 3));
        InMemoryTaskManager.addSubTasks(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3));
        InMemoryTaskManager.addSubTasks(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusNew, 4));


        System.out.println("Поехали!");
        //печать списка всех задач.
        System.out.println("-".repeat(20));
        System.out.println("получаем списки всех задач");
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getTasksList());
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getSubTasksList());
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getEpicsList());


        //добавляем еще подзадачу
        InMemoryTaskManager.addSubTasks(new Subtask("написать и согласовать еще ТЗ2",
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress, 4));

        //обновление и изменение статусов
        InMemoryTaskManager.updateTasks(new Task("написать cписок  дел", 1,
                "простая, обычная, задача", statusDone));
        InMemoryTaskManager.updateTasks(new Task("погулять с собакой еще раз", 2,
                "простая, обычная, задача - обновлена", statusInProgress));

        InMemoryTaskManager.updateEpic(new Epic("Новый Проект", 4, "Это задача для обновленного Эпика №2"));


        InMemoryTaskManager.updateSubTasks(new Subtask("упаковать коробки", 5,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3));
        InMemoryTaskManager.updateSubTasks(new Subtask("не забыть кошку", 6,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 3));
        InMemoryTaskManager.updateSubTasks(new Subtask("написать и согласовать ТЗ", 7,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress, 4));


        //печать списка всех задач после смены статусов.
        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println("поменяли статус задач и добавили подзадачу в Эпик4");
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getTasksList());
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getSubTasksList());
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getEpicsList());


        //История просмотров
        InMemoryTaskManager.getTaskByID(1);
        InMemoryTaskManager.getSubTaskByID(5);
        InMemoryTaskManager.getEpicByID(3);
        InMemoryTaskManager.getTaskByID(1);
        InMemoryTaskManager.getSubTaskByID(5);
        InMemoryTaskManager.getEpicByID(3);
        InMemoryTaskManager.getTaskByID(1);
        InMemoryTaskManager.getSubTaskByID(5);
        InMemoryTaskManager.getEpicByID(3);
        InMemoryTaskManager.getTaskByID(1);
        InMemoryTaskManager.getSubTaskByID(5);
        InMemoryTaskManager.getEpicByID(3);
        InMemoryTaskManager.getTaskByID(1);
        InMemoryTaskManager.getSubTaskByID(5);
        InMemoryTaskManager.getEpicByID(3);

        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getHistory());






        //f. Удаление по идентификатору.
        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println("удаляем объекты по идентификатору");
        System.out.println("-".repeat(20));
        System.out.println("Удаляем Task id 1");
        System.out.println(InMemoryTaskManager.getTasksList());
        System.out.println(InMemoryTaskManager.getTaskByID(1));
        InMemoryTaskManager.delTaskByID(1);
        System.out.println("проверяем удаление Task id 1 ");
        System.out.println(InMemoryTaskManager.getTaskByID(1));
        System.out.println(InMemoryTaskManager.getTasksList());

        System.out.println("-".repeat(20));
        System.out.println("удаляем SubTask 5 ");
        System.out.println(InMemoryTaskManager.getEpicByID(3));
        System.out.println(InMemoryTaskManager.getSubTasksListByEpic(3));
        System.out.println(InMemoryTaskManager.getSubTaskByID(5));
        InMemoryTaskManager.delSubTasksByID(5);
        System.out.println("проверяем удаление SubTask 5 и очистку Эпика");
        System.out.println(InMemoryTaskManager.getSubTaskByID(5));
        System.out.println(InMemoryTaskManager.getEpicByID(3));
        System.out.println(InMemoryTaskManager.getSubTasksListByEpic(3));
        System.out.println("Все оставшиеся подзадачи:");
        System.out.println(InMemoryTaskManager.getSubTasksList());

        System.out.println("-".repeat(20));
        System.out.println("Удаляем Epic 4:");
        System.out.println(InMemoryTaskManager.getEpicByID(4));
        System.out.println("и соответственно его подзадачи:");
        System.out.println(InMemoryTaskManager.getSubTasksListByEpic(4));
        InMemoryTaskManager.delEpicByID(4);
        System.out.println("проверяем удаление Epic 4 и его подзадач");
        System.out.println(InMemoryTaskManager.getEpicByID(4));
        System.out.println(InMemoryTaskManager.getSubTasksListByEpic(4));
        System.out.println(InMemoryTaskManager.getSubTasksList());

        //печать списка всех задач после удаления.
        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println("проверяем общее удаление ");
        System.out.println("-".repeat(20));
        System.out.println("удаляем все задачи");
        System.out.println(InMemoryTaskManager.getTasksList());
        InMemoryTaskManager.delTasks();
        System.out.println(InMemoryTaskManager.getTasksList());
        System.out.println("-".repeat(20));
        System.out.println("удаляем все подзадачи и очищаем список в Эпиках");
        System.out.println(InMemoryTaskManager.getSubTasksList());
        System.out.println(InMemoryTaskManager.getEpicsList());
        InMemoryTaskManager.delSubTasks();
        System.out.println("проверяем удаление");
        System.out.println(InMemoryTaskManager.getSubTasksList());
        System.out.println(InMemoryTaskManager.getEpicsList());
        System.out.println("-".repeat(20));
        System.out.println("удаляем Эпики и соответствующие подзадачи");
        System.out.println(InMemoryTaskManager.getEpicsList());
        System.out.println(InMemoryTaskManager.getSubTasksList());
        InMemoryTaskManager.delEpics();
        System.out.println(InMemoryTaskManager.getEpicsList());
        System.out.println(InMemoryTaskManager.getSubTasksList());


        System.out.println();
        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println(InMemoryTaskManager.getHistory());


    }
}