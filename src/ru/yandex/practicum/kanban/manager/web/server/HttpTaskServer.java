package ru.yandex.practicum.kanban.manager.web.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.gson.adapter.TaskJsonAdapter;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.manager.web.server.handler.*;
import ru.yandex.practicum.kanban.task.Epic;
import ru.yandex.practicum.kanban.task.SubTask;
import ru.yandex.practicum.kanban.task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer httpServer;
    private final TaskManager manager;
    private final String tasksDirectory = "/tasks";
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskJsonAdapter())
            .registerTypeAdapter(SubTask.class, new TaskJsonAdapter())
            .registerTypeAdapter(Epic.class, new TaskJsonAdapter())
            .create();

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        init();
    }

    private void init() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext(tasksDirectory, new TasksHandler(manager, gson));
        httpServer.createContext(tasksDirectory + "/task", new TaskHandler(manager, gson));
        httpServer.createContext(tasksDirectory + "/epic", new EpicHandler(manager, gson));
        httpServer.createContext(tasksDirectory + "/subtask", new SubtaskHandler(manager, gson));
        httpServer.createContext(tasksDirectory + "/subtask/epic", new SubTaskEpicHandler(manager, gson));
        httpServer.createContext(tasksDirectory + "/history", new HistoryHandler(manager, gson));
        httpServer.start();
    }

    public TaskManager getManager() {
        return manager;
    }

    public void stop() {
        httpServer.stop(0);
    }
}
