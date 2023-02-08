package ru.yandex.practicum.kanban.manager.web.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.kanban.manager.taskmanager.TaskManager;

import java.io.IOException;

public class SubTaskEpicHandler extends AbstractTaskHandler {

    public SubTaskEpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void getById(HttpExchange exchange) throws IOException {
        sendObjectAsJson(exchange, manager.getEpic(getAttributeId(exchange)).getAllSubTasks());
    }

}
