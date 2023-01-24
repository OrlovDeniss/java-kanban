package ru.yandex.practicum.kanban.manager.history;

import ru.yandex.practicum.kanban.task.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(Long id);

    List<Task> getHistory();
}
