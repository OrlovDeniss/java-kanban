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

    public HttpTaskServer() throws IOException, InterruptedException {

        manager = Managers.getDefault();

        init();

    }

    private void init() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/tasks/task", new TaskHandler(manager));
        httpServer.createContext("/tasks/epic", new EpicHandler(manager));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(manager));
        httpServer.createContext("/tasks/subtask/epic", new SubTaskEpicHandler(manager));
        httpServer.createContext("/tasks/history", new HistoryHandler(manager));

        System.out.println("server run..");
        httpServer.start();

    }

    public TaskManager getManager() {
        return manager;
    }

    public void stop() {
        httpServer.stop(0);
    }
}
