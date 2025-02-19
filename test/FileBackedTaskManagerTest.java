import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import service.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    static FileBackedTaskManager managerSave;
    static FileBackedTaskManager managerLoad;
    static File tmpFile;

    static TaskStatus statusNew = TaskStatus.NEW;
    static TaskStatus statusInProgress = TaskStatus.IN_PROGRESS;
    static TaskStatus statusDone = TaskStatus.DONE;

    @BeforeEach
    void setUp() throws IOException {
        // создаем  временный пустой файл и экземпляр нового менеджера
        tmpFile = File.createTempFile("data", ".csv");
        managerSave = new FileBackedTaskManager(tmpFile);

    }

    @Test
    void emptyFileSaveLoadTest() throws IOException {
        Long fileSizeBeforeSave = Files.size(tmpFile.toPath());
        List<Task> saveTasklist = managerSave.getTasksList();
        List<Subtask> saveSubTasklist = managerSave.getSubTasksList();
        List<Epic> saveEpiclist = managerSave.getEpicsList();

        managerLoad = FileBackedTaskManager.loadFromFile(tmpFile);
        Long fileSizeAfterLoad = Files.size(tmpFile.toPath());
        // после загрузки памяти, размер файл поменяться не должен
        assertEquals(fileSizeBeforeSave, fileSizeAfterLoad,
                "Файл до и после операции save-load различны");

        List<Task> loadTasklist = managerLoad.getTasksList();
        List<Subtask> loadSubTasklist = managerLoad.getSubTasksList();
        List<Epic> loadEpiclist = managerLoad.getEpicsList();

        // состояние "памяти"  после операции save-load должны быть равными
        for (int i = 0; i < saveTasklist.size(); i++) {
            assertEquals(saveTasklist.get(i), loadTasklist.get(i),
                    "Задачи после save-load не равны");
        }
        for (int i = 0; i < saveSubTasklist.size(); i++) {
            assertEquals(saveSubTasklist.get(i), loadSubTasklist.get(i),
                    "Задачи после save-load не равны");
        }
        for (int i = 0; i < saveEpiclist.size(); i++) {
            assertEquals(saveEpiclist.get(i), loadEpiclist.get(i),
                    "Задачи после save-load не равны");
        }
    }

    @Test
    void filledFileSaveLoadTest() throws IOException {
        Long fileSizeBeforeSave = Files.size(tmpFile.toPath());
        // наполняем менеджер задачами  с помощью  наследуемых методов нового менеджера,
        // и заполняем файл

        managerSave.add(new Epic("Переезд", "Это задача -Эпик №1"));
        managerSave.add(new Epic("Проект", "Это задача -Эпик №2"));

        managerSave.add(new Task("написать cписок дел",
                "простая-обычная-задача", statusNew, "10.10.2026-00:00", 10));
        managerSave.add(new Task("погулять с собакой еще раз",
                "простая-обычная-задача", statusNew, "10.10.2024-09:00", 30));

        managerSave.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusDone, 1,
                "10.01.2025-17:00", 60));
        managerSave.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 1,
                "10.01.2025-16:55", 20));
        managerSave.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusDone, 2,
                "15.02.2025-10:00", 1000));

        managerSave.delTaskByID(4);
        managerSave.delEpicByID(2);

        Long fileSizeAfterSave = Files.size(tmpFile.toPath());
        // после сохранения "наполненного" менеджера, файл наполняется з
        // адачами из "памяти" менеджера
        assertNotEquals(fileSizeBeforeSave, fileSizeAfterSave,
                "Размер файла до и после сохранения - равны");

        List<Task> saveTasklist = managerSave.getTasksList();
        List<Subtask> saveSubTasklist = managerSave.getSubTasksList();
        List<Epic> saveEpiclist = managerSave.getEpicsList();

        managerLoad = FileBackedTaskManager.loadFromFile(tmpFile);
        Long fileSizeAfterLoad = Files.size(tmpFile.toPath());
        // после загрузки памяти, размер файл поменяться не должен
        assertEquals(fileSizeAfterSave, fileSizeAfterLoad,
                "Файл до и после операции save-load различны");

        List<Task> loadTasklist = managerLoad.getTasksList();
        List<Subtask> loadSubTasklist = managerLoad.getSubTasksList();
        List<Epic> loadEpiclist = managerLoad.getEpicsList();

        // состояние "памяти"  после операции save-load должны быть равными
        for (int i = 0; i < saveTasklist.size(); i++) {
            assertEquals(saveTasklist.get(i), loadTasklist.get(i),
                    "Задачи после save-load не равны");
        }
        for (int i = 0; i < saveSubTasklist.size(); i++) {
            assertEquals(saveSubTasklist.get(i), loadSubTasklist.get(i),
                    "Задачи после save-load не равны");
        }
        for (int i = 0; i < saveEpiclist.size(); i++) {

            assertEquals(saveEpiclist.get(i), loadEpiclist.get(i),
                    "Задачи после save-load не равны");
            assertEquals(saveEpiclist.get(i).getStatus(), loadEpiclist.get(i).getStatus(),
                    "Статусы Эпика после save-load не равны");
        }
    }

    @AfterAll
    static void afterAll() {
        tmpFile.deleteOnExit();

    }
}