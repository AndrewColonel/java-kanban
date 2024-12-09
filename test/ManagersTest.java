//убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    static InMemoryTaskManager inMemoryTaskManager;
    static InMemoryHistoryManager inMemoryHistoryManager;


    @BeforeAll
    static void startUp() { //создаем объекты inMemoryTaskManager и inMemoryHistoryManager
        inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryHistoryManager = new InMemoryHistoryManager();

    }

    @Test
    void getDefault() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Менеджер не создан");



        if (manager instanceof InMemoryTaskManager) {
            inMemoryTaskManager = (InMemoryTaskManager) manager;
            assertEquals(inMemoryHistoryManager, manager, "Задачи не совпадают.");
        }
    }

    @Test
    void getDefaultHistory() {
    }
}