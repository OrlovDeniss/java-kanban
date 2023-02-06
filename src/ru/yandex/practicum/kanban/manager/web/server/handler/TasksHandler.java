package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;

public class TasksHandler extends AbstractHandler implements HttpHandler {

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));

    }
}
