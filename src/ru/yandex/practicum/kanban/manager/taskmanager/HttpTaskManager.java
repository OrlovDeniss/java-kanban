package ru.yandex.practicum.kanban.manager.taskmanager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.web.kv.KVTaskClient;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class HttpTaskManager extends FileBackedTasksManager {

    private String url;
    private KVTaskClient kvClient;
    private final Gson gson;
    private final String TASKS_KEY = "tasks";
    private final String SUBTASKS_KEY = "subtasks";
    private final String EPICS_KEY = "epics";
    private final String HISTORY_KEY = "history";
    private final Type taskListType = new TypeToken<List<Task>>() {
    }.getType();
    private final Type subtaskListType = new TypeToken<List<SubTask>>() {
    }.getType();
    private final Type epicListType = new TypeToken<List<Epic>>() {
    }.getType();

    public HttpTaskManager(String url) throws IOException, InterruptedException {

        super(new File("/resources/data.csv"));
        this.url = url;
        gson = Managers.getGson();
        kvClient = new KVTaskClient(url);

    }

    public HttpTaskManager loadManagerState() throws IOException, InterruptedException {

        HttpTaskManager newManager = new HttpTaskManager(url);

        List<Task> tasks = gson.fromJson(kvClient.load(TASKS_KEY), taskListType);
        List<Epic> epics = gson.fromJson(kvClient.load(EPICS_KEY), epicListType);
        List<SubTask> subTasks = gson.fromJson(kvClient.load(SUBTASKS_KEY), subtaskListType);
        List<Task> historyId = gson.fromJson(kvClient.load(HISTORY_KEY), taskListType);

        if (Objects.nonNull(tasks)) {
            for (Task t : tasks) {
                newManager.add(t);
            }
        }
        if (Objects.nonNull(epics)) {
            for (Epic t : epics) {
                newManager.add(t);
            }
        }
        if (Objects.nonNull(subTasks)) {
            for (SubTask t : subTasks) {
                newManager.add(t);
            }
        }
        if (Objects.nonNull(historyId)) {
            for (Task task : historyId) {
                if (newManager.getInMemoryTasks().containsKey(task.getId())) {
                    newManager.getHistoryManager().add(newManager.getTask(task.getId()));

                } else if (newManager.getInMemoryEpicTasks().containsKey(task.getId())) {
                    newManager.getHistoryManager().add(newManager.getEpic(task.getId()));

                } else if (newManager.getInMemorySubTasks().containsKey(task.getId())) {
                    newManager.getHistoryManager().add(newManager.getSubTask(task.getId()));
                }
            }
        }

        return newManager;
    }

    private void kvClientPut(String key, String json) throws IOException, InterruptedException {

        if (!json.isEmpty()) {
            kvClient.put(key, json);
        }

    }

    @Override
    public void save() throws IOException, InterruptedException {

        kvClientPut(TASKS_KEY, gson.toJson(getAllTasks()));
        kvClientPut(EPICS_KEY, gson.toJson(getAllEpicTasks()));
        kvClientPut(SUBTASKS_KEY, gson.toJson(getAllSubTasks()));
        kvClientPut(HISTORY_KEY, gson.toJson(getHistory()));

    }

    @Override
    public void clearTasks() throws IOException, InterruptedException {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubTasks() throws IOException, InterruptedException {
        super.clearSubTasks();
        save();
    }

    @Override
    public void clearEpicTasks() throws IOException, InterruptedException {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void removeTask(Long id) throws IOException, InterruptedException {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubTask(Long id) throws IOException, InterruptedException {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeEpic(Long id) throws IOException, InterruptedException {
        super.removeEpic(id);
        save();
    }

    @Override
    public Task getTask(Long id) throws IOException, InterruptedException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(Long id) throws IOException, InterruptedException {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(Long id) throws IOException, InterruptedException {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void add(Task task) throws IOException, InterruptedException {
        super.add(task);
        save();
    }

    @Override
    public void add(SubTask subTask) throws IOException, InterruptedException {
        super.add(subTask);
        save();
    }

    @Override
    public void add(Epic epic) throws IOException, InterruptedException {
        super.add(epic);
        save();
    }

    @Override
    public void update(Task task) throws IOException, InterruptedException {
        super.update(task);
        save();
    }

    @Override
    public void update(SubTask subTask) throws IOException, InterruptedException {
        super.update(subTask);
        save();
    }

    @Override
    public void update(Epic epic) throws IOException, InterruptedException {
        super.update(epic);
        save();
    }
}
