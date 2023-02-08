package ru.yandex.practicum.kanban.manager.taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.gson.adapter.TaskJsonAdapter;
import ru.yandex.practicum.kanban.manager.web.kv.KVTaskClient;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

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

    public HttpTaskManager(String url) throws IOException {
        this.url = url;
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskJsonAdapter())
                .registerTypeAdapter(SubTask.class, new TaskJsonAdapter())
                .registerTypeAdapter(Epic.class, new TaskJsonAdapter())
                .create();
        kvClient = new KVTaskClient(url);
    }

    public HttpTaskManager loadManagerState() throws IOException {
        var newManager = new HttpTaskManager(url);
        List<Task> tasks = gson.fromJson(kvClient.load(TASKS_KEY), taskListType);
        List<Epic> epics = gson.fromJson(kvClient.load(EPICS_KEY), epicListType);
        List<SubTask> subTasks = gson.fromJson(kvClient.load(SUBTASKS_KEY), subtaskListType);
        List<Task> history = gson.fromJson(kvClient.load(HISTORY_KEY), taskListType);
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
        addHistoryListToManager(newManager, history);
        return newManager;
    }

    private void addHistoryListToManager(HttpTaskManager manager, List<Task> history) {
        if (Objects.nonNull(history)) {
            for (Task task : history) {
                var id = task.getId();
                var historyManager = manager.getHistoryManager();
                if (manager.getInMemoryTasks().containsKey(id)) {
                    historyManager.add(manager.getTask(id));
                } else if (manager.getInMemoryEpicTasks().containsKey(id)) {
                    historyManager.add(manager.getEpic(id));
                } else if (manager.getInMemorySubTasks().containsKey(id)) {
                    historyManager.add(manager.getSubTask(id));
                }
            }
        }
    }

    private void kvClientPut(String key, String json) throws IOException {
        if (!json.isEmpty()) {
            kvClient.put(key, json);
        }
    }

    @Override
    public void save() {
        try {
            kvClientPut(TASKS_KEY, gson.toJson(getAllTasks()));
            kvClientPut(EPICS_KEY, gson.toJson(getAllEpicTasks()));
            kvClientPut(SUBTASKS_KEY, gson.toJson(getAllSubTasks()));
            kvClientPut(HISTORY_KEY, gson.toJson(getHistory()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getCause());
        }
    }

}
