package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY = 10;
    private List<Task> historyTasks = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (historyTasks.size() < MAX_HISTORY) {
            historyTasks.add(task);
        } else {
            historyTasks.remove(0);
            historyTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }
}
