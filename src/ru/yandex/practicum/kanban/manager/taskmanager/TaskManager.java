package ru.yandex.practicum.kanban.manager.taskmanager;

import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    void clearTasks();

    void clearSubTasks();

    void clearEpicTasks();

    void removeTask(Long id);

    void removeSubTask(Long id);

    void removeEpic(Long id);

    Task getTask(Long id);

    SubTask getSubTask(Long id);

    Epic getEpic(Long id);

    List<SubTask> getEpicSubTasks(Epic epic);

    void add(Task task);

    void add(SubTask subTask);

    void add(Epic epic);

    void update(Task task);

    void update(SubTask subTask);

    void update(Epic epic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpicTasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
