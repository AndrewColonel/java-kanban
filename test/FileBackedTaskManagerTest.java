import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import service.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        managerLoad = new FileBackedTaskManager(tmpFile);
    }

    @Test
    void emptyFileSaveLoadTest() throws IOException {
        Long fileSizeBeforeSave = Files.size(tmpFile.toPath());
        managerSave.save();
        Long fileSizeAfterSave = Files.size(tmpFile.toPath());
        // после сохранения "пустого" менеджера, в файл дописывается строка заголовка, увеличивая размер
        assertNotEquals(fileSizeBeforeSave, fileSizeAfterSave, "Размер файла до и после сохранения - равны");

        List<Task> saveTasklist = managerSave.getTasksList();
        List<Subtask> saveSubTasklist = managerSave.getSubTasksList();
        List<Epic> saveEpiclist = managerSave.getEpicsList();

        managerLoad.load(tmpFile);
        Long fileSizeAfterLoad = Files.size(tmpFile.toPath());
        // после загрузки памяти, размер файл поменяться не должен
        assertEquals(fileSizeAfterSave, fileSizeAfterLoad, "Файл до и после операции save-load различны");

        List<Task> loadTasklist = managerLoad.getTasksList();
        List<Subtask> loadSubTasklist = managerLoad.getSubTasksList();
        List<Epic> loadEpiclist = managerLoad.getEpicsList();

        // состояние "памяти"  после операции save-load должны быть равными
        assertEquals(saveTasklist, loadTasklist, "Задачи после save-load не равны");
        assertEquals(saveSubTasklist, loadSubTasklist, "Задачи после save-load не равны");
        assertEquals(saveEpiclist, loadEpiclist, "Задачи после save-load не равны");

        tmpFile.delete();
    }

    @Test
    void filledFileSaveLoadTest() throws IOException {
        Long fileSizeBeforeSave = Files.size(tmpFile.toPath());
        // наполняем менеджер задачами  с помощью  наследуемых методов нового менеджера,
        // и заполняем файл

        managerSave.add(new Task("написать cписок дел",
                "простая-обычная-задача", statusNew));
        managerSave.add(new Task("погулять с собакой еще раз",
                "простая-обычная-задача", statusNew));

        managerSave.add(new Epic("Переезд", "Это задача -Эпик №1"));
        managerSave.add(new Epic("Проект", "Это задача -Эпик №2"));

        managerSave.add(new Subtask("упаковать коробки",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД", statusInProgress, 3));
        managerSave.add(new Subtask("не забыть кошку",
                "Это подзадача для Эпика 1 - ПЕРЕЕЗД!!!", statusNew, 3));
        managerSave.add(new Subtask("написать и согласовать ТЗ", 0,
                "Это подзадача для Эпика 2 - ПРОЕКТ", statusDone, 4));

        Long fileSizeAfterSave = Files.size(tmpFile.toPath());
        // после сохранения "наполненного" менеджера, файл наполняется з
        // адачами из "памяти" менеджера
        assertNotEquals(fileSizeBeforeSave, fileSizeAfterSave, "Размер файла до и после сохранения - равны");

        List<Task> saveTasklist = managerSave.getTasksList();
        List<Subtask> saveSubTasklist = managerSave.getSubTasksList();
        List<Epic> saveEpiclist = managerSave.getEpicsList();

        managerLoad.load(tmpFile);
        Long fileSizeAfterLoad = Files.size(tmpFile.toPath());
        // после загрузки памяти, размер файл поменяться не должен
        assertEquals(fileSizeAfterSave, fileSizeAfterLoad, "Файл до и после операции save-load различны");

        List<Task> loadTasklist = managerLoad.getTasksList();
        List<Subtask> loadSubTasklist = managerLoad.getSubTasksList();
        List<Epic> loadEpiclist = managerLoad.getEpicsList();

        // состояние "памяти"  после операции save-load должны быть равными
        assertEquals(saveTasklist, loadTasklist, "Задачи после save-load не равны");
        assertEquals(saveSubTasklist, loadSubTasklist, "Задачи после save-load не равны");
        assertEquals(saveEpiclist, loadEpiclist, "Задачи после save-load не равны");

        tmpFile.delete();
    }

    @AfterAll
    static void afterAll() {
        tmpFile.deleteOnExit();

    }
}