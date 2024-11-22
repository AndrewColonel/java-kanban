public class Main {

    public static void main(String[] args) {
        TaskStatus status = TaskStatus.NEW;

        Task newTask1 = new Task("написать cписок  дел", 1,
                "простая, орбычная, задача", status);
        Task newTask2 = new Task("помыть посуду", 2,
                "простая, орбычная, задача", status);
        Task newTask3 = new Task("погулять с собакой", 3,
                "простая, орбычная, задача", status);

        Task newTask4 = new Task("упаковать коробки", 4,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", status);
        Task newTask5 = new Task("не забыть кошку", 4,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", status);
        Task newTask6 = new Task("заказать машину и гркзчиков", 4,
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", status);

        Task newTask7 = new Task("написать и согласовать ТЗ", 7,
                "Это подзадача для Эпика 2 - ПРОЕКТ!!!", status);
        Task newTask8 = new Task("заключить договор", 8,
                "Это подзадача для Эпика 2 - ПРОЕКТ!!!", status);
        Task newTask9 = new Task("получить аванс", 9,
                "Это подзадача для Эпика 2 - ПРОЕКТ!!!", status);


        Task newTask10 = new Task("Переезд", 10,
                "Это задача -Эпик №1");
        Task newTask11 = new Task("Проект", 11,
                "Это задача -Эпик №2");

        Subtask newSubTask1 = new Subtask(newTask4);
        Subtask newSubTask2 = new Subtask(newTask5);
        Subtask newSubTask3 = new Subtask(newTask6);
        Subtask newSubTask4 = new Subtask(newTask7);
        Subtask newSubTask5 = new Subtask(newTask8);
        Subtask newSubTask6 = new Subtask(newTask9);
        Subtask newSubTask7 = new Subtask(newTask4);

        Epic epic1 = new Epic(newTask10);
        Epic epic2 = new Epic(newTask11);


        TaskManager taskManager = new TaskManager();

        taskManager.addTasks(newTask1);
        taskManager.addTasks(newTask2);
        taskManager.addTasks(newTask3);

        taskManager.addSubTasks(newSubTask1);
        taskManager.addSubTasks(newSubTask2);
        taskManager.addSubTasks(newSubTask3);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);






        System.out.println("-".repeat(20));
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getSubTasks());
        System.out.println("-".repeat(20));
        System.out.println(taskManager.getEpics());







       /* System.out.println("Поехали!");
        System.out.println(newTask1);
        System.out.print("Хэшкод: ");
        System.out.println(newTask1.hashCode());
        System.out.println(newTask2);
        System.out.print("Хэшкод: ");
        System.out.println(newTask2.hashCode());
        System.out.println(newTask3);
        System.out.print("Хэшкод: ");
        System.out.println(newTask3.hashCode());

        System.out.println("-".repeat(20));

        System.out.println(newSubTask1);
        System.out.print("Хэшкод: ");
        System.out.println(newSubTask1.hashCode());
        System.out.println(newSubTask2);
        System.out.print("Хэшкод: ");
        System.out.println(newSubTask2.hashCode());
        System.out.println(newSubTask3);
        System.out.print("Хэшкод: ");
        System.out.println(newSubTask3.hashCode());

        System.out.println("-".repeat(20));

        System.out.println(newSubTask4);
        System.out.print("Хэшкод: ");
        System.out.println(newSubTask4.hashCode());
        System.out.println(newSubTask5);
        System.out.print("Хэшкод: ");
        System.out.println(newSubTask5.hashCode());
        System.out.println(newSubTask6);
        System.out.print("Хэшкод: ");
        System.out.println(newSubTask6.hashCode());

        System.out.println("-".repeat(20));

        System.out.println("сравнение объектов");
        System.out.println();
        System.out.println(newTask1.name
                + ", " + newTask1.id + " - 'равен' - " + newTask2.name + ", " + newTask2.id
                + " ---" + newTask1.equals(newTask2));
        System.out.println(newTask2.name
                + " - 'равен' - "
                + ", " + newTask1.id + " - 'равен' - " + newTask3.name + ", " + newTask3.id
                + " ---" + newTask2.equals(newTask3));
        System.out.println(newTask1.name
                + ", " + newTask1.id + " - 'равен' - " + newTask1.name + ", " + newTask1.id
                + " ---" + newTask1.equals(newTask1));

        System.out.println("-".repeat(20));

        System.out.println(newSubTask1.name
                + ", " + newSubTask1.id + " - 'равен' - " + newSubTask2.name + ", " + newSubTask2.id
                + " ---" + newSubTask1.equals(newSubTask2));

        System.out.println(newSubTask2.name
                + ", " + newSubTask2.id + " - 'равен' - " + newSubTask3.name + ", " + newSubTask3.id
                + " ---" + newSubTask2.equals(newSubTask3));

        System.out.println(newSubTask1.name
                + ", " + newSubTask1.id + " - 'равен' - " + newSubTask7.name + ", " + newSubTask7.id
                + " ---" + newSubTask1.equals(newSubTask7));

        System.out.println("-".repeat(20));
        System.out.println("распечатка Эпиков");
        System.out.println();
        System.out.println(epic1);
        System.out.println(epic2);
*/

    }
}
