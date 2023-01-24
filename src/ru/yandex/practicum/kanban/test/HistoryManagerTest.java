package ru.yandex.practicum.kanban.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {

    private static final String WRONG_NUMBER_OF_TASKS = "Неверное количество задач.";
    private static final String TASKS_DO_NOT_MATCH = "Задачи не совпадают.";
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void addTaskAndDuplicate() {

        Task task = new Task();

        historyManager.add(task);

        assertEquals(task, historyManager.getHistory().get(0), TASKS_DO_NOT_MATCH);

        assertEquals(1, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

        historyManager.add(task);

        assertEquals(task, historyManager.getHistory().get(0), TASKS_DO_NOT_MATCH);

        assertEquals(1, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeTaskAndRemoveAgain() {

        Task task = new Task();

        historyManager.add(task);

        assertEquals(task, historyManager.getHistory().get(0), TASKS_DO_NOT_MATCH);

        assertEquals(1, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

        historyManager.remove(task.getId());

        assertEquals(0, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

        historyManager.remove(task.getId());

        assertEquals(0, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeFirstHistoryElement() {

        final int historySize = 3;

        for (int i = 0; i < historySize; i++) {
            historyManager.add(new Task());
        }

        List<Task> referenceList = historyManager.getHistory();

        historyManager.remove(referenceList.get(0).getId());

        assertEquals(referenceList.get(1), historyManager.getHistory().get(0), TASKS_DO_NOT_MATCH);
        assertEquals(referenceList.get(2), historyManager.getHistory().get(1), TASKS_DO_NOT_MATCH);

        assertEquals(2, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeMiddleHistoryElement() {

        final int historySize = 3;

        for (int i = 0; i < historySize; i++) {
            historyManager.add(new Task());
        }

        List<Task> referenceList = historyManager.getHistory();

        historyManager.remove(referenceList.get(1).getId());

        assertEquals(referenceList.get(0), historyManager.getHistory().get(0), TASKS_DO_NOT_MATCH);
        assertEquals(referenceList.get(2), historyManager.getHistory().get(1), TASKS_DO_NOT_MATCH);

        assertEquals(2, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void removeLastHistoryElement() {

        final int historySize = 3;

        for (int i = 0; i < historySize; i++) {
            historyManager.add(new Task());
        }

        List<Task> referenceList = historyManager.getHistory();

        historyManager.remove(referenceList.get(2).getId());

        assertEquals(referenceList.get(0), historyManager.getHistory().get(0), TASKS_DO_NOT_MATCH);
        assertEquals(referenceList.get(1), historyManager.getHistory().get(1), TASKS_DO_NOT_MATCH);

        assertEquals(2, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

    }

    @Test
    public void getHistory() {

        assertEquals(0, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

        historyManager.add(new Task());

        assertEquals(1, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

        for (int i = 0; i < 4; i++) {
            historyManager.add(new Task());
        }

        assertEquals(5, historyManager.getHistory().size(), WRONG_NUMBER_OF_TASKS);

    }

}
