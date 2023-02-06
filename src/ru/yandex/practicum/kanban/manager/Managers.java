package ru.yandex.practicum.kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.gson.adapter.TaskJsonAdapter;
import ru.yandex.practicum.kanban.manager.history.HistoryManager;
import ru.yandex.practicum.kanban.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.manager.taskmanager.HttpTaskManager;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskJsonAdapter())
                .registerTypeAdapter(SubTask.class, new TaskJsonAdapter())
                .registerTypeAdapter(Epic.class, new TaskJsonAdapter())
                .create();
    }

}
