package ru.yandex.practicum.kanban.test.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.taskmanager.HttpTaskManager;
import ru.yandex.practicum.kanban.manager.web.kv.KVServer;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    static KVServer kvServer;
    HttpTaskManager manager;

    HttpTaskManagerTest() {
        super(new HttpTaskManager("http://localhost:8078"));
        manager = new HttpTaskManager("http://localhost:8078");
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @Test
    public void saveAndLoadWhenEmpty() {
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(0, manager.getAllEpicTasks().size());
        assertEquals(0, manager.getAllSubTasks().size());
        assertEquals(0, manager.getHistory().size());
        manager.save();

        var newManager = manager.loadManagerState();
        assertEquals(0, newManager.getAllTasks().size());
        assertEquals(0, newManager.getAllEpicTasks().size());
        assertEquals(0, newManager.getAllSubTasks().size());
        assertEquals(0, newManager.getHistory().size());
    }

    @Test
    public void saveAndLoadTasksAndHistory() {
        var task = new Task();
        var epic = new Epic();
        var subTask = new SubTask(epic);
        subTask.setStartTime(LocalDateTime.of(2023, 1, 1, 13, 0));
        subTask.setDuration(Duration.ofMinutes(45));

        manager.add(task);
        manager.add(epic);
        manager.add(subTask);
        manager.getSubTask(subTask.getId());

        var newManager = manager.loadManagerState();
        assertEquals(task, newManager.getAllTasks().get(0));
        assertEquals(epic, newManager.getAllEpicTasks().get(0));
        assertEquals(subTask, newManager.getAllSubTasks().get(0));
        assertEquals(subTask, newManager.getHistory().get(0));
    }

}
