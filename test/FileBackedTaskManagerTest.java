import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest {

    @BeforeAll
    void setUp() {
        try {
            File emptyTmpFile = File.createTempFile("data", null);
//            System.out.println(emptyTmpFile.getCanonicalPath());

            emptyTmpFile.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadFromFile() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

    @Test
    void fromString() {
    }

    @AfterAll
    static void afterAll() {
        try {

            emptyTmpFile.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}