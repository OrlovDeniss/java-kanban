package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.task.Task;

import java.util.List;

public interface HistoryManager {
    void addTask(Task task);

    List<Task> getHistory();
}
