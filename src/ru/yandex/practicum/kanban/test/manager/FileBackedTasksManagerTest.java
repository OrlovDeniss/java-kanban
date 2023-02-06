package ru.yandex.practicum.kanban.test.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.Status;
import ru.yandex.practicum.kanban.manager.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final FileBackedTasksManager fileBackedTasksManager;
    private final File file = new File("resources/data.csv");

    public FileBackedTasksManagerTest() {

        super(new FileBackedTasksManager(new File("resources/data.csv")));

        fileBackedTasksManager = new FileBackedTasksManager(file);

    }

    @Test
    public void saveAndLoadEmptyFile() throws IOException, InterruptedException {

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
    public void saveAndLoadEpicWithoutHistory() throws IOException, InterruptedException {

        var testEpic = new Epic();
        testEpic.setTitle("Test tittle");
        testEpic.setDescription("Test description");
        testEpic.setDuration(Duration.ofMinutes(30));
        testEpic.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0));

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
    public void saveAndLoadEpicWithHistory() throws IOException, InterruptedException {

        var testEpic = new Epic();

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

    @Test
    public void saveAndLoadTasks() throws IOException, InterruptedException {

        var task = new Task();
        var task2 = new Task();

        fileBackedTasksManager.add(task);
        fileBackedTasksManager.add(task2);

        var managerFromFile = fileBackedTasksManager.loadFromFile(file);

        assertEquals(task, managerFromFile.getTask(task.getId()));
        assertEquals(task2, managerFromFile.getTask(task2.getId()));

    }

    @Test
    public void saveAndLoadEpicWithSubTasks() throws IOException, InterruptedException {

        var testEpic = new Epic();
        var subTask1 = new SubTask(testEpic);
        var subTask2 = new SubTask(testEpic);

        subTask1.setStartTime(LocalDateTime.of(2023, 1, 1, 13, 0));
        subTask2.setStartTime(LocalDateTime.of(2023, 1, 2, 13, 0));

        subTask1.setDuration(Duration.ofMinutes(60));
        subTask2.setDuration(Duration.ofMinutes(60));

        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);

        fileBackedTasksManager.add(testEpic);
        fileBackedTasksManager.add(subTask1);
        fileBackedTasksManager.add(subTask2);

        var managerFromFile = fileBackedTasksManager.loadFromFile(file);

        assertEquals(testEpic, managerFromFile.getEpic(testEpic.getId()));
        assertEquals(subTask1, managerFromFile.getSubTask(subTask1.getId()));
        assertEquals(subTask2, managerFromFile.getSubTask(subTask2.getId()));

        assertEquals(Status.IN_PROGRESS, managerFromFile.getEpic(testEpic.getId()).getStatus());
        assertEquals(Duration.ofMinutes(120), managerFromFile.getEpic(testEpic.getId()).getDuration().get());
        assertEquals(1, managerFromFile.getEpic(testEpic.getId()).getStartTime().get().getDayOfMonth());

    }
}
