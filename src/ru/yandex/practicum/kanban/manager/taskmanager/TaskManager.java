package ru.yandex.practicum.kanban.manager.taskmanager;

import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    void clearTasks() throws IOException, InterruptedException;

    void clearSubTasks() throws IOException, InterruptedException;

    void clearEpicTasks() throws IOException, InterruptedException;

    void removeTask(Long id) throws IOException, InterruptedException;

    void removeSubTask(Long id) throws IOException, InterruptedException;

    void removeEpic(Long id) throws IOException, InterruptedException;

    Task getTask(Long id) throws IOException, InterruptedException;

    SubTask getSubTask(Long id) throws IOException, InterruptedException;

    Epic getEpic(Long id) throws IOException, InterruptedException;

    List<SubTask> getEpicSubTasks(Epic epic);

    void add(Task task) throws IOException, InterruptedException;

    void add(SubTask subTask) throws IOException, InterruptedException;

    void add(Epic epic) throws IOException, InterruptedException;

    void update(Task task) throws IOException, InterruptedException;

    void update(SubTask subTask) throws IOException, InterruptedException;

    void update(Epic epic) throws IOException, InterruptedException;

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpicTasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
