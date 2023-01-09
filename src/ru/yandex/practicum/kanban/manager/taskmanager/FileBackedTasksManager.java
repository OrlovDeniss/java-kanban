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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;
    private static String fileFields = "id,type,name,status,description,epic";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        var historyFile = new File("resources/data.csv");
        var taskManager = new FileBackedTasksManager(historyFile);

        var task1 = new Task();
        var task2 = new Task();

        var epic1 = new Epic();
        var subTask1 = new SubTask(epic1);
        var subTask2 = new SubTask(epic1);
        var subTask3 = new SubTask(epic1);

        var epic2 = new Epic();

        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(epic1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);
        taskManager.add(epic2);

        taskManager.getSubTask(subTask1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubTask(subTask3.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic2.getId());

        var newTaskManager = loadFromFile(historyFile);
        System.out.println("new FileBackedTasksManager load from file: ");
        for (String s : readAllLines(file)) {
            System.out.println(s);
        }

        System.out.println();

        System.out.println("new FileBackedTasksManager Tasks:");
        for (Task task : newTaskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println();

        System.out.println("new FileBackedTasksManager Epic:");
        for (Epic task : newTaskManager.getAllEpicTasks()) {
            System.out.println(task);
        }

        System.out.println();

        System.out.println("new FileBackedTasksManager SubTask:");
        for (Task task : newTaskManager.getAllSubTasks()) {
            System.out.println(task);
        }

        System.out.println();

        System.out.println("new FileBackedTasksManager History:");
        for (Task task : newTaskManager.getHistory()) {
            System.out.println(task);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<String> fileAllLines = readAllLines(file);

        if (fileAllLines.size() > 1) {

            List<String> taskLines = fileAllLines.subList(1, fileAllLines.size() - 2);
            int lastIdForIdManager = 0;

            if (!fileAllLines.isEmpty()) {

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
            }
            IdManager.setStartId(lastIdForIdManager + 1);

            String historyLine = fileAllLines.get(fileAllLines.size() - 1);

            if (!historyLine.isEmpty()) {
                List<Integer> historyId = historyFromString(historyLine);
                for (int id : historyId) {
                    if (tasks.containsKey(id)) {
                        historyManager.add(manager.getTask(id));
                    } else if (epicTasks.containsKey(id)) {
                        historyManager.add(manager.getEpic(id));
                    } else if (subTasks.containsKey(id)) {
                        historyManager.add(manager.getSubTask(id));
                    }
                }
            }
        }
        return manager;
    }

    private static List<String> readAllLines(File file) {
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
        writeList.add(historyToString(historyManager));

        writeFile(file, writeList);
    }

    private void writeFile(File file, List<String> list) {
        try {
            Files.write(file.toPath(), list, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private static String historyToString(HistoryManager historyManager) {

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

    private static List<Integer> historyFromString(String str) {
        List<Integer> historyTasksId = new ArrayList<>();
        for (String s : str.split(",")) {
            historyTasksId.add(Integer.parseInt(s));
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

        return stringBuilder.toString();
    }

    private Task fromString(String str) {

        String[] taskString = str.split(",");

        int id = Integer.parseInt(taskString[0]);
        TypeTask type = TypeTask.valueOf(taskString[1]);
        String name = taskString[2];
        Status status = Status.valueOf(taskString[3]);
        String description = taskString[4];

        Task task = null;

        switch (type) {
            case TASK:
                task = new Task();
                break;
            case EPIC:
                task = new Epic();
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(taskString[5]);
                task = new SubTask(epicTasks.get(epicId));
                break;
        }

        task.setId(id);
        task.setTitle(name);
        task.setStatus(status);
        task.setDescription(description);
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
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public List<SubTask> getEpicSubTasks(Epic epic) {
        return super.getEpicSubTasks(epic);
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
