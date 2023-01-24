package ru.yandex.practicum.kanban.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.task.Epic;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private FileBackedTasksManager fileBackedTasksManager;
    private final File file = new File("resources/data.csv");

    public FileBackedTasksManagerTest() {

        super(new FileBackedTasksManager(new File("resources/data.csv")));

        fileBackedTasksManager = new FileBackedTasksManager(file);

    }

    @Test
    public void saveAndLoadEmptyFile() {

        assertEquals(0, fileBackedTasksManager.getAllTasks().size());
        assertEquals(0, fileBackedTasksManager.getAllEpicTasks().size());
        assertEquals(0, fileBackedTasksManager.getAllSubTasks().size());
        assertEquals(0, fileBackedTasksManager.getHistory().size());

        fileBackedTasksManager.save();

        var newManager = fileBackedTasksManager.loadFromFile(file);

        assertEquals(0, newManager.getAllTasks().size());
        assertEquals(0, newManager.getAllEpicTasks().size());
        assertEquals(0, newManager.getAllSubTasks().size());
        assertEquals(0, newManager.getHistory().size());

    }

    @Test
    public void saveAndLoadEpicWithoutHistory() {

        var testEpic = new Epic();
        testEpic.setTitle("Test tittle");
        testEpic.setDescription("Test description");
        testEpic.setDuration(Duration.ofMinutes(30));
        testEpic.setStartTime(LocalDateTime.of(2023,1,1,0,0));

        fileBackedTasksManager.add(testEpic);

        var managerFromFile = fileBackedTasksManager.loadFromFile(file);

        assertEquals(1, managerFromFile.getAllEpicTasks().size());
        assertEquals(0, managerFromFile.getAllTasks().size());
        assertEquals(0, managerFromFile.getAllSubTasks().size());
        assertEquals(0, managerFromFile.getHistory().size());

        var epicFromFile = managerFromFile.getEpic(testEpic.getId());

        assertEquals(testEpic, epicFromFile);

    }

    @Test
    public void saveAndLoadEpicWithHistory() {

        var testEpic = new Epic();
        testEpic.setTitle("Test tittle");
        testEpic.setDescription("Test description");
        testEpic.setDuration(Duration.ofMinutes(15));
        testEpic.setStartTime(LocalDateTime.of(2023, 1,2,13,15));

        fileBackedTasksManager.add(testEpic);
        fileBackedTasksManager.getEpic(testEpic.getId());

        var managerFromFile = fileBackedTasksManager.loadFromFile(file);

        assertEquals(1, managerFromFile.getAllEpicTasks().size());
        assertEquals(0, managerFromFile.getAllTasks().size());
        assertEquals(0, managerFromFile.getAllSubTasks().size());
        assertEquals(1, managerFromFile.getHistory().size());

        var epicFromFile = managerFromFile.getEpic(testEpic.getId());

        assertEquals(testEpic, epicFromFile);

        var taskFromHistory = managerFromFile.getHistory().get(0);

        assertEquals(testEpic, taskFromHistory);

    }

}
