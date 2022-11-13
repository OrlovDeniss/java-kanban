package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epicTasks = new HashMap<>();

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();
        for (Epic e : epicTasks.values()) {
            e.clear();
        }
    }

    public void clearEpicTasks() {
        epicTasks.clear();
    }

    public void remove(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.get(id).getSuperEpic().remove(id);
            subTasks.remove(id);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.remove(id);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epicTasks.get(id);
    }

    public List<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getAllSubTasks();
    }

    public void add(Task task) {
        tasks.put(task.getId(), task);
    }

    public void add(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    public void add(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        subTask.getSuperEpic().addSubTask(subTask);
    }

    public void update(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public String toString() {
        return "ru.yandex.practicum.kanban.manager.Manager{" +
                "taskMap=" + tasks +
                ", subTaskMap=" + subTasks +
                ", epicTaskMap=" + epicTasks +
                '}';
    }
}
