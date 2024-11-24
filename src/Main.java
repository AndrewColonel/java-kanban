public class Main {

    public static void main(String[] args) {
        TaskStatus statusNew = TaskStatus.NEW;
        TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
        TaskStatus statusDone = TaskStatus.DONE;


        //создаем экземпляр менеджера
        TaskManager taskManager = new TaskManager();


        //Создание задач, эпиков и подзадач. Объекта передается в качестве параметра
        taskManager.addTasks(new Task("написать cписок  дел",
                "простая, орбычная, задача", statusNew));
        taskManager.addTasks(new Task("погулять с собакой еще раз",
                "простая, орбычная, задача - обновлена", statusNew));

        taskManager.addEpic(new Epic(new Task("Переезд", "Это задача -Эпик №1")));
        taskManager.addEpic(new Epic(new Task("Проект", "Это задача -Эпик №2")));

        taskManager.addSubTasks(new Subtask(new Task("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusNew), 3));
        taskManager.addSubTasks(new Subtask(new Task("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew), 3));
        taskManager.addSubTasks(new Subtask(new Task("написать и согласовать ТЗ",
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusNew), 4));


        System.out.println("Поехали!");
        //печать списка всех задач.
        System.out.println("-".repeat(20));
        System.out.println("получаем списки всех задач");
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getSubTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getEpics());

        //изменение статусов
        taskManager.updateTasks(new Task("написать cписок  дел", 1,
                "простая, орбычная, задача", statusDone));
        taskManager.updateTasks(new Task("погулять с собакой еще раз", 2,
                "простая, орбычная, задача - обновлена", statusInProgress));

        taskManager.updateSubTasks(new Subtask(new Task("упаковать коробки", 5,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone), 3));
        taskManager.updateSubTasks(new Subtask(new Task("не забыть кошку", 6,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone), 3));
        taskManager.updateSubTasks(new Subtask(new Task("написать и согласовать ТЗ", 7,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusInProgress), 4));


        //печать списка всех задач после смены статусов.
        System.out.println("-".repeat(20));
        System.out.println("поменяли статус задач");
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getSubTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getEpics());


        //f. Удаление по идентификатору.
        System.out.println("-".repeat(20));
        System.out.println("удаляем объекты по идентификатору");
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getByIDtasks(1));
        taskManager.delByIDtasks(1);
        System.out.println(taskManager.getByIDtasks(1));
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getByIDepics(4));
        taskManager.delByIDepics(4);
        System.out.println(taskManager.getByIDepics(4));

        //печать списка всех задач после удеаления.
        System.out.println("-".repeat(20));
        System.out.println("что осталось после удаления задачи и эпика");
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getSubTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getEpics());
    }
}