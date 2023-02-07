package ru.yandex.practicum.kanban.manager.web.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;
import ru.yandex.practicum.kanban.manager.web.server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer httpServer;
    private final TaskManager manager;
    private final String tasksDirectory = "/tasks";

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        init();
    }

    private void init() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext(tasksDirectory, new TasksHandler(manager));
        httpServer.createContext(tasksDirectory + "/task", new TaskHandler(manager));
        httpServer.createContext(tasksDirectory + "/epic", new EpicHandler(manager));
        httpServer.createContext(tasksDirectory + "/subtask", new SubtaskHandler(manager));
        httpServer.createContext(tasksDirectory + "/subtask/epic", new SubTaskEpicHandler(manager));
        httpServer.createContext(tasksDirectory + "/history", new HistoryHandler(manager));
        httpServer.start();
    }

    public TaskManager getManager() {
        return manager;
    }

    public void stop() {
        httpServer.stop(0);
    }
}
