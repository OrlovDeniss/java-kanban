package ru.yandex.practicum.kanban.manager.taskmanager;

import ru.yandex.practicum.kanban.Status;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.id.IdManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;
    private static final String fileFields = "id,type,name,status,description,epic,duration,startTime";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<String> fileAllLines = readAllLines(file);

        if (fileAllLines.size() > 1) {

            List<String> taskLines = fileAllLines.subList(1, fileAllLines.size() - 2);

            addTaskLinesToManager(manager, taskLines);

            String historyLine = fileAllLines.get(fileAllLines.size() - 1);

            if (!historyLine.isEmpty()) {
                addHistoryLineToManager(manager, historyLine);
            }
        }
        return manager;
    }

    private void addHistoryLineToManager(FileBackedTasksManager manager, String historyLine) {

        List<Long> historyId = historyFromString(historyLine);
        Map<Long, Task> tasks = manager.getTasks();
        Map<Long, Epic> epicTasks = manager.getEpicTasks();
        Map<Long, SubTask> subTasks = manager.getSubTasks();
        HistoryManager historyManager = manager.getHistoryManager();

        for (Long id : historyId) {
            if (tasks.containsKey(id)) {
                historyManager.add(manager.getTask(id));
            } else if (epicTasks.containsKey(id)) {
                historyManager.add(manager.getEpic(id));
            } else if (subTasks.containsKey(id)) {
                historyManager.add(manager.getSubTask(id));
            }
        }
    }

    private void addTaskLinesToManager(FileBackedTasksManager manager, List<String> taskLines) {

        Long lastIdForIdManager = 0L;

        for (String taskLine : taskLines) {

            Task task = manager.fromString(taskLine);

            if (task instanceof Epic) {
                manager.add((Epic) task);
            } else if (task instanceof SubTask) {
                manager.add((SubTask) task);
            } else {
                manager.add(task);
            }

            if (lastIdForIdManager < task.getId()) {
                lastIdForIdManager = task.getId();
            }
        }

        IdManager.setStartId(lastIdForIdManager + 1L);
    }

    private List<String> readAllLines(File file) {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void save() {

        List<String> writeList = new ArrayList<>();

        writeList.add(fileFields);

        for (Task task : getAllTasks()) {
            writeList.add(toString(task));
        }
        for (Epic task : getAllEpicTasks()) {
            writeList.add(toString(task));
        }
        for (SubTask task : getAllSubTasks()) {
            writeList.add(toString(task));
        }

        writeList.add("");
        writeList.add(historyToString(getHistoryManager()));

        writeFile(file, writeList);
    }

    private void writeFile(File file, List<String> list) {
        try {
            Files.write(file.toPath(), list, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException(e.getCause());
        }
    }

    private String historyToString(HistoryManager historyManager) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Task task : historyManager.getHistory()) {
            stringBuilder.append(task.getId())
                    .append(",");
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    private List<Long> historyFromString(String str) {

        List<Long> historyTasksId = new ArrayList<>();

        for (String s : str.split(",")) {
            historyTasksId.add(Long.parseLong(s));
        }

        return historyTasksId;
    }

    private String toString(Task task) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(task.getId())
                .append(",")
                .append(task.getClass().getSimpleName().toUpperCase())
                .append(",")
                .append(task.getTitle())
                .append(",")
                .append(task.getStatus())
                .append(",")
                .append(task.getDescription())
                .append(",");

        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            stringBuilder.append(subTask.getSuperEpic().getId());
        }

        stringBuilder.append(",")
                .append(task.getDuration().orElse(null))
                .append(",")
                .append(task.getStartTime().orElse(null))
                .append(",");

        return stringBuilder.toString();
    }

    private Task fromString(String str) {

        String[] taskString = str.split(",");

        var id = Long.parseLong(taskString[0]);
        var type = TypeTask.valueOf(taskString[1]);
        var name = taskString[2];
        var status = Status.valueOf(taskString[3]);
        var description = taskString[4];
        var duration = taskString[6].equals("null") ? null : Duration.parse(taskString[6]);
        var startTime = taskString[7].equals("null") ? null : LocalDateTime.parse(taskString[7]);

        Task task;

        switch (type) {

            case TASK:
                task = new Task();
                break;

            case EPIC:
                task = new Epic();
                break;

            case SUBTASK:
                Long epicId = Long.parseLong(taskString[5]);
                task = new SubTask(getEpicTasks().get(epicId));
                break;

            default:
                throw new IllegalArgumentException("Несовместимый тип задачи");
        }

        task.setId(id);
        task.setTitle(name);
        task.setStatus(status);
        task.setDescription(description);
        task.setDuration(duration);
        task.setStartTime(startTime);

        return task;
    }


    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void removeTask(Long id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubTask(Long id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeEpic(Long id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public Task getTask(Long id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(Long id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(SubTask subTask) {
        super.add(subTask);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(SubTask subTask) {
        super.update(subTask);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }
}
