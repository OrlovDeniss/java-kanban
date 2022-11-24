package ru.yandex.practicum.kanban.manager.taskmanager;

import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epicTasks = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (Epic e : epicTasks.values()) {
            e.clear();
        }
    }

    @Override
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

    @Override
    public Task getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.addTask(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.addTask(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public List<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getAllSubTasks();
    }

    @Override
    public void add(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void add(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void add(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        subTask.getSuperEpic().addSubTask(subTask);
    }

    @Override
    public void update(Epic epic) {
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskMap=" + tasks +
                ", subTaskMap=" + subTasks +
                ", epicTaskMap=" + epicTasks +
                '}';
    }
}
